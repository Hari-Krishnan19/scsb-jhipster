package org.recap.repository;

import org.recap.domain.HoldingsEntity;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the HoldingsEntity entity.
 */
@SuppressWarnings("unused")
public interface HoldingsEntityRepository extends JpaRepository<HoldingsEntity,Long> {

    @Query("select distinct holdingsEntity from HoldingsEntity holdingsEntity left join fetch holdingsEntity.itemEntities")
    List<HoldingsEntity> findAllWithEagerRelationships();

    @Query("select holdingsEntity from HoldingsEntity holdingsEntity left join fetch holdingsEntity.itemEntities where holdingsEntity.id =:id")
    HoldingsEntity findOneWithEagerRelationships(@Param("id") Long id);

}
