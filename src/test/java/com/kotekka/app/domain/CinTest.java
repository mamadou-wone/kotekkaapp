package com.kotekka.app.domain;

import static com.kotekka.app.domain.CinTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cin.class);
        Cin cin1 = getCinSample1();
        Cin cin2 = new Cin();
        assertThat(cin1).isNotEqualTo(cin2);

        cin2.setId(cin1.getId());
        assertThat(cin1).isEqualTo(cin2);

        cin2 = getCinSample2();
        assertThat(cin1).isNotEqualTo(cin2);
    }
}
