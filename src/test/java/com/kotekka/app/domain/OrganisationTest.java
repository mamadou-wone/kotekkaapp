package com.kotekka.app.domain;

import static com.kotekka.app.domain.OrganisationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = getOrganisationSample1();
        Organisation organisation2 = new Organisation();
        assertThat(organisation1).isNotEqualTo(organisation2);

        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);

        organisation2 = getOrganisationSample2();
        assertThat(organisation1).isNotEqualTo(organisation2);
    }
}
