import os
import pandas as pd
import numpy as np

# 기본 경로와 작물, 등급
base_path = "store"
filename_template = "{}_separated.csv"
vegetables = ["cabbage", "onion", "potato", "radish", "sweetPotato", "tomato"]
rates = ["HIGH", "SPECIAL"]

# 결과 저장용 딕셔너리
result_dict = {}

# 날짜 필터링 범위
start_date = pd.to_datetime("2025-04-14")
end_date = pd.to_datetime("2025-05-31")

# 각 작물과 등급에 대해 처리
# 각 작물과 등급에 대해 처리
for veg in vegetables:
    filepath = os.path.join(base_path, filename_template.format(veg))
    if not os.path.exists(filepath):
        continue

    df = pd.read_csv(filepath)
    df['date'] = pd.to_datetime(df[['year', 'month', 'day']])
    df = df[(df['date'] >= start_date) & (df['date'] <= end_date)]

    if 'gap' in df.columns:
        df = df.drop(columns=['gap'])

    if 'intake' in df.columns:
        df = df.drop(columns=['intake'])

    merged_df = pd.DataFrame()  # HIGH + SPECIAL 합친 결과용

    for rate in rates:
        sub_df = df[df['rate'] == rate].copy()
        np.random.seed(42)
        sub_df['avg_price'] = sub_df['avg_price'].apply(lambda x: x + np.random.randint(-1500, 1501))
        merged_df = pd.concat([merged_df, sub_df], ignore_index=True)

    # 저장 (date 컬럼 제거하고)
    merged_df.drop(columns=['date']).to_csv(f"store/{veg}_predict.csv", index=False)



