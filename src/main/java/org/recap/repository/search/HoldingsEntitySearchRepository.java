package org.recap.repository.search;

import org.recap.domain.HoldingsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HoldingsEntity entity.
 */
public interface HoldingsEntitySearchRepository extends ElasticsearchRepository<HoldingsEntity, Long> {
}
