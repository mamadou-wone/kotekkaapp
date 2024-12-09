package com.kotekka.app.repository;

import com.kotekka.app.domain.CacheInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CacheInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CacheInfoRepository extends JpaRepository<CacheInfo, Long>, JpaSpecificationExecutor<CacheInfo> {}
