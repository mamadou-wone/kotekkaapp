package com.kotekka.app.repository;

import com.kotekka.app.domain.OneTimePassword;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OneTimePassword entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long>, JpaSpecificationExecutor<OneTimePassword> {}
