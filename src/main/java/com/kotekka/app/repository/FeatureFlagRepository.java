package com.kotekka.app.repository;

import com.kotekka.app.domain.FeatureFlag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeatureFlag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long>, JpaSpecificationExecutor<FeatureFlag> {}
