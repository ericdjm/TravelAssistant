-- ============================================================================
-- POIs Table Schema Extension
-- CPS731 Travel Assistant - Phase 3
-- ============================================================================

USE travel_assistant_db;

-- Drop table if exists (for fresh start)
DROP TABLE IF EXISTS pois;

-- Create POIs table
CREATE TABLE pois (
    poi_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,  -- restaurant, museum, park, cafe, bar, shopping, entertainment
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    rating FLOAT DEFAULT 0.0,
    price_level VARCHAR(10),  -- $, $$, $$$, $$$$
    tags JSON,  -- e.g., ["italian", "pasta", "dinner"]
    open_now BOOLEAN DEFAULT TRUE,
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- Sample POI Data - Toronto Area (40+ diverse POIs)
-- ============================================================================

-- RESTAURANTS (15 diverse options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_r001', 'Pasta Palace', 'restaurant', 43.6529, -79.3849, 4.5, '$$', '["italian", "pasta", "dinner"]', true, '123 Queen St W, Toronto'),
('poi_r002', 'Sushi Haven', 'restaurant', 43.6545, -79.3805, 4.7, '$$$', '["japanese", "sushi", "lunch", "dinner"]', true, '456 King St E, Toronto'),
('poi_r003', 'Burger Barn', 'restaurant', 43.6510, -79.3890, 4.2, '$', '["american", "burgers", "casual"]', true, '789 Spadina Ave, Toronto'),
('poi_r004', 'French Bistro', 'restaurant', 43.6570, -79.3780, 4.6, '$$$$', '["french", "fine-dining", "romantic"]', false, '321 Yonge St, Toronto'),
('poi_r005', 'Taco Fiesta', 'restaurant', 43.6500, -79.3920, 4.4, '$', '["mexican", "tacos", "casual"]', true, '555 College St, Toronto'),
('poi_r006', 'Dragon Wok', 'restaurant', 43.6540, -79.3870, 4.3, '$$', '["chinese", "noodles", "lunch"]', true, '777 Dundas St W, Toronto'),
('poi_r007', 'The Keg Steakhouse', 'restaurant', 43.6485, -79.3810, 4.6, '$$$', '["steakhouse", "fine-dining", "dinner"]', true, '165 York St, Toronto'),
('poi_r008', 'Pizzeria Napoli', 'restaurant', 43.6520, -79.3950, 4.5, '$$', '["italian", "pizza", "casual"]', true, '450 College St, Toronto'),
('poi_r009', 'Thai Basil', 'restaurant', 43.6555, -79.3800, 4.4, '$$', '["thai", "curry", "dinner"]', true, '88 Adelaide St E, Toronto'),
('poi_r010', 'Poke Bowl Paradise', 'restaurant', 43.6505, -79.3875, 4.3, '$', '["hawaiian", "poke", "healthy"]', true, '234 Bathurst St, Toronto'),
('poi_r011', 'Mediterranean Delight', 'restaurant', 43.6530, -79.3820, 4.5, '$$', '["mediterranean", "healthy", "lunch"]', true, '99 Front St E, Toronto'),
('poi_r012', 'BBQ Smokehouse', 'restaurant', 43.6495, -79.3900, 4.4, '$$', '["bbq", "american", "casual"]', true, '333 Queen St W, Toronto'),
('poi_r013', 'Veggie Delight', 'restaurant', 43.6515, -79.3855, 4.2, '$', '["vegetarian", "healthy", "casual"]', true, '222 King St W, Toronto'),
('poi_r014', 'Indian Spice Kitchen', 'restaurant', 43.6525, -79.3900, 4.6, '$$', '["indian", "curry", "dinner"]', true, '444 Spadina Ave, Toronto'),
('poi_r015', 'Korean BBQ House', 'restaurant', 43.6510, -79.3830, 4.5, '$$$', '["korean", "bbq", "dinner"]', true, '567 Bloor St W, Toronto');

-- MUSEUMS (6 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_m001', 'Royal Ontario Museum', 'museum', 43.6677, -79.3948, 4.8, '$$', '["history", "culture", "educational"]', true, '100 Queen''s Park, Toronto'),
('poi_m002', 'Art Gallery of Ontario', 'museum', 43.6536, -79.3925, 4.7, '$$', '["art", "culture", "educational"]', true, '317 Dundas St W, Toronto'),
('poi_m003', 'Bata Shoe Museum', 'museum', 43.6670, -79.4000, 4.4, '$', '["fashion", "history", "unique"]', true, '327 Bloor St W, Toronto'),
('poi_m004', 'Casa Loma', 'museum', 43.6780, -79.4094, 4.6, '$$$', '["castle", "history", "architecture"]', true, '1 Austin Terrace, Toronto'),
('poi_m005', 'Ontario Science Centre', 'museum', 43.7167, -79.3389, 4.5, '$$', '["science", "educational", "family"]', true, '770 Don Mills Rd, Toronto'),
('poi_m006', 'Hockey Hall of Fame', 'museum', 43.6470, -79.3774, 4.6, '$$', '["sports", "hockey", "canadian"]', true, '30 Yonge St, Toronto');

-- PARKS (8 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_p001', 'High Park', 'park', 43.6465, -79.4637, 4.9, '$', '["outdoor", "nature", "hiking"]', true, '1873 Bloor St W, Toronto'),
('poi_p002', 'Trinity Bellwoods Park', 'park', 43.6476, -79.4190, 4.6, '$', '["outdoor", "picnic", "casual"]', true, '790 Queen St W, Toronto'),
('poi_p003', 'Toronto Islands', 'park', 43.6205, -79.3790, 4.8, '$', '["outdoor", "beach", "scenic"]', true, 'Toronto Islands, Toronto'),
('poi_p004', 'Allan Gardens', 'park', 43.6626, -79.3755, 4.5, '$', '["gardens", "greenhouse", "peaceful"]', true, '19 Horticultural Ave, Toronto'),
('poi_p005', 'Riverdale Park', 'park', 43.6664, -79.3588, 4.7, '$', '["outdoor", "scenic", "views"]', true, '550 Broadview Ave, Toronto'),
('poi_p006', 'Kew Gardens', 'park', 43.6690, -79.2980, 4.6, '$', '["outdoor", "beach", "family"]', true, '2075 Queen St E, Toronto'),
('poi_p007', 'Evergreen Brick Works', 'park', 43.6850, -79.3650, 4.7, '$', '["nature", "educational", "markets"]', true, '550 Bayview Ave, Toronto'),
('poi_p008', 'Toronto Music Garden', 'park', 43.6360, -79.4030, 4.5, '$', '["gardens", "peaceful", "waterfront"]', true, '479 Queens Quay W, Toronto');

-- CAFES (6 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_c001', 'Coffee Culture', 'cafe', 43.6520, -79.3810, 4.3, '$', '["coffee", "breakfast", "wifi"]', true, '234 Adelaide St E, Toronto'),
('poi_c002', 'Bean Scene', 'cafe', 43.6555, -79.3870, 4.4, '$', '["coffee", "pastries", "cozy"]', true, '567 College St, Toronto'),
('poi_c003', 'Espresso House', 'cafe', 43.6490, -79.3845, 4.5, '$$', '["coffee", "breakfast", "trendy"]', true, '123 King St W, Toronto'),
('poi_c004', 'The Roastery', 'cafe', 43.6540, -79.3900, 4.6, '$$', '["coffee", "artisan", "specialty"]', true, '789 Queen St W, Toronto'),
('poi_c005', 'Sweet Tooth Bakery', 'cafe', 43.6515, -79.3825, 4.4, '$', '["bakery", "pastries", "breakfast"]', true, '345 Front St W, Toronto'),
('poi_c006', 'Tea Time Lounge', 'cafe', 43.6525, -79.3885, 4.3, '$$', '["tea", "pastries", "relaxing"]', true, '456 Yonge St, Toronto');

-- SHOPPING (3 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_s001', 'CF Toronto Eaton Centre', 'shopping', 43.6544, -79.3807, 4.5, '$$', '["mall", "shopping", "indoor"]', true, '220 Yonge St, Toronto'),
('poi_s002', 'St Lawrence Market', 'shopping', 43.6487, -79.3716, 4.7, '$', '["market", "food", "local"]', true, '93 Front St E, Toronto'),
('poi_s003', 'Kensington Market', 'shopping', 43.6542, -79.4008, 4.6, '$', '["market", "vintage", "eclectic"]', true, 'Kensington Ave, Toronto');

-- ENTERTAINMENT (5 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_e001', 'CN Tower', 'entertainment', 43.6426, -79.3871, 4.8, '$$$', '["landmark", "scenic", "tourist"]', true, '290 Bremner Blvd, Toronto'),
('poi_e002', 'Ripley''s Aquarium', 'entertainment', 43.6424, -79.3860, 4.7, '$$', '["aquarium", "educational", "family"]', true, '288 Bremner Blvd, Toronto'),
('poi_e003', 'Scotiabank Arena', 'entertainment', 43.6435, -79.3791, 4.6, '$$$', '["sports", "concerts", "events"]', true, '40 Bay St, Toronto'),
('poi_e004', 'Princess of Wales Theatre', 'entertainment', 43.6485, -79.3895, 4.5, '$$$', '["theatre", "shows", "culture"]', true, '300 King St W, Toronto'),
('poi_e005', 'Second City Toronto', 'entertainment', 43.6475, -79.3925, 4.6, '$$', '["comedy", "shows", "nightlife"]', true, '51 Mercer St, Toronto');

-- BARS (4 options)
INSERT INTO pois (poi_id, name, category, latitude, longitude, rating, price_level, tags, open_now, address) VALUES
('poi_b001', 'Sky Lounge', 'bar', 43.6490, -79.3820, 4.4, '$$$', '["rooftop", "nightlife", "scenic"]', false, '88 King St W, Toronto'),
('poi_b002', 'Steam Whistle Brewing', 'bar', 43.6395, -79.3860, 4.5, '$$', '["brewery", "beer", "tours"]', true, '255 Bremner Blvd, Toronto'),
('poi_b003', 'The Shamrock Pub', 'bar', 43.6505, -79.3865, 4.3, '$', '["pub", "casual", "sports"]', true, '120 Eglinton Ave E, Toronto'),
('poi_b004', 'Cocktail Lounge 360', 'bar', 43.6515, -79.3795, 4.5, '$$$', '["cocktails", "upscale", "nightlife"]', true, '301 Front St W, Toronto');

-- Verify insertion
SELECT category, COUNT(*) as count FROM pois GROUP BY category ORDER BY category;
SELECT 'Total POIs:', COUNT(*) FROM pois;
