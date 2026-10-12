-- Seed Data for Real-Time Bus Tracking System

-- Stops
INSERT INTO stops (id, name, landmark, latitude, longitude) VALUES
(1, 'Campus Main Gate', 'Security Checkpost & Visitors Center', 12.97160, 77.59456),
(2, 'Administrative Block', 'Near Registrar Office & Finance Center', 12.97320, 77.59710),
(3, 'Central Library', 'Digital Resource Center & Quadrangle', 12.97540, 77.59980),
(4, 'Engineering & Tech Block', 'Computer Science & Electronics Labs', 12.97810, 77.60300),
(5, 'Innovation & Research Park', 'Incubation Wing & Seminar Hall', 12.98150, 77.60650),
(6, 'North Campus Gate', 'North Perimeter & Parking Lot B', 12.98300, 77.60200),
(7, 'Student Hostels Block A-D', 'Opposite Dining Hall 2', 12.98050, 77.59800),
(8, 'University Cafeteria', 'Student Activity Center', 12.97650, 77.59600),
(9, 'Sports Complex & Stadium', 'Gymnasium & Athletic Track', 12.97300, 77.59200),
(10, 'Central Metro Station', 'Exit Gate 2 - Feeder Bay', 12.96500, 77.59000),
(11, 'City Transit Junction', 'Crossroads Bus Bay', 12.96800, 77.59250);

-- Routes
INSERT INTO routes (id, route_number, route_name, origin, destination, total_distance_km, active) VALUES
(1, 'R-101', 'Campus Express', 'Campus Main Gate', 'Innovation & Research Park', 1.85, TRUE),
(2, 'R-102', 'Hostel & Sports Shuttle', 'North Campus Gate', 'Campus Main Gate', 2.10, TRUE),
(3, 'R-103', 'Metro Campus Feeder', 'Central Metro Station', 'Central Library', 2.45, TRUE);

-- Route Stops (Mapping)
-- Route 101 Stops
INSERT INTO route_stops (id, route_id, stop_id, stop_order, distance_from_start_km) VALUES
(1, 1, 1, 1, 0.00),
(2, 1, 2, 2, 0.35),
(3, 1, 3, 3, 0.75),
(4, 1, 4, 4, 1.25),
(5, 1, 5, 5, 1.85);

-- Route 102 Stops
INSERT INTO route_stops (id, route_id, stop_id, stop_order, distance_from_start_km) VALUES
(6, 2, 6, 1, 0.00),
(7, 2, 7, 2, 0.50),
(8, 2, 8, 3, 1.05),
(9, 2, 9, 4, 1.60),
(10, 2, 1, 5, 2.10);

-- Route 103 Stops
INSERT INTO route_stops (id, route_id, stop_id, stop_order, distance_from_start_km) VALUES
(11, 3, 10, 1, 0.00),
(12, 3, 11, 2, 0.45),
(13, 3, 1, 3, 1.15),
(14, 3, 3, 4, 2.45);

-- Buses
INSERT INTO buses (id, bus_number, reg_number, capacity, driver_name, driver_phone, status, current_latitude, current_longitude, speed_kmph, heading, assigned_route_id, current_stop_id, next_stop_id, next_stop_name, distance_to_next_stop_km, eta_next_stop_minutes, last_updated) VALUES
(1, 'BUS-01', 'KA-04-E-1001', 45, 'Ramesh Kumar', '+91 98765 43210', 'IN_TRANSIT', 12.97160, 77.59456, 25.0, 45.0, 1, 1, 2, 'Administrative Block', 0.35, 1, NOW()),
(2, 'BUS-02', 'KA-04-E-1002', 40, 'Suresh Reddy', '+91 98765 43211', 'IN_TRANSIT', 12.98300, 77.60200, 22.0, 180.0, 2, 6, 7, 'Student Hostels Block A-D', 0.50, 2, NOW()),
(3, 'BUS-03', 'KA-04-E-1003', 50, 'Mahesh Gowda', '+91 98765 43212', 'IN_TRANSIT', 12.96500, 77.59000, 28.0, 30.0, 3, 10, 11, 'City Transit Junction', 0.45, 1, NOW()),
(4, 'BUS-04', 'KA-04-E-1004', 35, 'Anand Verma', '+91 98765 43213', 'IDLE', 12.97160, 77.59456, 0.0, 0.0, NULL, 1, NULL, NULL, 0.0, 0, NOW());
