package com.kotekka.app.domain;

import static com.kotekka.app.domain.FailedAttemptHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FailedAttemptHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FailedAttemptHistory.class);
        FailedAttemptHistory failedAttemptHistory1 = getFailedAttemptHistorySample1();
        FailedAttemptHistory failedAttemptHistory2 = new FailedAttemptHistory();
        assertThat(failedAttemptHistory1).isNotEqualTo(failedAttemptHistory2);

        failedAttemptHistory2.setId(failedAttemptHistory1.getId());
        assertThat(failedAttemptHistory1).isEqualTo(failedAttemptHistory2);

        failedAttemptHistory2 = getFailedAttemptHistorySample2();
        assertThat(failedAttemptHistory1).isNotEqualTo(failedAttemptHistory2);
    }
}
