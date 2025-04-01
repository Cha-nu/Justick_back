import csv
import requests
from datetime import datetime

# API URL
url = "http://localhost:8080/api/cabbage"

# CSV 파일 경로
csv_file_path = "data/cleaned_cabbage_price.csv"

# CSV 열 인덱스 참고
# DATE,품목명,단위(키로),등급명,평균가격,전일,전년,반입량

with open(csv_file_path, newline='', encoding='utf-8') as csvfile:
  reader = csv.DictReader(csvfile)

  for row in reader:
    # 평균가격 또는 날짜가 비어있는 행은 제외 (0 또는 빈값 처리)
    if not row["평균가격"] or row["평균가격"] == "0":
      continue

    # 요청 데이터 생성
    data = {
      "date": row["DATE"],
      "rating": int(row["등급명"]),  # 0: 상, 1: 특
      "averagePrice": int(row["평균가격"]),
    }

    response = requests.post(url, json=data)

    if response.status_code == 200:
      print(f"전송 성공: {data['date']} / 등급 {data['rating']}")
    else:
      print(f"전송 실패: {response.status_code} - {response.text}")
