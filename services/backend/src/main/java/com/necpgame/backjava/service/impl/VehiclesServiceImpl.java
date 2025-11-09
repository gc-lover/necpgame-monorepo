package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LogisticsVehicleEntity;
import com.necpgame.backjava.model.GetVehicles200Response;
import com.necpgame.backjava.model.Vehicle;
import com.necpgame.backjava.repository.LogisticsVehicleRepository;
import com.necpgame.backjava.service.VehiclesService;
import com.necpgame.backjava.service.mapper.LogisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VehiclesServiceImpl implements VehiclesService {

    private final LogisticsVehicleRepository vehicleRepository;
    private final LogisticsMapper mapper;

    public VehiclesServiceImpl(LogisticsVehicleRepository vehicleRepository, LogisticsMapper mapper) {
        this.vehicleRepository = vehicleRepository;
        this.mapper = mapper;
    }

    @Override
    public GetVehicles200Response getVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .map(mapper::toVehicle)
                .collect(Collectors.toList());
        return mapper.toVehiclesResponse(vehicles);
    }
}
