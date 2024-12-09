package com.kotekka.app.domain;

import static com.kotekka.app.domain.FailedAttemptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FailedAttemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FailedAttempt.class);
        FailedAttempt failedAttempt1 = getFailedAttemptSample1();
        FailedAttempt failedAttempt2 = new FailedAttempt();
        assertThat(failedAttempt1).isNotEqualTo(failedAttempt2);

        failedAttempt2.setId(failedAttempt1.getId());
        assertThat(failedAttempt1).isEqualTo(failedAttempt2);

        failedAttempt2 = getFailedAttemptSample2();
        assertThat(failedAttempt1).isNotEqualTo(failedAttempt2);
    }
}
