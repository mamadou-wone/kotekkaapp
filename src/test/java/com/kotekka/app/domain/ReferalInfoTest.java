package com.kotekka.app.domain;

import static com.kotekka.app.domain.ReferalInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReferalInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferalInfo.class);
        ReferalInfo referalInfo1 = getReferalInfoSample1();
        ReferalInfo referalInfo2 = new ReferalInfo();
        assertThat(referalInfo1).isNotEqualTo(referalInfo2);

        referalInfo2.setId(referalInfo1.getId());
        assertThat(referalInfo1).isEqualTo(referalInfo2);

        referalInfo2 = getReferalInfoSample2();
        assertThat(referalInfo1).isNotEqualTo(referalInfo2);
    }
}
