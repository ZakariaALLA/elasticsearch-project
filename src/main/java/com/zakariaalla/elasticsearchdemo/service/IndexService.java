package com.zakariaalla.elasticsearchdemo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zakariaalla.elasticsearchdemo.helper.Indices;
import com.zakariaalla.elasticsearchdemo.helper.Utils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexService {

    private final static Logger LOG = LoggerFactory.getLogger(IndexService.class);

    private final List<String> INDICES_TO_CREATE = List.of(Indices.VEHICLE_INDEX);
    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IndexService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void createIndices() {
        final String settings = Utils.loadAsString("static/es-settings.json");
        for (String indexName : INDICES_TO_CREATE) {
            try {
                IndexOperations indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
                boolean indexExists = indexOps.exists();
                if (indexExists) {
                    continue;
                }

                final String mappings = Utils.loadAsString("static/mappings/" + indexName + ".json");
                if (settings == null || mappings == null) {
                    LOG.error("Error while loading settings or mappings for index {}. Skipping index creation.", indexName);
                    continue;
                }

                Map<String, Object> settingsMap = null;
                Map<String, Object> mappingMap = null;
                try {
                    settingsMap = objectMapper.readValue(settings, new TypeReference<>() {
                    });
                } catch (Exception ex) {
                    LOG.warn("Failed to parse settings JSON for index {}: {}", indexName, ex.getMessage());
                }

                try {
                    mappingMap = objectMapper.readValue(mappings, new TypeReference<>() {
                    });
                } catch (Exception ex) {
                    LOG.warn("Failed to parse mappings JSON for index {}: {}", indexName, ex.getMessage());
                }

                boolean created;
                if (settingsMap != null) {
                    created = indexOps.create(settingsMap);
                } else {
                    created = indexOps.create();
                }

                if (created && mappingMap != null) {
                    indexOps.putMapping((Document) mappingMap);
                }

                LOG.info("Index {} created successfully.", indexName);
            } catch (final Exception e) {
                LOG.error("Error while checking if index {} exists: {}", indexName, e.getMessage());
            }
        }
    }
}
