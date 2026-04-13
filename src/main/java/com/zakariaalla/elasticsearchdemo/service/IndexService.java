package com.zakariaalla.elasticsearchdemo.service;

import com.zakariaalla.elasticsearchdemo.helper.Indices;
import com.zakariaalla.elasticsearchdemo.helper.Utils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexService {

    private final static Logger LOG = LoggerFactory.getLogger(IndexService.class);

    private final List<String> INDICES = List.of(Indices.VEHICLE_INDEX);
    private final ElasticsearchOperations elasticsearchOperations;

    public IndexService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void tryToCreateIndices() {
        try {
            recreateIndices(false);
        } catch (Exception e) {
            LOG.error("Error while creating indices: {}", e.getMessage());
        }
    }

    public void recreateIndices(final boolean deleteExisting) {
        final String settings = Utils.loadAsString("static/es-settings.json");

        if (settings == null) {
            LOG.error("Failed to load index settings");
            return;
        }

        for (final String indexName : INDICES) {
            try {
                IndexCoordinates index = IndexCoordinates.of(indexName);
                var indexOps = elasticsearchOperations.indexOps(index);

                if (indexOps.exists()) {
                    if (!deleteExisting) {
                        LOG.info("Index '{}' already exists, skipping", indexName);
                        continue;
                    }

                    boolean deleted = indexOps.delete();
                    LOG.info("Index '{}' deleted: {}", indexName, deleted);

                    if (!deleted) {
                        LOG.error("Failed to delete index '{}', skipping creation", indexName);
                        continue;
                    }
                }

                Document settingsDoc = Document.parse(settings);
                Document actualSettings = settingsDoc.containsKey("settings") ? Document.parse(settingsDoc.get("settings").toString()) : settingsDoc;

                boolean created = indexOps.create(actualSettings);
                if (!created) {
                    LOG.warn("Index '{}' was not created (create() returned false)", indexName);
                    continue;
                }

                final String mappings = loadMappings(indexName);
                if (mappings != null) {
                    try {
                        Document mappingDoc = Document.parse(mappings);
                        indexOps.putMapping(mappingDoc);
                    } catch (Exception e) {
                        LOG.warn("Failed to apply mapping for index '{}': {}", indexName, e.getMessage());
                    }
                }

                LOG.info("Index '{}' created successfully", indexName);

            } catch (final Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private String loadMappings(String indexName) {
        final String mappings = Utils.loadAsString("static/mappings/" + indexName + ".json");
        if (mappings == null) {
            LOG.error("Failed to load mappings for index with name '{}'", indexName);
            return null;
        }

        return mappings;
    }
}