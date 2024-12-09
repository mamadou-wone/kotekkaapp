package com.kotekka.app.domain;

import static com.kotekka.app.domain.FeatureFlagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeatureFlagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeatureFlag.class);
        FeatureFlag featureFlag1 = getFeatureFlagSample1();
        FeatureFlag featureFlag2 = new FeatureFlag();
        assertThat(featureFlag1).isNotEqualTo(featureFlag2);

        featureFlag2.setId(featureFlag1.getId());
        assertThat(featureFlag1).isEqualTo(featureFlag2);

        featureFlag2 = getFeatureFlagSample2();
        assertThat(featureFlag1).isNotEqualTo(featureFlag2);
    }
}
