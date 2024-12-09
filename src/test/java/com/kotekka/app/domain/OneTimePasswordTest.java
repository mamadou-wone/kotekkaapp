package com.kotekka.app.domain;

import static com.kotekka.app.domain.OneTimePasswordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OneTimePasswordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OneTimePassword.class);
        OneTimePassword oneTimePassword1 = getOneTimePasswordSample1();
        OneTimePassword oneTimePassword2 = new OneTimePassword();
        assertThat(oneTimePassword1).isNotEqualTo(oneTimePassword2);

        oneTimePassword2.setId(oneTimePassword1.getId());
        assertThat(oneTimePassword1).isEqualTo(oneTimePassword2);

        oneTimePassword2 = getOneTimePasswordSample2();
        assertThat(oneTimePassword1).isNotEqualTo(oneTimePassword2);
    }
}
