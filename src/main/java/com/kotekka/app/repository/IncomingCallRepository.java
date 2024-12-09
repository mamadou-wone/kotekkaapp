package com.kotekka.app.repository;

import com.kotekka.app.domain.IncomingCall;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncomingCall entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomingCallRepository extends JpaRepository<IncomingCall, Long>, JpaSpecificationExecutor<IncomingCall> {}
