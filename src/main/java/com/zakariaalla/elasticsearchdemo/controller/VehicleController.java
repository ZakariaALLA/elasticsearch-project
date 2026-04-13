package com.zakariaalla.elasticsearchdemo.controller;

import com.zakariaalla.elasticsearchdemo.documents.Vehicle;
import com.zakariaalla.elasticsearchdemo.search.SearchRequestDTO;
import com.zakariaalla.elasticsearchdemo.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    @PostMapping
    public void createPerson(@RequestBody final Vehicle vehicle) {
        this.vehicleService.indexVehicle(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable final String id) {
        return this.vehicleService.findVehicleById(id);
    }

    @PostMapping("/search")
    public List<Vehicle> search(@RequestBody final SearchRequestDTO dto) {
        return vehicleService.search(dto);
    }
}
