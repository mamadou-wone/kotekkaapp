package com.kotekka.app.domain;

import static com.kotekka.app.domain.UserAccessTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAccessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccess.class);
        UserAccess userAccess1 = getUserAccessSample1();
        UserAccess userAccess2 = new UserAccess();
        assertThat(userAccess1).isNotEqualTo(userAccess2);

        userAccess2.setId(userAccess1.getId());
        assertThat(userAccess1).isEqualTo(userAccess2);

        userAccess2 = getUserAccessSample2();
        assertThat(userAccess1).isNotEqualTo(userAccess2);
    }
}
