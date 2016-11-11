package org.recap.repository;

import org.recap.domain.BibliographicEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BibliographicEntity entity.
 */
@SuppressWarnings("unused")
public interface BibliographicEntityRepository extends JpaRepository<BibliographicEntity,Long> {

    @Query("select distinct bibliographicEntity from BibliographicEntity bibliographicEntity left join fetch bibliographicEntity.holdingsEntities")
    List<BibliographicEntity> findAllWithEagerRelationships();

    @Query("select bibliographicEntity from BibliographicEntity bibliographicEntity left join fetch bibliographicEntity.holdingsEntities where bibliographicEntity.id =:id")
    BibliographicEntity findOneWithEagerRelationships(@Param("id") Long id);

}
