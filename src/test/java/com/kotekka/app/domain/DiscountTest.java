package com.kotekka.app.domain;

import static com.kotekka.app.domain.DiscountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiscountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Discount.class);
        Discount discount1 = getDiscountSample1();
        Discount discount2 = new Discount();
        assertThat(discount1).isNotEqualTo(discount2);

        discount2.setId(discount1.getId());
        assertThat(discount1).isEqualTo(discount2);

        discount2 = getDiscountSample2();
        assertThat(discount1).isNotEqualTo(discount2);
    }
}
