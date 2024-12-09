package com.kotekka.app.repository;

import com.kotekka.app.domain.UserAffiliation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAffiliation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAffiliationRepository extends JpaRepository<UserAffiliation, Long>, JpaSpecificationExecutor<UserAffiliation> {}
