import pandas as pd
import os
from datetime import datetime, timedelta

# 작물 리스트
vegetables = ["cabbage", "onion", "potato", "sweetPotato", "radish", "tomato"]

# 오늘 날짜와 365일 전, 어제 날짜 계산
today = datetime.today().date()
end_date = today - timedelta(days=1)
start_date = end_date - timedelta(days=364)  # 365일(어제 포함) 전

for veg in vegetables:
    input_path = f'store/{veg}_separated.csv'
    output_path = f'store/{veg}_separated_filled.csv'

    if not os.path.exists(input_path):
        print(f"{input_path} 파일이 없습니다.")
        continue

    df = pd.read_csv(input_path)
    df['date'] = pd.to_datetime(df[['year', 'month', 'day']])
    all_dates = pd.date_range(start=start_date, end=end_date, freq='D')
    rates = df['rate'].unique()

    filled_list = []
    for rate in rates:
        sub = df[df['rate'] == rate].copy()
        sub = sub.set_index('date')
        sub = sub.reindex(all_dates)
        sub['rate'] = rate
        sub[['intake', 'avg_price']] = sub[['intake', 'avg_price']].ffill()
        sub['year'] = sub.index.year
        sub['month'] = sub.index.month
        sub['day'] = sub.index.day
        filled_list.append(sub)

    filled_df = pd.concat(filled_list).reset_index(drop=True)
    filled_df.sort_values(['rate', 'year', 'month', 'day'], inplace=True)
    filled_df['gap'] = filled_df.groupby('rate')['avg_price'].diff().fillna(0).astype(int)
    final_filled = filled_df[['year', 'month', 'day', 'intake', 'avg_price', 'gap', 'rate']]
    final_filled = final_filled.sort_values(['rate', 'year', 'month', 'day']).reset_index(drop=True)

    final_filled['intake'] = final_filled['intake'].astype(int)
    final_filled['avg_price'] = final_filled['avg_price'].astype(int)

    os.makedirs('store', exist_ok=True)
    final_filled.to_csv(output_path, index=False)
    print(f"{output_path} 저장 완료")