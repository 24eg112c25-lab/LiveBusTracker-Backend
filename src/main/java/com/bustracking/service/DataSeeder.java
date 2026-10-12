package com.bustracking.service;

import com.bustracking.model.*;
import com.bustracking.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final BusRepository busRepository;
    private final BusLocationRepository busLocationRepository;
    private final RouteService routeService;

    public DataSeeder(StopRepository stopRepository,
                      RouteRepository routeRepository,
                      RouteStopRepository routeStopRepository,
                      BusRepository busRepository,
                      BusLocationRepository busLocationRepository,
                      RouteService routeService) {
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.busRepository = busRepository;
        this.busLocationRepository = busLocationRepository;
        this.routeService = routeService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Check if database is already configured with Anurag University data
        boolean hasAnurag = stopRepository.findAll().stream()
                .anyMatch(s -> s.getName().contains("Anurag University"));

        if (hasAnurag) {
            log.info("Database already seeded with Anurag University, Hyderabad transit network.");
            return;
        }

        if (stopRepository.count() > 0) {
            log.info("Old dataset detected. Upgrading database to Anurag University transit network...");
            busLocationRepository.deleteAll();
            busRepository.deleteAll();
            routeStopRepository.deleteAll();
            routeRepository.deleteAll();
            stopRepository.deleteAll();
        }

        log.info("Seeding transit network for Anurag University, Venkatapur, Ghatkesar, Hyderabad...");

        // 1. Create Stops from Anurag University to Hyderabad City
        Stop s1 = stopRepository.save(new Stop(
                "Anurag University (Main Campus Gate)",
                "Venkatapur, Ghatkesar, Hyderabad, Telangana 500088",
                17.41931, 78.65654));

        Stop s2 = stopRepository.save(new Stop(
                "Venkatapur Junction",
                "Near Hyderabad-Warangal Highway NH-163 Corridor",
                17.42550, 78.64800));

        Stop s3 = stopRepository.save(new Stop(
                "Korremula Cross Road",
                "Ghatkesar Mandal Transit Crossroads",
                17.42100, 78.62500));

        Stop s4 = stopRepository.save(new Stop(
                "Narapally Bus Station",
                "Near VBIT Engineering College & Educational Hub",
                17.41250, 78.60100));

        Stop s5 = stopRepository.save(new Stop(
                "Medipally Bus Bay",
                "Peerzadiguda / Medipally Main Road",
                17.40800, 78.58200));

        Stop s6 = stopRepository.save(new Stop(
                "Boduppal Cross Road",
                "Boduppal Commercial Arch & Bus Stop",
                17.40500, 78.56800));

        Stop s7 = stopRepository.save(new Stop(
                "Uppal Ring Road & Metro Station",
                "Hyderabad Metro Red Line Junction / Inner Ring Road",
                17.40180, 78.56020));

        Stop s8 = stopRepository.save(new Stop(
                "Habsiguda Crossroads",
                "Near CCMB, NGRI Research Institutes & Metro Pillar",
                17.41800, 78.54200));

        Stop s9 = stopRepository.save(new Stop(
                "Tarnaka Metro Station",
                "Tarnaka Flyover & Osmania University Administrative Gate",
                17.42880, 78.53030));

        Stop s10 = stopRepository.save(new Stop(
                "Secunderabad Railway Station",
                "Platform 1 / Gurudwara City Bus Terminal",
                17.43390, 78.50160));

        Stop s11 = stopRepository.save(new Stop(
                "Hussain Sagar Lake (Tank Bund)",
                "Buddha Statue Vista & Tank Bund Promenade",
                17.42390, 78.47380));

        Stop s12 = stopRepository.save(new Stop(
                "Charminar (Old City Transit Hub)",
                "Historic Monument, Gulzar Houz & Mecca Masjid Bay",
                17.36160, 78.47470));

        Stop s13 = stopRepository.save(new Stop(
                "Ghatkesar Town Junction",
                "Ghatkesar Railway Station & Warangal Highway Bus Stand",
                17.44850, 78.68300));

        // 2. Create Routes
        // Route 1: Anurag University - Hyderabad City Express (Main corridor to Charminar)
        Route r1 = routeRepository.save(new Route(
                "AU-HYD-01",
                "Anurag University - Hyderabad City Express",
                "Anurag University (Main Campus Gate)",
                "Charminar (Old City Transit Hub)",
                0.0, true));

        // Route 2: Anurag - Secunderabad Metro Shuttle
        Route r2 = routeRepository.save(new Route(
                "AU-SEC-02",
                "Anurag - Secunderabad Metro Shuttle",
                "Anurag University (Main Campus Gate)",
                "Secunderabad Railway Station",
                0.0, true));

        // Route 3: Anurag - Ghatkesar Local Connector
        Route r3 = routeRepository.save(new Route(
                "AU-GHK-03",
                "Anurag - Ghatkesar Local Connector",
                "Anurag University (Main Campus Gate)",
                "Ghatkesar Town Junction",
                0.0, true));

        // 3. Map Route Stops sequentially
        List<Stop> r1Stops = Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12);
        for (int i = 0; i < r1Stops.size(); i++) {
            routeStopRepository.save(new RouteStop(r1, r1Stops.get(i), i + 1, 0.0));
        }
        routeService.recalculateRouteDistances(r1.getId());

        List<Stop> r2Stops = Arrays.asList(s1, s2, s4, s7, s8, s9, s10);
        for (int i = 0; i < r2Stops.size(); i++) {
            routeStopRepository.save(new RouteStop(r2, r2Stops.get(i), i + 1, 0.0));
        }
        routeService.recalculateRouteDistances(r2.getId());

        List<Stop> r3Stops = Arrays.asList(s1, s2, s13);
        for (int i = 0; i < r3Stops.size(); i++) {
            routeStopRepository.save(new RouteStop(r3, r3Stops.get(i), i + 1, 0.0));
        }
        routeService.recalculateRouteDistances(r3.getId());

        // 4. Create Buses with Initial Locations
        // Bus 1: Main City Express
        Bus b1 = new Bus("AU-BUS-01", "TS-08-UB-1001", 50, "K. Srinivas Rao", "+91 98480 12345", r1);
        b1.setStatus(BusStatus.IN_TRANSIT);
        b1.setLastUpdated(LocalDateTime.now());
        BusLocation loc1 = new BusLocation(b1, s1.getLatitude(), s1.getLongitude(), 30.0, 270.0);
        loc1.setCurrentStop(s1);
        loc1.setNextStop(s2);
        loc1.setNextStopName(s2.getName());
        loc1.setDistanceToNextStopKm(1.1);
        loc1.setEtaNextStopMinutes(2);
        b1.setCurrentLocation(loc1);
        busRepository.save(b1);

        // Bus 2: Secunderabad Shuttle
        Bus b2 = new Bus("AU-BUS-02", "TS-08-UB-1002", 45, "M. Venkat Reddy", "+91 98480 12346", r2);
        b2.setStatus(BusStatus.IN_TRANSIT);
        b2.setLastUpdated(LocalDateTime.now());
        BusLocation loc2 = new BusLocation(b2, s7.getLatitude(), s7.getLongitude(), 32.0, 290.0);
        loc2.setCurrentStop(s7);
        loc2.setNextStop(s8);
        loc2.setNextStopName(s8.getName());
        loc2.setDistanceToNextStopKm(2.4);
        loc2.setEtaNextStopMinutes(4);
        b2.setCurrentLocation(loc2);
        busRepository.save(b2);

        // Bus 3: Ghatkesar Shuttle
        Bus b3 = new Bus("AU-BUS-03", "TS-08-UB-1003", 40, "B. Ramesh", "+91 98480 12347", r3);
        b3.setStatus(BusStatus.IN_TRANSIT);
        b3.setLastUpdated(LocalDateTime.now());
        BusLocation loc3 = new BusLocation(b3, s1.getLatitude(), s1.getLongitude(), 25.0, 45.0);
        loc3.setCurrentStop(s1);
        loc3.setNextStop(s2);
        loc3.setNextStopName(s2.getName());
        loc3.setDistanceToNextStopKm(1.1);
        loc3.setEtaNextStopMinutes(2);
        b3.setCurrentLocation(loc3);
        busRepository.save(b3);

        // Bus 4: Spare Fleet
        Bus b4 = new Bus("AU-BUS-04", "TS-08-UB-1004", 35, "P. Shekhar", "+91 98480 12348", null);
        b4.setStatus(BusStatus.IDLE);
        b4.setLastUpdated(LocalDateTime.now());
        BusLocation loc4 = new BusLocation(b4, s1.getLatitude(), s1.getLongitude(), 0.0, 0.0);
        loc4.setCurrentStop(s1);
        b4.setCurrentLocation(loc4);
        busRepository.save(b4);

        log.info("Database successfully seeded for Anurag University: 13 stops, 3 routes, and 4 buses!");
    }
}
