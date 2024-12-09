package com.kotekka.app.repository;

import com.kotekka.app.domain.FailedAttemptHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FailedAttemptHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FailedAttemptHistoryRepository extends JpaRepository<FailedAttemptHistory, Long> {}
