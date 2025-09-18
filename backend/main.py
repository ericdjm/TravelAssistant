from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import List, Optional
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = FastAPI(
    title="Travel Assistant API",
    description="AI-powered travel assistant backend",
    version="1.0.0"
)

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000", "http://frontend:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Pydantic models
class TravelQuery(BaseModel):
    destination: str
    start_date: Optional[str] = None
    end_date: Optional[str] = None
    budget: Optional[float] = None
    preferences: Optional[List[str]] = []

class TravelRecommendation(BaseModel):
    id: int
    destination: str
    description: str
    estimated_cost: float
    recommended_duration: str
    highlights: List[str]

# In-memory storage for demo (replace with database)
recommendations_db = [
    {
        "id": 1,
        "destination": "Paris, France",
        "description": "The City of Light offers romantic charm, world-class museums, and exceptional cuisine.",
        "estimated_cost": 2500.0,
        "recommended_duration": "5-7 days",
        "highlights": ["Eiffel Tower", "Louvre Museum", "Notre-Dame Cathedral", "Champs-Élysées"]
    },
    {
        "id": 2,
        "destination": "Tokyo, Japan",
        "description": "A fascinating blend of traditional culture and cutting-edge technology.",
        "estimated_cost": 3000.0,
        "recommended_duration": "7-10 days",
        "highlights": ["Shibuya Crossing", "Senso-ji Temple", "Mount Fuji", "Tsukiji Market"]
    }
]

@app.get("/")
async def root():
    return {"message": "Travel Assistant API is running!"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "travel-assistant-api"}

@app.get("/recommendations", response_model=List[TravelRecommendation])
async def get_recommendations():
    """Get all travel recommendations"""
    return recommendations_db

@app.get("/recommendations/{destination}")
async def get_recommendation_by_destination(destination: str):
    """Get recommendation for specific destination"""
    for rec in recommendations_db:
        if destination.lower() in rec["destination"].lower():
            return rec
    raise HTTPException(status_code=404, detail="Destination not found")

@app.post("/search")
async def search_destinations(query: TravelQuery):
    """Search for destinations based on travel query"""
    # Simple filtering logic (enhance with AI/ML in production)
    results = []
    
    for rec in recommendations_db:
        if query.destination.lower() in rec["destination"].lower():
            if query.budget is None or rec["estimated_cost"] <= query.budget:
                results.append(rec)
    
    if not results:
        return {"message": "No destinations found matching your criteria", "suggestions": recommendations_db[:2]}
    
    return {"results": results, "count": len(results)}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)