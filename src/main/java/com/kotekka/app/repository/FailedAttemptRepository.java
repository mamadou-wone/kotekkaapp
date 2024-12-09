package com.kotekka.app.repository;

import com.kotekka.app.domain.FailedAttempt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FailedAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FailedAttemptRepository extends JpaRepository<FailedAttempt, Long>, JpaSpecificationExecutor<FailedAttempt> {}
