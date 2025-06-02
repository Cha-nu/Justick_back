import pandas as pd
import os

# retail.xlsx 파일 읽기
retail_path = 'data/OnionRetail.xlsx'
retail_df = pd.read_excel(retail_path)

# ',' 제거하고 평균가격 숫자로 변환
retail_df['평균가격'] = (
    retail_df['평균가격']
    .astype(str)
    .str.replace(',', '')
    .astype(float)          # 먼저 float으로 변환
    .round()                # 반올림
    .astype(int)            # 최종 정수형 변환
)

# 평균가격이 0이 아닌 행만 필터링
retail_df = retail_df[retail_df['평균가격'] != 0].copy()

# 날짜 분해
retail_df[['year', 'month', 'day']] = retail_df['DATE'].astype(str).str.split('-', expand=True).astype(int)
retail_df.rename(columns={'평균가격': 'avg_price'}, inplace=True)

# 가격 차이(gap) 계산
retail_df.sort_values('DATE', inplace=True)
retail_df['gap'] = retail_df['avg_price'].diff().fillna(0).astype(int)

# 최종 열 선택 및 정렬
final_retail_df = retail_df[['year', 'month', 'day', 'avg_price', 'gap']]
final_retail_df = final_retail_df.sort_values(['year', 'month', 'day'])

# 저장
output_path = 'store/onion_retail.csv'
final_retail_df.to_csv(output_path, index=False)

print(final_retail_df.head())
