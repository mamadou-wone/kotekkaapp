package com.kotekka.app.repository;

import com.kotekka.app.domain.ServiceClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceClientRepository extends JpaRepository<ServiceClient, Long>, JpaSpecificationExecutor<ServiceClient> {}
