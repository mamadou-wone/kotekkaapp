package com.kotekka.app.domain;

import static com.kotekka.app.domain.CacheInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CacheInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CacheInfo.class);
        CacheInfo cacheInfo1 = getCacheInfoSample1();
        CacheInfo cacheInfo2 = new CacheInfo();
        assertThat(cacheInfo1).isNotEqualTo(cacheInfo2);

        cacheInfo2.setId(cacheInfo1.getId());
        assertThat(cacheInfo1).isEqualTo(cacheInfo2);

        cacheInfo2 = getCacheInfoSample2();
        assertThat(cacheInfo1).isNotEqualTo(cacheInfo2);
    }
}
