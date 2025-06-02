import pandas as pd
from Predictor28 import Predictor28
from Predictor7 import Predictor7
from Predictor import Predictor

all_records = {}
items = ["cabbage", "onion", "potato", "radish", "sweetPotato", "tomato"]
grades = ["HIGH", "SPECIAL"]

# Step 1: 28일 예측
for item in items:
    df = pd.read_csv(f"store/{item}_separated.csv")
    for grade in grades:
        model = Predictor28()
        model.fit(df, cutoff_date="2025-05-31", months=[5, 6], rate=grade)
        pred_28 = model.post_latest()  # list of 28 dicts
        all_records[(item, grade)] = pred_28
        pd.DataFrame(pred_28).to_csv(f"store/{item}_{grade}_test.csv", index=False)

# Step 2: 7일 예측 → 앞 7개만 교체
for item in items:
    df = pd.read_csv(f"store/{item}_separated.csv")
    for grade in grades:
        model = Predictor7()
        model.fit(df, cutoff_date="2025-05-31", months=[5, 6], rate=grade)
        pred_7 = model.post_latest()

        path = f"store/{item}_{grade}_test.csv"
        full_df = pd.read_csv(path)
        full_df.iloc[:7] = pd.DataFrame(pred_7)
        full_df.to_csv(path, index=False)

# Step 3: 1일 예측 → 앞앞 1개만 교체
for item in items:
    df = pd.read_csv(f"store/{item}_separated.csv")
    for grade in grades:
        model = Predictor()
        model.fit(df, cutoff_date="2025-05-31", months=[5, 6], rate=grade)
        pred_1 = model.post_latest()

        path = f"store/{item}_{grade}_test.csv"
        full_df = pd.read_csv(path)
        full_df.iloc[0] = pd.Series(pred_1)
        full_df.to_csv(path, index=False)
