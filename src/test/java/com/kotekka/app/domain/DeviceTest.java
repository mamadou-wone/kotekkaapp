package com.kotekka.app.domain;

import static com.kotekka.app.domain.DeviceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = getDeviceSample1();
        Device device2 = new Device();
        assertThat(device1).isNotEqualTo(device2);

        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);

        device2 = getDeviceSample2();
        assertThat(device1).isNotEqualTo(device2);
    }
}
