from sqlalchemy import create_engine, Column, Integer, String, Float, DateTime, Text, JSON
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from datetime import datetime
import os

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://travel_user:travel_pass@db:5432/travel_assistant")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

class Destination(Base):
    __tablename__ = "destinations"
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    country = Column(String)
    description = Column(Text)
    estimated_cost = Column(Float)
    recommended_duration = Column(String)
    highlights = Column(JSON)
    created_at = Column(DateTime, default=datetime.utcnow)

class TravelPlan(Base):
    __tablename__ = "travel_plans"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(String)  # In production, this would be a foreign key
    destination_id = Column(Integer)  # Foreign key to destinations
    start_date = Column(DateTime)
    end_date = Column(DateTime)
    budget = Column(Float)
    preferences = Column(JSON)
    status = Column(String, default="draft")
    created_at = Column(DateTime, default=datetime.utcnow)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Create tables
def create_tables():
    Base.metadata.create_all(bind=engine)