# TravelAssistant

AI-powered travel assistant with FastAPI backend and Next.js frontend.

## Architecture

This project consists of three main components:

- **Backend**: FastAPI application with PostgreSQL database
- **Frontend**: Next.js application with TypeScript
- **Database**: PostgreSQL with sample travel data

## Quick Start with Docker Compose

### Prerequisites

- Docker and Docker Compose installed
- Git

### Development Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd TravelAssistant
```

2. Start all services in development mode:
```bash
docker-compose -f docker-compose.dev.yml up --build
```

3. Access the application:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8000
- API Documentation: http://localhost:8000/docs
- Database: localhost:5432

### Production Setup

1. Start all services in production mode:
```bash
docker-compose up --build -d
```

2. The application will be available at:
- Frontend: http://localhost:3000
- Backend: http://localhost:8000

## Development

### Backend (FastAPI)

The backend is located in the `backend/` directory and includes:

- FastAPI application with CORS support
- PostgreSQL database integration with SQLAlchemy
- Travel recommendations API endpoints
- Environment-based configuration

#### API Endpoints

- `GET /` - Root endpoint
- `GET /health` - Health check
- `GET /recommendations` - Get all travel recommendations
- `GET /recommendations/{destination}` - Get recommendation by destination
- `POST /search` - Search destinations based on criteria

#### Local Development

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```

### Frontend (Next.js)

The frontend is located in the `frontend/` directory and includes:

- Next.js 14 with App Router
- TypeScript configuration
- Styled Components for styling
- API integration with the backend
- Responsive design

#### Local Development

```bash
cd frontend
npm install
npm run dev
```

## Database

PostgreSQL database with pre-populated sample data:

- Destinations table with travel information
- Travel plans table for user itineraries
- Sample destinations: Paris, Tokyo, New York, Barcelona

## Environment Variables

### Backend (.env)
```
DATABASE_URL=postgresql://travel_user:travel_pass@db:5432/travel_assistant
API_HOST=0.0.0.0
API_PORT=8000
DEBUG=true
SECRET_KEY=your-secret-key-here
```

### Frontend (.env.local)
```
NEXT_PUBLIC_API_URL=http://localhost:8000
NODE_ENV=development
```

## Docker Services

- **db**: PostgreSQL 15 database
- **backend**: FastAPI application with uvicorn
- **frontend**: Next.js application

## Features

- 🌍 Travel destination search and recommendations
- 💰 Budget-based filtering
- 📅 Date-based planning
- 🎯 Personalized preferences
- 📱 Responsive web interface
- 🐳 Full Docker containerization
- 🔄 Hot reload in development mode

## API Documentation

When the backend is running, visit http://localhost:8000/docs for interactive API documentation powered by FastAPI's automatic OpenAPI generation.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test with Docker Compose
5. Submit a pull request

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 3000, 8000, and 5432 are available
2. **Docker build issues**: Try `docker-compose down -v` and rebuild
3. **Database connection issues**: Wait for the database health check to pass

### Logs

View logs for specific services:
```bash
docker-compose logs backend
docker-compose logs frontend
docker-compose logs db
```

## License

MIT License
