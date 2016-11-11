package org.recap.repository;

import org.recap.domain.ItemEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ItemEntity entity.
 */
@SuppressWarnings("unused")
public interface ItemEntityRepository extends JpaRepository<ItemEntity,Long> {

}
