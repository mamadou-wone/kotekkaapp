package com.kotekka.app.repository;

import com.kotekka.app.domain.MoneyRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoneyRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyRequestRepository extends JpaRepository<MoneyRequest, Long>, JpaSpecificationExecutor<MoneyRequest> {}
