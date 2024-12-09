package com.kotekka.app.domain;

import static com.kotekka.app.domain.ServiceClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceClient.class);
        ServiceClient serviceClient1 = getServiceClientSample1();
        ServiceClient serviceClient2 = new ServiceClient();
        assertThat(serviceClient1).isNotEqualTo(serviceClient2);

        serviceClient2.setId(serviceClient1.getId());
        assertThat(serviceClient1).isEqualTo(serviceClient2);

        serviceClient2 = getServiceClientSample2();
        assertThat(serviceClient1).isNotEqualTo(serviceClient2);
    }
}
