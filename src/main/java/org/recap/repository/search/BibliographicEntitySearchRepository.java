package org.recap.repository.search;

import org.recap.domain.BibliographicEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BibliographicEntity entity.
 */
public interface BibliographicEntitySearchRepository extends ElasticsearchRepository<BibliographicEntity, Long> {
}
