package com.kotekka.app.domain;

import static com.kotekka.app.domain.PartnerCallTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerCallTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerCall.class);
        PartnerCall partnerCall1 = getPartnerCallSample1();
        PartnerCall partnerCall2 = new PartnerCall();
        assertThat(partnerCall1).isNotEqualTo(partnerCall2);

        partnerCall2.setId(partnerCall1.getId());
        assertThat(partnerCall1).isEqualTo(partnerCall2);

        partnerCall2 = getPartnerCallSample2();
        assertThat(partnerCall1).isNotEqualTo(partnerCall2);
    }
}
