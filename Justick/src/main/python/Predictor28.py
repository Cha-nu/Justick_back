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
        self.fc = nn.Linear(hidden_size, 28)  # 28일 예측

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
            loss = self.criterion(out, y)
            loss.backward()
            for n, p in self.model.named_parameters():
                if p.requires_grad:
                    precision[n] += p.grad.data.pow(2)
        return {n: p / len(self.dataloader) for n, p in precision.items()}

    def penalty(self, model):
        return sum((self._precision_matrices[n] * (p - self.params[n]).pow(2)).sum()
                   for n, p in model.named_parameters() if p.requires_grad)

class Predictor28:
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

    def fit(self, df_raw, cutoff_date="2025-05-27", months=[5, 6], rate="SPECIAL"):
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
        for i in range(len(df) - self.WINDOW - 27):
            window = df.iloc[i:i + self.WINDOW]
            targets = df.iloc[i + self.WINDOW:i + self.WINDOW + 28]
            X_seq.append(window[self.feature_cols].values)
            y_seq.append(targets[self.target_col].values)
            date_seq.append(targets['date'].iloc[-1])

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
                loss = self.criterion(pred, y)
                loss.backward()
                self.optimizer.step()

        self.ewc_list = [EWC(self.model, init_loader, self.criterion)]

        for i in range(len(self.X_seq)):
            if self.date_seq[i] <= cutoff:
                continue

            self.model.eval()
            with torch.no_grad():
                pred = self.model(self.X_seq[i].unsqueeze(0)).squeeze(0).numpy()
                real = self.y_seq[i].numpy()

                pred_log = self.scaler_y.inverse_transform(pred.reshape(-1, 1)).flatten()
                real_log = self.scaler_y.inverse_transform(real.reshape(-1, 1)).flatten()

                pred_rescaled = np.expm1(pred_log)
                real_rescaled = np.expm1(real_log)

                for j in range(28):
                    pred_date = self.date_seq[i] - pd.Timedelta(days=27 - j)
                    self.results.append((pred_date, pred_rescaled[j], real_rescaled[j]))

            if i > 0 and self.date_seq[i - 1] > cutoff:
                loader = DataLoader(TensorDataset(self.X_seq[i - 1].unsqueeze(0), self.y_seq[i - 1].unsqueeze(0)), batch_size=1)
                self.model.train()
                for epoch in range(self.EPOCHS):
                    for x, y in loader:
                        self.optimizer.zero_grad()
                        out = self.model(x)
                        loss = self.criterion(out, y)
                        for ewc in self.ewc_list:
                            loss += self.LAMBDA_EWC * ewc.penalty(self.model)
                        loss.backward()
                        self.optimizer.step()
                self.ewc_list.append(EWC(self.model, loader, self.criterion))

        last_input = torch.tensor(df.iloc[-self.WINDOW:][self.feature_cols].values, dtype=torch.float32).unsqueeze(0)
        self.model.eval()
        with torch.no_grad():
            pred = self.model(last_input).squeeze(0).numpy()
            pred_log = self.scaler_y.inverse_transform(pred.reshape(-1, 1)).flatten()
            pred_rescaled = np.expm1(pred_log)
            start_date = cutoff + pd.Timedelta(days=1)
            for i in range(28):
                self.results.append((start_date + pd.Timedelta(days=i), pred_rescaled[i], None))

    def predict_next_month(self):
        return [round(price, 2) for (_, price, _) in self.results[-28:]]

    def post_latest(self):
        return [{
            "year": date.year,
            "month": date.month,
            "day": date.day,
            "price": int(price),
            "rate": self.rate
        } for (date, price, _) in self.results[-28:]]

    def save(self, path="model_monthly.pth"):
        torch.save(self.model.state_dict(), path)

    def load(self, path="model_monthly.pth"):
        self.model = LSTMModel(input_size=len(self.feature_cols))
        self.optimizer = torch.optim.AdamW(self.model.parameters(), lr=self.LR)
        self.model.load_state_dict(torch.load(path))
        self.model.eval()

if __name__ == "__main__":
    df = pd.read_csv("cabbage_separated.csv")

    model = Predictor28()
    model.fit(df, cutoff_date="2025-05-20", months=[4, 5, 6], rate="HIGH")
    model.post_latest()

    model.update_one_day(df, months=[4, 5, 6], rate="HIGH") #하루 업데이트
    model.save("high_model.pth")      # 저장
    model.load("high_model.pth")      # 불러오기
    model = Predictor28()  # 모델 새로 인스턴스화 (완전 초기화)
    model.fit(df, cutoff_date="2025-07-15", months=[7, 8, 9], rate="SPECIAL")

    #예시
    # 최초 1회만
    model = Predictor28()
    model.fit(df, cutoff_date="2025-05-20", months=[4, 5, 6], rate="SPECIAL")
    model.save("special_model.pth")

    # 이후 매일 아침 업데이트
    model.load("special_model.pth")
    df = pd.read_csv("cabbage_separated.csv")
    model.update_one_day(df, months=[4, 5, 6], rate="SPECIAL")
    model.save("special_model.pth")