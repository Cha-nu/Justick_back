#하루치 완성 모델
import pandas as pd
import numpy as np
import torch
import torch.nn as nn
from sklearn.preprocessing import MinMaxScaler
from torch.utils.data import DataLoader, TensorDataset
import random


torch.manual_seed(42)
np.random.seed(42)
random.seed(42)

class LSTMModel(nn.Module):
    def __init__(self, input_size, hidden_size=64, num_layers=2, dropout=0.2):
        super().__init__()
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, dropout=dropout, batch_first=True)
        self.fc = nn.Linear(hidden_size, 1)

    def forward(self, x):
        out, _ = self.lstm(x)
        return self.fc(out[:, -1, :])

class EWC:
    def __init__(self, model, dataloader, criterion):
        self.model = model
        self.criterion = criterion
        self.dataloader = dataloader
        self.params = {n: p.clone().detach() for n, p in model.named_parameters() if p.requires_grad}
        self._precision_matrices = self._diag_fisher()

    def _diag_fisher(self):
        precision = {n: torch.zeros_like(p) for n, p in self.model.named_parameters() if p.requires_grad}
        self.model.eval()
        for x, y in self.dataloader:
            self.model.zero_grad()
            out = self.model(x)
            loss = self.criterion(out.squeeze(-1), y)
            loss.backward()
            for n, p in self.model.named_parameters():
                if p.requires_grad:
                    precision[n] += p.grad.data.pow(2)
        return {n: p / len(self.dataloader) for n, p in precision.items()}

    def penalty(self, model):
        return sum((self._precision_matrices[n] * (p - self.params[n]).pow(2)).sum()
                   for n, p in model.named_parameters() if p.requires_grad)

class Predictor:
    def __init__(self, window=7, epochs=30, lr=1e-3, lambda_ewc=100):
        self.WINDOW = window
        self.EPOCHS = epochs
        self.LR = lr
        self.LAMBDA_EWC = lambda_ewc
        self.feature_cols = ['intake', 'gap', 'price_diff', 'rolling_mean', 'rolling_std']
        self.target_col = 'log_price'
        self.scaler_x = MinMaxScaler()
        self.scaler_y = MinMaxScaler()
        self.criterion = nn.MSELoss()
        self.results = []
        self.rate = "SPECIAL"  

    def fit(self, df_raw, cutoff_date="2025-05-20", months=[4, 5, 6], rate="SPECIAL"): #기본값은 이렇게 넣어뒀는데 20일걸로 테스트해서, 이거 불러올때 바꿀 수 있어, 수정 안해도 돼
        self.rate = rate
        df = df_raw[df_raw['rate'] == rate].copy()
        df['date'] = pd.to_datetime(df[['year', 'month', 'day']])
        df = df[df['month'].isin(months)].sort_values('date').reset_index(drop=True)
        df['prev_price'] = df['avg_price'].shift(1)
        df['price_diff'] = df['avg_price'] - df['prev_price']
        df['rolling_mean'] = df['avg_price'].rolling(window=3).mean()
        df['rolling_std'] = df['avg_price'].rolling(window=3).std()
        df = df.dropna()
        df['log_price'] = np.log1p(df['avg_price'])

        df[self.feature_cols] = self.scaler_x.fit_transform(df[self.feature_cols])
        df[[self.target_col]] = self.scaler_y.fit_transform(df[[self.target_col]])

        self.df = df.reset_index(drop=True)
        self.dates = df['date'].values

        X_seq, y_seq, date_seq = [], [], []
        for i in range(len(df) - self.WINDOW):
            window = df.iloc[i:i + self.WINDOW]
            target = df.iloc[i + self.WINDOW]
            X_seq.append(window[self.feature_cols].values)
            y_seq.append(target[self.target_col])
            date_seq.append(target['date'])

        self.X_seq = torch.tensor(np.array(X_seq), dtype=torch.float32)
        self.y_seq = torch.tensor(np.array(y_seq), dtype=torch.float32)
        self.date_seq = date_seq

        self.model = LSTMModel(input_size=len(self.feature_cols))
        self.optimizer = torch.optim.AdamW(self.model.parameters(), lr=self.LR)

        cutoff = pd.to_datetime(cutoff_date)
        init_idx = [i for i, d in enumerate(self.date_seq) if d <= cutoff]
        X_init = self.X_seq[init_idx]
        y_init = self.y_seq[init_idx]
        init_loader = DataLoader(TensorDataset(X_init, y_init), batch_size=16, shuffle=True)

        for epoch in range(self.EPOCHS):
            self.model.train()
            for x, y in init_loader:
                self.optimizer.zero_grad()
                pred = self.model(x)
                loss = self.criterion(pred.squeeze(-1), y)
                loss.backward()
                self.optimizer.step()

        self.ewc_list = [EWC(self.model, init_loader, self.criterion)]

        for i in range(len(self.X_seq)):
            if self.date_seq[i] <= cutoff:
                continue

            self.model.eval()
            with torch.no_grad():
                pred = self.model(self.X_seq[i].unsqueeze(0)).item()
                real = self.y_seq[i].item()
                pred_log = self.scaler_y.inverse_transform([[pred]])[0][0]
                real_log = self.scaler_y.inverse_transform([[real]])[0][0]
                pred_rescaled = np.expm1(pred_log)
                real_rescaled = np.expm1(real_log)
                self.results.append((self.date_seq[i], pred_rescaled, real_rescaled))

            if i > 0 and self.date_seq[i - 1] > cutoff:
                loader = DataLoader(TensorDataset(self.X_seq[i - 1].unsqueeze(0), self.y_seq[i - 1].unsqueeze(0)), batch_size=1)
                self.model.train()
                for epoch in range(self.EPOCHS):
                    for x, y in loader:
                        self.optimizer.zero_grad()
                        out = self.model(x)
                        loss = self.criterion(out.squeeze(-1), y)
                        for ewc in self.ewc_list:
                            loss += self.LAMBDA_EWC * ewc.penalty(self.model)
                        loss.backward()
                        self.optimizer.step()
                self.ewc_list.append(EWC(self.model, loader, self.criterion))

        last_input = torch.tensor(df.iloc[-self.WINDOW:][self.feature_cols].values, dtype=torch.float32).unsqueeze(0)
        self.model.eval()
        with torch.no_grad():
            pred = self.model(last_input).item()
            pred_log = self.scaler_y.inverse_transform([[pred]])[0][0]
            pred_rescaled = np.expm1(pred_log)
            next_date = cutoff + pd.Timedelta(days=1)
            self.results.append((next_date, pred_rescaled, None))

    def update_one_day(self, df_raw, months=[4, 5, 6], rate="SPECIAL"):
        self.rate = rate
        df = df_raw[df_raw['rate'] == rate].copy()
        df['date'] = pd.to_datetime(df[['year', 'month', 'day']])
        df = df[df['month'].isin(months)].sort_values('date').reset_index(drop=True)
        df['prev_price'] = df['avg_price'].shift(1)
        df['price_diff'] = df['avg_price'] - df['prev_price']
        df['rolling_mean'] = df['avg_price'].rolling(window=3).mean()
        df['rolling_std'] = df['avg_price'].rolling(window=3).std()
        df = df.dropna()
        df['log_price'] = np.log1p(df['avg_price'])

        df[self.feature_cols] = self.scaler_x.transform(df[self.feature_cols])
        df[[self.target_col]] = self.scaler_y.transform(df[[self.target_col]])

        last_seq = torch.tensor(df.iloc[-self.WINDOW:][self.feature_cols].values, dtype=torch.float32).unsqueeze(0)
        last_target = torch.tensor(df.iloc[-1][self.target_col], dtype=torch.float32).unsqueeze(0)

        self.model.train()
        loader = DataLoader(TensorDataset(last_seq, last_target), batch_size=1)
        for epoch in range(self.EPOCHS):
            for x, y in loader:
                self.optimizer.zero_grad()
                out = self.model(x)
                loss = self.criterion(out.squeeze(-1), y)
                for ewc in self.ewc_list:
                    loss += self.LAMBDA_EWC * ewc.penalty(self.model)
                loss.backward()
                self.optimizer.step()

        self.ewc_list.append(EWC(self.model, loader, self.criterion))

        self.model.eval()
        with torch.no_grad():
            pred = self.model(last_seq).item()
            pred_log = self.scaler_y.inverse_transform([[pred]])[0][0]
            pred_rescaled = np.expm1(pred_log)
            next_date = df['date'].iloc[-1] + pd.Timedelta(days=1)
            self.results.append((next_date, pred_rescaled, None))

    def predict_next(self):
        return self.results[-1][1]

    def post_latest(self):
        date, pred_price, _ = self.results[-1]                                                                                                                                                                                                                                                                                                       
        return {
            "year": date.year,
            "month": date.month,
            "day": date.day,
            "price": int(pred_price),
            "rate": self.rate
        }

    def save(self, path="model.pth"):
        torch.save(self.model.state_dict(), path)

    def load(self, path="model.pth"):
        self.model = LSTMModel(input_size=len(self.feature_cols))
        self.optimizer = torch.optim.AdamW(self.model.parameters(), lr=self.LR)
        self.model.load_state_dict(torch.load(path))
        self.model.eval()

if __name__ == "__main__":
    items = ["cabbage", "onion", "potato", "radish", "sweetPotato", "tomato"]
    grades = ["HIGH", "SPECIAL"]

    for item in items:
        try:
            df = pd.read_csv(f"store/{item}_separated.csv")
        except FileNotFoundError:
            print(f"{item} 파일이 없습니다.")
            continue

        records = []
        for grade in grades:
            print(f"{item} {grade} 모델 학습 시작")
            model = Predictor()
            model.fit(df, cutoff_date="2025-05-31", months=[4, 5, 6], rate=grade)
            result1 = model.post_latest()
            model.save(f"model/{item}_{grade.lower()}_model.pth")
            records.append(result1)

        # 결과를 DataFrame으로 저장
        result_df = pd.DataFrame(records)
        result_df.to_csv(f"store/{item}_record.csv", index=False)
        print(f"{item}_record.csv 저장 완료")
