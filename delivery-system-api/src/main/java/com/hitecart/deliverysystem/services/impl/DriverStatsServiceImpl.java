package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dao.DriverStatsRepository;
import com.hitecart.deliverysystem.model.Driver;
import com.hitecart.deliverysystem.model.DriverStats;
import com.hitecart.deliverysystem.services.IDriverStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverStatsServiceImpl implements IDriverStatsService {

    private final DriverStatsRepository driverStatsRepository;

    @Transactional
    @Override
    public void createStatsForDriver(Driver driver) {
        if (!driverStatsRepository.findByDriverId(driver.getId()).isPresent()) {
            DriverStats newStats = new DriverStats();
            newStats.setDriver(driver);
            driverStatsRepository.save(newStats);
            log.warn("Created missing stats record for driver {}", driver.getId());
        }
    }
}
