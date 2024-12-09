package com.kotekka.app.domain;

import static com.kotekka.app.domain.UserPreferenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreference.class);
        UserPreference userPreference1 = getUserPreferenceSample1();
        UserPreference userPreference2 = new UserPreference();
        assertThat(userPreference1).isNotEqualTo(userPreference2);

        userPreference2.setId(userPreference1.getId());
        assertThat(userPreference1).isEqualTo(userPreference2);

        userPreference2 = getUserPreferenceSample2();
        assertThat(userPreference1).isNotEqualTo(userPreference2);
    }
}
