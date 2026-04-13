package com.zakariaalla.elasticsearchdemo.search.utils;


import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.zakariaalla.elasticsearchdemo.search.SearchRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

public final class SearchUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SearchUtil.class);

    private SearchUtil() {
    }

    public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto) {
        try {
            final Query query = getQueryBuilder(dto);
            if (query == null) {
                return null;
            }

            return SearchRequest.of(s -> s.index(indexName).query(query));
        } catch (final Exception e) {
            LOG.error("Failed to build search request for index {}", indexName, e);
            return null;
        }
    }

    private static Query getQueryBuilder(final SearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        final String term = dto.getSearchTerm();
        if (fields.size() > 1) {
            return Query.of(q -> q.multiMatch(m -> m
                    .query(term)
                    .type(TextQueryType.CrossFields)
                    .operator(Operator.And)
                    .fields(fields)
            ));
        }

        return fields.stream()
                .findFirst()
                .map(field -> Query.of(q -> q.match(MatchQuery.of(m -> m
                        .field(field)
                        .query(term)
                        .operator(Operator.And)
                ))))
                .orElse(null);
    }
}
