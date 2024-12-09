package com.kotekka.app.repository;

import com.kotekka.app.domain.Cin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CinRepository extends JpaRepository<Cin, Long>, JpaSpecificationExecutor<Cin> {}
