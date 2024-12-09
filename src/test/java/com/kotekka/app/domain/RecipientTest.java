package com.kotekka.app.domain;

import static com.kotekka.app.domain.RecipientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipient.class);
        Recipient recipient1 = getRecipientSample1();
        Recipient recipient2 = new Recipient();
        assertThat(recipient1).isNotEqualTo(recipient2);

        recipient2.setId(recipient1.getId());
        assertThat(recipient1).isEqualTo(recipient2);

        recipient2 = getRecipientSample2();
        assertThat(recipient1).isNotEqualTo(recipient2);
    }
}
