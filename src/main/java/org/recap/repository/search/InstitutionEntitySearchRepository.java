package org.recap.repository.search;

import org.recap.domain.InstitutionEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstitutionEntity entity.
 */
public interface InstitutionEntitySearchRepository extends ElasticsearchRepository<InstitutionEntity, Long> {
}
