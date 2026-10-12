# Real-Time Bus Tracking System

**College**: Anurag University, Venkatapur, Ghatkesar, Hyderabad, Telangana 500088  
A complete full-stack College Capstone Project built with **Java 21**, **Spring Boot 3.3.4**, **Spring Data JPA**, **MySQL Connector/J**, **Jakarta Bean Validation**, and **Leaflet with OpenStreetMap**.

---

## 📌 Project Overview

The **Real-Time Bus Tracking System** is a transit monitoring solution designed for **Anurag University, Hyderabad**. It allows students and passengers to track campus shuttle buses in real time on an interactive Leaflet OpenStreetMap centered at Anurag University (`17.41931`, `78.65654`), view live bus headings, monitor remaining distance and estimated arrival times (ETAs) to upcoming stops along the corridor towards Hyderabad, and search for transit stops and destinations (including **Charminar**, **Hussain Sagar Lake**, and **Secunderabad Railway Station**). 

Administrators are equipped with complete CRUD controls over the vehicle fleet, transit routes, and campus bus stops (including an **interactive map coordinate picker**). The platform includes a built-in **Hardware-Free GPS Movement Simulator** that realistically moves buses along designated route waypoints using spherical trigonometry and linear interpolation without needing expensive physical GPS hardware.

The frontend is housed directly inside `src/main/resources/static/`, enabling the entire system to run seamlessly as a **single, unified Spring Boot application** compiled into a standalone executable JAR.

---

## 🛠️ Technology Stack

| Layer | Technologies & Frameworks | Description |
|---|---|---|
| **Language & Platform** | Java 21 LTS | Compiled with `-parameters` and targeting Java 21 release bytecode |
| **Backend Framework** | Spring Boot 3.3.4 | Spring Web (REST API), Spring Data JPA, Spring Validation |
| **Database** | MySQL Server 8.0 (`bus_tracking_db`) | Relational persistence with HikariCP connection pooling |
| **ORM / Persistence** | Hibernate 6.5.3 (JPA 3.1) | Object-relational mapping, automatic schema maintenance (`ddl-auto=update`) |
| **Data Validation** | Jakarta Bean Validation | `@Valid`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@DecimalMin`, `@DecimalMax` |
| **Geospatial & Mapping** | Leaflet.js v1.9.4 & OpenStreetMap | Interactive tile rendering, custom directional SVG markers, route polylines |
| **Frontend UI** | HTML5, CSS3, Vanilla ES6 JavaScript | Clean responsive dashboard, dark-accent headers, glassmorphic status cards |
| **Simulation Engine** | Spring `@Scheduled` + GeoUtils | Math-based GPS interpolation, bearing calculations, dwell timers |
| **Testing** | JUnit 5, MockMvc, H2 Database | 14 automated unit, integration, and validation tests |
| **Build Tool** | Apache Maven 3.8+ | Unified build producing a standalone executable JAR |

---

## 📂 Project Directory Structure

```
BusTracking/
├── pom.xml                                      # Maven configuration (Java 21, Spring Boot 3.3.4, JPA, MySQL)
├── README.md                                    # Project documentation, setup guide & viva demonstration manual
├── .gitignore                                   # Standard git ignore definitions
└── src/
    ├── main/
    │   ├── java/com/bustracking/
    │   │   ├── BusTrackingApplication.java      # Application entry point (@SpringBootApplication)
    │   │   ├── config/
    │   │   │   ├── GlobalExceptionHandler.java  # Centralized REST validation & error responses
    │   │   │   ├── SchedulingConfig.java        # Enables @EnableScheduling for GPS simulator
    │   │   │   └── WebConfig.java               # CORS configuration & static resource routing
    │   │   ├── controller/
    │   │   │   ├── BusController.java           # Fleet management & live telemetry REST APIs
    │   │   │   ├── RouteController.java         # Route management and origin-destination search APIs
    │   │   │   ├── StopController.java          # Bus stop management APIs
    │   │   │   └── SimulationController.java    # Simulator speed, pause/resume, and teleport APIs
    │   │   ├── dto/
    │   │   │   ├── BusDto.java                  # Validated Bus request/response DTO
    │   │   │   ├── BusLocationDto.java          # Telemetry DTO (latitude, longitude, speed, heading)
    │   │   │   ├── RouteDto.java                # Validated Route request/response DTO
    │   │   │   ├── RouteStopDto.java            # Route stop mapping DTO
    │   │   │   ├── StopDto.java                 # Validated Stop request/response DTO
    │   │   │   ├── EtaInfoDto.java              # Live stop ETA response DTO
    │   │   │   └── SimulationStatusDto.java     # Simulator status & tick telemetry DTO
    │   │   ├── model/
    │   │   │   ├── Bus.java                     # Bus JPA entity
    │   │   │   ├── BusLocation.java             # Telemetry entity (coordinates, speed, heading, ETA)
    │   │   │   ├── BusStatus.java               # Enum: IN_TRANSIT, STOPPED_AT_STOP, IDLE, OUT_OF_SERVICE
    │   │   │   ├── Route.java                   # Route JPA entity
    │   │   │   ├── RouteStop.java               # Route-Stop join entity with sequenceOrder
    │   │   │   └── Stop.java                    # Bus stop JPA entity
    │   │   ├── repository/
    │   │   │   ├── BusRepository.java           # Bus JPA repository with custom queries
    │   │   │   ├── BusLocationRepository.java   # Location JPA repository
    │   │   │   ├── RouteRepository.java         # Route JPA repository
    │   │   │   ├── RouteStopRepository.java     # Ordered route stop repository
    │   │   │   └── StopRepository.java          # Stop JPA repository
    │   │   ├── service/
    │   │   │   ├── BusService.java              # Fleet business logic & location association
    │   │   │   ├── RouteService.java            # Route & stop sequence management
    │   │   │   ├── StopService.java             # Stop CRUD logic
    │   │   │   ├── GeoUtils.java                # Haversine distance, bearing & interpolation math
    │   │   │   └── DataSeeder.java              # Preloads 11 stops, 3 routes, and 4 buses
    │   │   └── simulator/
    │   │       └── BusMovementSimulator.java    # Background scheduled GPS simulator engine
    │   └── resources/
    │       ├── application.properties           # Spring Boot configuration with env var defaults
    │       ├── schema.sql                       # Database schema DDL
    │       ├── data.sql                         # Database seed records
    │       └── static/                          # Unified single-project frontend
    │           ├── index.html                   # Overview landing page & live system metrics
    │           ├── bus-tracking.html            # Passenger live Leaflet OpenStreetMap portal
    │           ├── admin.html                   # Admin fleet, route, stop & simulation control center
    │           ├── css/style.css                # Modern responsive styling & design tokens
    │           └── js/
    │               ├── api.js                   # Unified fetch API client
    │               ├── tracking.js              # Real-time passenger map & polling engine
    │               ├── admin.js                 # Admin CRUD & Leaflet map coordinate picker
    │               └── simulator.js             # Simulation status monitor & teleportation controls
    └── test/
        ├── java/com/bustracking/
        │   ├── BusControllerIntegrationTest.java# MockMvc integration tests
        │   ├── GeoUtilsTest.java                # Unit tests for Haversine, bearing & ETA
        │   └── ValidationTest.java              # Unit tests for Jakarta validation constraints
        └── resources/
            └── application-test.properties      # Test configuration using in-memory H2
```

---

## ⚙️ Prerequisites & MySQL Database Configuration

### 1. Prerequisites
- **Java**: Java 21 or higher (JDK 21 LTS or JDK 26 targeting release 21)
- **Maven**: Apache Maven 3.8.0 or higher
- **MySQL**: MySQL Server 8.0 or higher (running on port `3306`)

### 2. Database Setup
Log into your MySQL command-line or MySQL Workbench:
```sql
CREATE DATABASE IF NOT EXISTS bus_tracking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Application Credentials Configuration
The database connection is configured in `src/main/resources/application.properties`:
```properties
server.port=8080
spring.application.name=RealTimeBusTracking

# MySQL Database Configuration with Environment Variable support and local defaults
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/bus_tracking_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# GPS Simulation Configuration
bus.simulation.enabled=true
bus.simulation.interval-ms=2000
bus.simulation.default-speed-multiplier=2.0
```

#### Setting Environment Variables (Optional)
If your MySQL root password is not `root`, you can set environment variables before running:

##### Windows PowerShell:
```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your_mysql_password"
```

##### Windows Command Prompt:
```cmd
set DB_USERNAME=root
set DB_PASSWORD=your_mysql_password
```

##### Linux / macOS:
```bash
export DB_USERNAME="root"
export DB_PASSWORD="your_mysql_password"
```

---

## 🏃 How to Build and Run

### 1. Run Automated Test Suite
```powershell
mvn test
```
*Executes all 14 unit and integration tests (MockMvc tests, Bean Validation constraint checks, and GeoUtils math tests) using isolated in-memory H2 database.*

### 2. Package into Executable JAR
```powershell
mvn clean package
```
*Builds a self-contained executable JAR located at `target/real-time-bus-tracking-1.0.0.jar`.*

### 3. Run the Application

#### Option A: Running with Maven
```powershell
mvn spring-boot:run
```

#### Option B: Running the Packaged JAR Directly
```powershell
java -jar target/real-time-bus-tracking-1.0.0.jar
```

Once the application starts, open your web browser and navigate to:
- **Home / Overview**: [http://localhost:8080/index.html](http://localhost:8080/index.html)
- **Live Passenger Tracking**: [http://localhost:8080/bus-tracking.html](http://localhost:8080/bus-tracking.html)
- **Admin Management & Simulator**: [http://localhost:8080/admin.html](http://localhost:8080/admin.html)

---

## 📡 Complete REST API Reference

### 1. Fleet & Bus Telemetry (`/api/buses`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/buses` | Retrieve all registered buses with embedded location data |
| `GET` | `/api/buses/active` | Retrieve only currently active buses (`IN_TRANSIT` or `STOPPED_AT_STOP`) |
| `GET` | `/api/buses/locations` | Retrieve current simulated coordinates, heading, and speed for all buses |
| `GET` | `/api/buses/{id}` | Get bus details by ID |
| `GET` | `/api/buses/{id}/location` | Get current simulated location for a specific bus |
| `GET` | `/api/buses/{id}/eta` | Calculate real-time ETAs to all stops along the bus's route |
| `POST` | `/api/buses` | Register a new bus (`@Valid` Bean Validation enforced) |
| `PUT` | `/api/buses/{id}` | Update bus details (`@Valid` Bean Validation enforced) |
| `DELETE` | `/api/buses/{id}` | Delete a bus record |

#### Sample Response: `GET /api/buses/1/eta`
```json
[
  {
    "stopId": 1,
    "stopName": "Campus Main Gate",
    "landmark": "Security Checkpost & Visitors Center",
    "stopOrder": 1,
    "distanceKm": 0.0,
    "etaMinutes": 0,
    "status": "PASSED"
  },
  {
    "stopId": 2,
    "stopName": "Administrative Block",
    "landmark": "Near Registrar Office",
    "stopOrder": 2,
    "distanceKm": 0.35,
    "etaMinutes": 1,
    "status": "NEXT_STOP"
  },
  {
    "stopId": 3,
    "stopName": "Central Library",
    "landmark": "Digital Resource Center",
    "stopOrder": 3,
    "distanceKm": 0.82,
    "etaMinutes": 2,
    "status": "UPCOMING"
  }
]
```

### 2. Transit Routes (`/api/routes`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/routes` | List all transit routes |
| `GET` | `/api/routes/{id}` | Get route details with ordered stop sequence |
| `POST` | `/api/routes` | Create a new route (`@Valid` Bean Validation enforced) |
| `PUT` | `/api/routes/{id}` | Update route details (`@Valid` Bean Validation enforced) |
| `DELETE` | `/api/routes/{id}` | Delete a route |
| `POST` | `/api/routes/{id}/stops` | Add a stop to a route with sequence order |
| `DELETE` | `/api/routes/{id}/stops/{stopId}` | Remove a stop from a route |
| `GET` | `/api/routes/search?originStopId={id}&destStopId={id}` | Find routes connecting two campus stops |

### 3. Bus Stops (`/api/stops`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/stops` | List all campus bus stops |
| `GET` | `/api/stops/{id}` | Get stop details by ID |
| `POST` | `/api/stops` | Create a new bus stop (`@Valid` Bean Validation enforced) |
| `PUT` | `/api/stops/{id}` | Update bus stop (`@Valid` Bean Validation enforced) |
| `DELETE` | `/api/stops/{id}` | Delete bus stop |

### 4. GPS Movement Simulator (`/api/simulator`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/simulator/status` | Get simulator state (running, tick count, speed multiplier) |
| `POST` | `/api/simulator/start` | Start or resume the GPS movement simulation |
| `POST` | `/api/simulator/pause` | Pause the GPS movement simulation |
| `POST` | `/api/simulator/reset` | Reset all buses back to their route starting stop |
| `POST` | `/api/simulator/speed?multiplier={val}` | Set speed multiplier (`0.5` to `10.0`) |
| `POST` | `/api/simulator/teleport?busId={id}&stopId={id}` | Instantly teleport a bus to a stop (ideal for viva demonstrations) |

---

## 📐 Mathematical Foundations

### 1. Haversine Distance Formula
Used in `GeoUtils.java` to compute true great-circle distance between two GPS coordinates on Earth:
$$\Delta\phi = \text{lat}_2 - \text{lat}_1, \quad \Delta\lambda = \text{lon}_2 - \text{lon}_1$$
$$a = \sin^2\left(\frac{\Delta\phi}{2}\right) + \cos(\phi_1)\cos(\phi_2)\sin^2\left(\frac{\Delta\lambda}{2}\right)$$
$$c = 2 \cdot \text{atan2}\left(\sqrt{a}, \sqrt{1 - a}\right), \quad d = R \cdot c \quad (R = 6371\text{ km})$$

### 2. Spherical Trigonometry Bearing (Heading Angle)
Used to orient the bus marker icon in Leaflet so it points in the direction of travel:
$$\theta = \text{atan2}\left(\sin(\Delta\lambda)\cos(\phi_2), \cos(\phi_1)\sin(\phi_2) - \sin(\phi_1)\cos(\phi_2)\cos(\Delta\lambda)\right)$$
$$\text{Heading} = (\theta \cdot \frac{180}{\pi} + 360) \pmod{360}$$

### 3. Linear Waypoint Interpolation
Calculates intermediate positions between consecutive route stops:
$$\text{lat}(t) = \text{lat}_1 + t \cdot (\text{lat}_2 - \text{lat}_1), \quad \text{lng}(t) = \text{lng}_1 + t \cdot (\text{lng}_2 - \text{lng}_1) \quad (0.0 \le t \le 1.0)$$

---

## 🎓 Step-by-Step Viva / Evaluation Demonstration Guide

When presenting this project to professors or external evaluators, follow this recommended demonstration sequence:

### Step 1: System Overview & Telemetry Metrics
1. Open [http://localhost:8080/index.html](http://localhost:8080/index.html).
2. Point out the live telemetry cards:
   - **Total Fleet Buses**: 4 registered buses.
   - **Configured Routes**: 3 active campus transit routes.
   - **Campus Bus Stops**: 11 geocoded stops across campus.
   - **Simulated Active Buses**: 3 buses actively in transit.
3. Explain that the application is running as a single, self-contained Spring Boot web server with no external frontend dependencies.

### Step 2: Live Passenger Tracking & Leaflet OpenStreetMap
1. Click **"Launch Live Passenger Map"** or go to [http://localhost:8080/bus-tracking.html](http://localhost:8080/bus-tracking.html).
2. **Show Dynamic Bus Movement**:
   - Point out the bus markers moving along the colored polyline route paths.
   - Show how the bus marker orientation arrow dynamically rotates to match the calculated bearing angle.
   - Note the real-time polling indicator in the navbar updating every 2 seconds without full-page reloads.
3. **Select a Bus**:
   - In the left sidebar, choose **BUS-01 (Campus Express)**.
   - The map centers and highlights BUS-01.
   - Click **"🎯 Follow Bus"** to demonstrate automatic camera tracking as the bus advances.
4. **Demonstrate Stop Progress & Real-Time ETAs**:
   - Show the dynamic timeline card:
     - Passed stops marked with a green checkmark `PASSED`.
     - Current/next stop highlighted with distance in km and ETA in minutes.
     - Upcoming stops displaying cumulative distance and estimated arrival time.
5. **Transit Route Finder**:
   - In the sidebar, select **Origin Stop**: "Campus Main Gate" and **Destination Stop**: "Innovation & Research Park".
   - Click **"Search Routes"** to display matching direct routes with stop count and total distance.

### Step 3: Admin Management & Interactive Map Coordinate Picker
1. Click **"Admin Portal"** or go to [http://localhost:8080/admin.html](http://localhost:8080/admin.html).
2. **Fleet Management Tab**:
   - Demonstrate bus registration: Click **"+ Register New Bus"**.
   - Show Jakarta Bean Validation: Try submitting empty fields to demonstrate client-side and server-side validation error handling.
   - Add a test bus (e.g. `BUS-10`, `KA-04-E-1010`, capacity `45`) and assign it to a route.
3. **Transit Routes Tab**:
   - Click **"Manage Stops"** on route **R-101**.
   - Show how administrators can add or remove stops and view total cumulative route distance recalculating automatically.
4. **Bus Stops Tab & Interactive Map Coordinate Picker**:
   - Click **"+ Add New Stop"**.
   - Point to the embedded Leaflet map in the modal.
   - **Click anywhere on the campus map**: Show that the latitude and longitude fields instantly populate with high precision.
   - Drag the marker pin to refine the coordinates.
   - Save the stop to show real-time persistence into MySQL.

### Step 4: Hardware-Free GPS Simulator Demonstration
1. In the Admin portal, switch to the **"⚡ GPS Simulator"** tab.
2. **Speed Multiplier Slider**:
   - Drag the slider from `2.0x` to `5.0x` or `10.0x`.
   - Switch back to the Passenger Tracking tab to show the bus noticeably accelerating across the campus map.
3. **Stop Boarding Dwell Time**:
   - Explain the realistic dwell feature: When a bus arrives at a stop, it automatically pauses for ~6 seconds (`STOPPED_AT_STOP`), drops its speed to `0 km/h`, and then resumes transit (`IN_TRANSIT`).
4. **Demo Teleportation Tool (The Viva Savior Feature)**:
   - Evaluators often ask: *"How do we know the arrival triggers and ETA recalculations work without waiting for the bus to travel the whole route?"*
   - Use the **Demo Teleportation Tool**:
     - Select **BUS-01** and choose **Target Stop: Central Library**.
     - Click **"🚀 Teleport to Stop"**.
     - Instantly switch to the Passenger Live Map to demonstrate that BUS-01 is now docked at the Central Library, its status is `STOPPED_AT_STOP`, its ETA has refreshed, and upcoming stop distances have updated.

---

## 🔒 Security Architecture: Academic vs. Production

### Current Academic & Viva Environment
- **Public & Unauthenticated**: Endpoints are currently open to enable multi-client academic evaluation and viva presentations without login expiration or token management hurdles.
- **Data Seeding**: Automatically seeds on initial boot via `DataSeeder.java` if the database is empty.

### Production Hardening Blueprint
For real-world municipal or institutional deployment:
1. Include `spring-boot-starter-security`.
2. Implement JWT (JSON Web Token) / OAuth2 authentication for administrative endpoints (`POST`, `PUT`, `DELETE` on `/api/buses/**`, `/api/routes/**`, `/api/stops/**`, and `/api/simulator/**`).
3. Keep read endpoints (`GET /api/buses/locations`, `GET /api/buses/{id}/eta`, `GET /api/routes/**`) publicly accessible for passenger mobile apps and web clients.

---

## 📄 License & Academic Attribution
This project was developed for academic capstone evaluation and demonstration. All code and assets are open-source and intended for educational transit monitoring research.
