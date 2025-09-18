-- Initialize database with sample data
CREATE TABLE IF NOT EXISTS destinations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255),
    description TEXT,
    estimated_cost DECIMAL(10,2),
    recommended_duration VARCHAR(50),
    highlights JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS travel_plans (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255),
    destination_id INTEGER REFERENCES destinations(id),
    start_date DATE,
    end_date DATE,
    budget DECIMAL(10,2),
    preferences JSONB,
    status VARCHAR(50) DEFAULT 'draft',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample destinations
INSERT INTO destinations (name, country, description, estimated_cost, recommended_duration, highlights) VALUES
('Paris, France', 'France', 'The City of Light offers romantic charm, world-class museums, and exceptional cuisine.', 2500.00, '5-7 days', '["Eiffel Tower", "Louvre Museum", "Notre-Dame Cathedral", "Champs-Élysées"]'),
('Tokyo, Japan', 'Japan', 'A fascinating blend of traditional culture and cutting-edge technology.', 3000.00, '7-10 days', '["Shibuya Crossing", "Senso-ji Temple", "Mount Fuji", "Tsukiji Market"]'),
('New York City, USA', 'United States', 'The city that never sleeps, filled with iconic landmarks and diverse neighborhoods.', 2800.00, '4-6 days', '["Statue of Liberty", "Central Park", "Times Square", "Broadway Shows"]'),
('Barcelona, Spain', 'Spain', 'A vibrant city known for its unique architecture, beautiful beaches, and rich culture.', 2000.00, '4-6 days', '["Sagrada Familia", "Park Güell", "Gothic Quarter", "La Rambla"]')
ON CONFLICT DO NOTHING;