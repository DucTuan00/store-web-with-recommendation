import pandas as pd
from sklearn.neighbors import NearestNeighbors
from sqlalchemy import create_engine, Column, Integer, String, ForeignKey, Float, Text, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
from flask import Flask, request, jsonify
import datetime

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    username = Column(String(50), nullable=False)
    password = Column(String(250), nullable=False)
    favorite_categories = Column(String(250), nullable=True)
    orders = relationship("UserOrder", back_populates="user")

class Product(Base):
    __tablename__ = 'products'
    id = Column(Integer, primary_key=True)
    name = Column(String(100), nullable=False)
    brand = Column(String(50), nullable=False)
    category = Column(String(50), nullable=False)
    price = Column(String(50), nullable=False)
    description = Column(Text, nullable=True)
    imageFileName = Column("image_file_name", String(250), nullable=True)
    orders = relationship("UserOrder", back_populates="product")

class UserOrder(Base):
    __tablename__ = 'user_order'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('user.id'), nullable=False)
    product_id = Column(Integer, ForeignKey('products.id'), nullable=False)
    phone = Column(String(50), nullable=True)
    address = Column(String(250), nullable=True)
    order_date = Column(DateTime, default=datetime.datetime.utcnow)
    rating = Column(Integer, nullable=True)
    user = relationship("User", back_populates="orders")
    product = relationship("Product", back_populates="orders")

# Create an engine
engine = create_engine('mysql+mysqlconnector://root:123456@localhost:3306/storeweb')

# Create all tables
Base.metadata.create_all(engine)

# Create a configured "Session" class
Session = sessionmaker(bind=engine)

# Create a Session
session = Session()

app = Flask(__name__)

def fetch_data():
    # Fetch user data
    users = session.query(User).all()
    user_data = {
        'user_id': [user.id for user in users],
        'favorite_categories': [user.favorite_categories.split(',') if user.favorite_categories else [] for user in users]
    }
    df_users = pd.DataFrame(user_data)

    # Fetch order data
    orders = session.query(UserOrder).all()
    order_data = {
        'user_id': [order.user_id for order in orders],
        'product_id': [order.product_id for order in orders],
        'rating': [order.rating for order in orders]
    }
    df_orders = pd.DataFrame(order_data)
    
    # Ensure unique user-product pairs
    df_orders = df_orders.groupby(['user_id', 'product_id']).rating.mean().reset_index()

    # User-item matrix for ratings
    user_item_matrix = df_orders.pivot(index='user_id', columns='product_id', values='rating').fillna(0)

    # User-category matrix for preferences
    all_categories = set(cat for sublist in df_users['favorite_categories'] for cat in sublist)
    all_categories = list(all_categories)
    user_category_matrix = pd.DataFrame(0, index=df_users['user_id'], columns=all_categories)
    for index, row in df_users.iterrows():
        user_category_matrix.loc[row['user_id'], row['favorite_categories']] = 1

    # Combined matrix
    combined_matrix = user_item_matrix.copy()
    for category in user_category_matrix.columns:
        combined_matrix[category] = user_category_matrix[category]

    return combined_matrix, df_orders

def recommend(user_id, num_recommendations):
    combined_matrix, df_orders = fetch_data()

    if user_id not in combined_matrix.index:
        return []

    model = NearestNeighbors(metric='cosine', algorithm='brute')
    model.fit(combined_matrix.values)

    user_index = combined_matrix.index.get_loc(user_id)
    distances, indices = model.kneighbors([combined_matrix.iloc[user_index]], n_neighbors=num_recommendations + 1)
    recommended_users = [combined_matrix.index[i] for i in indices.flatten() if i != user_index]
    
    recommended_products = []
    for rec_user in recommended_users:
        user_orders = df_orders[df_orders['user_id'] == rec_user]
        recommended_products.extend(user_orders['product_id'].tolist())
    
    recommended_products = list(set(recommended_products))[:num_recommendations]
    return recommended_products

@app.route('/recommend', methods=['GET'])
def get_recommendations():
    user_id = int(request.args.get('user_id'))
    num_recommendations = int(request.args.get('num_recommendations'))
    recommendations = recommend(user_id, num_recommendations)
    
    products = session.query(Product).filter(Product.id.in_(recommendations)).all()
    recommended_products = [{
        'product_id': product.id,
        'product_name': product.name,
        'category': product.category
    } for product in products]
    
    return jsonify(recommendations)

if __name__ == '__main__': 
    app.run(port=5000)
