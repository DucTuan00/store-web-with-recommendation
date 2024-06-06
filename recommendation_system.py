import pandas as pd
from sqlalchemy import create_engine
import sys
import json
from sklearn.neighbors import NearestNeighbors

# Database connection details
DB_USERNAME = 'postgres'
DB_PASSWORD = '123456'
DB_HOST = 'localhost'
DB_PORT = '5432'
DB_NAME = 'student'
DB_TABLE = 'test'

# Connect to PostgreSQL database using SQLAlchemy
def get_data_from_db():
    try:
        engine = create_engine(f'postgresql://{DB_USERNAME}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}')
        query = f"""
        SELECT user_id, item_id, rating
        FROM {DB_TABLE}
        """
        df = pd.read_sql(query, engine)
        return df
    except Exception as e:
        return {"error": str(e)}

data = {
    'user_id': [1, 1, 2, 2, 3, 3, 4],
    'item_id': [1, 2, 1, 3, 1, 2, 3],
    'rating': [5, 3, 4, 2, 2, 5, 5],
    'category': ['Electronics', 'Books', 'Electronics', 'Clothing', 'Electronics', 'Books', 'Clothing']
}

user_preferences = {
    1: ['Electronics', 'Books'],
    2: ['Electronics'],
    3: ['Books'],
    4: ['Clothing']
}
    
# Fetch data from database
data_result = get_data_from_db()
if "error" in data_result:
    print(json.dumps(data_result))
    sys.exit(1)

df = data_result
user_item_matrix = df.pivot(index='user_id', columns='item_id', values='rating').fillna(0)

model = NearestNeighbors(metric='cosine', algorithm='brute')
model.fit(user_item_matrix.values)

def recommend(user_id, num_recommendations):
    if user_id not in user_item_matrix.index:
        return {"error": f"User ID {user_id} not found."}

    user_index = user_item_matrix.index.get_loc(user_id)
    total_users = user_item_matrix.shape[0]

    if num_recommendations >= total_users:
        num_recommendations = total_users - 1

    try:
        distances, indices = model.kneighbors([user_item_matrix.iloc[user_index]], n_neighbors=num_recommendations + 1)
        recommendations = [user_item_matrix.columns[i] for i in indices.flatten() if i != user_index]
        return list(map(int, recommendations[:num_recommendations]))  # Convert to native int type
       
    except Exception as e:
        return {"error": str(e)}

if __name__ == "__main__":
    try:
        user_id = int(sys.argv[1])
        num_recommendations = int(sys.argv[2])
        recommendations = recommend(user_id, num_recommendations)
        print(json.dumps(recommendations))
    except Exception as e:
        print(json.dumps({"error": str(e)}))
        sys.exit(1)
