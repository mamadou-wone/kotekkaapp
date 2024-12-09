package com.kotekka.app.domain;

import static com.kotekka.app.domain.AuditLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditLog.class);
        AuditLog auditLog1 = getAuditLogSample1();
        AuditLog auditLog2 = new AuditLog();
        assertThat(auditLog1).isNotEqualTo(auditLog2);

        auditLog2.setId(auditLog1.getId());
        assertThat(auditLog1).isEqualTo(auditLog2);

        auditLog2 = getAuditLogSample2();
        assertThat(auditLog1).isNotEqualTo(auditLog2);
    }
}
