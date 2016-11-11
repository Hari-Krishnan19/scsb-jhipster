package org.recap.repository.search;

import org.recap.domain.ItemEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ItemEntity entity.
 */
public interface ItemEntitySearchRepository extends ElasticsearchRepository<ItemEntity, Long> {
}
