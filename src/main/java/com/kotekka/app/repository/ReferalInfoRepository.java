package com.kotekka.app.repository;

import com.kotekka.app.domain.ReferalInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReferalInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferalInfoRepository extends JpaRepository<ReferalInfo, Long>, JpaSpecificationExecutor<ReferalInfo> {}
