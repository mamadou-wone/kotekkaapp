package com.kotekka.app.repository;

import com.kotekka.app.domain.PartnerCall;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PartnerCall entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerCallRepository extends JpaRepository<PartnerCall, Long>, JpaSpecificationExecutor<PartnerCall> {}
