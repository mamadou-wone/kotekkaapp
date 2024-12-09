package com.kotekka.app.domain;

import static com.kotekka.app.domain.IncomingCallTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomingCallTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomingCall.class);
        IncomingCall incomingCall1 = getIncomingCallSample1();
        IncomingCall incomingCall2 = new IncomingCall();
        assertThat(incomingCall1).isNotEqualTo(incomingCall2);

        incomingCall2.setId(incomingCall1.getId());
        assertThat(incomingCall1).isEqualTo(incomingCall2);

        incomingCall2 = getIncomingCallSample2();
        assertThat(incomingCall1).isNotEqualTo(incomingCall2);
    }
}
