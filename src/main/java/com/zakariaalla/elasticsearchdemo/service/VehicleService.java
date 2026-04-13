package com.zakariaalla.elasticsearchdemo.service;

import com.zakariaalla.elasticsearchdemo.documents.Vehicle;
import com.zakariaalla.elasticsearchdemo.helper.Indices;
import com.zakariaalla.elasticsearchdemo.search.SearchRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Vehicle> search(final SearchRequestDTO dto) {
        try {
            Query query = buildQuery(dto);

            SearchHits<Vehicle> searchHits = elasticsearchOperations.search(query, Vehicle.class);
            return searchHits.stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }

    }

    private Query buildQuery(final SearchRequestDTO dto) {
        return NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field(dto.getFields().getFirst())
                                .query(dto.getSearchTerm())
                        )
                )
                .build();
    }
}
