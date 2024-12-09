package com.kotekka.app.domain;

import static com.kotekka.app.domain.UserAffiliationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAffiliationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAffiliation.class);
        UserAffiliation userAffiliation1 = getUserAffiliationSample1();
        UserAffiliation userAffiliation2 = new UserAffiliation();
        assertThat(userAffiliation1).isNotEqualTo(userAffiliation2);

        userAffiliation2.setId(userAffiliation1.getId());
        assertThat(userAffiliation1).isEqualTo(userAffiliation2);

        userAffiliation2 = getUserAffiliationSample2();
        assertThat(userAffiliation1).isNotEqualTo(userAffiliation2);
    }
}
