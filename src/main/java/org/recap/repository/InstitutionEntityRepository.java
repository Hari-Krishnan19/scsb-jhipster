package org.recap.repository;

import org.recap.domain.InstitutionEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstitutionEntity entity.
 */
@SuppressWarnings("unused")
public interface InstitutionEntityRepository extends JpaRepository<InstitutionEntity,Long> {

    InstitutionEntity findByInstitutionCode(String institutionCode);
}
