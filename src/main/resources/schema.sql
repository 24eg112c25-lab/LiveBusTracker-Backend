-- Real-Time Bus Tracking System Schema
CREATE TABLE IF NOT EXISTS stops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    landmark VARCHAR(255),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_number VARCHAR(50) NOT NULL UNIQUE,
    route_name VARCHAR(150) NOT NULL,
    origin VARCHAR(150) NOT NULL,
    destination VARCHAR(150) NOT NULL,
    total_distance_km DOUBLE DEFAULT 0.0,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS route_stops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id BIGINT NOT NULL,
    stop_id BIGINT NOT NULL,
    stop_order INT NOT NULL,
    distance_from_start_km DOUBLE DEFAULT 0.0,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (stop_id) REFERENCES stops(id) ON DELETE CASCADE,
    CONSTRAINT uq_route_stop_order UNIQUE (route_id, stop_order)
);

CREATE TABLE IF NOT EXISTS buses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bus_number VARCHAR(50) NOT NULL UNIQUE,
    reg_number VARCHAR(50) NOT NULL,
    capacity INT DEFAULT 40,
    driver_name VARCHAR(100),
    driver_phone VARCHAR(20),
    status VARCHAR(30) DEFAULT 'IDLE',
    current_latitude DOUBLE,
    current_longitude DOUBLE,
    speed_kmph DOUBLE DEFAULT 0.0,
    heading DOUBLE DEFAULT 0.0,
    assigned_route_id BIGINT,
    current_stop_id BIGINT,
    next_stop_id BIGINT,
    next_stop_name VARCHAR(150),
    distance_to_next_stop_km DOUBLE DEFAULT 0.0,
    eta_next_stop_minutes INT DEFAULT 0,
    last_updated DATETIME,
    FOREIGN KEY (assigned_route_id) REFERENCES routes(id) ON DELETE SET NULL,
    FOREIGN KEY (current_stop_id) REFERENCES stops(id) ON DELETE SET NULL,
    FOREIGN KEY (next_stop_id) REFERENCES stops(id) ON DELETE SET NULL
);
