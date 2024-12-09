package com.kotekka.app.domain.enumeration;

/**
 * The TransactionStatus enumeration.
 */
public enum TransactionStatus {
    PENDING,
    TO_CONFIRM,
    CONFIRMED,
    REJECTED,
    PARTNER_CONFIRMED,
    PARTNER_REJECTED,
    IN_PROGRESS,
    PROCESSED,
    FAILED,
}
