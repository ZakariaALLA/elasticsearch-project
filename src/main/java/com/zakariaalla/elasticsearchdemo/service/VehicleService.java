package com.zakariaalla.elasticsearchdemo.service;

import com.zakariaalla.elasticsearchdemo.documents.Vehicle;
import com.zakariaalla.elasticsearchdemo.helper.Indices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public VehicleService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Boolean indexVehicle(final Vehicle vehicle) {
        try {
            elasticsearchOperations.save(vehicle, IndexCoordinates.of(Indices.VEHICLE_INDEX));
            return true;
        } catch (Exception e) {
            LOGGER.error("Error indexing vehicle: {}", e.getMessage());
            return false;
        }
    }

    public Vehicle findVehicleById(final String id) {
        try {
            return elasticsearchOperations.get(id, Vehicle.class, IndexCoordinates.of(Indices.VEHICLE_INDEX));
        } catch (Exception e) {
            LOGGER.error("Error finding vehicle by id {}: {}", id, e.getMessage());
            return null;
        }
    }

}
