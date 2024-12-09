package com.kotekka.app.domain;

import static com.kotekka.app.domain.MoneyRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyRequest.class);
        MoneyRequest moneyRequest1 = getMoneyRequestSample1();
        MoneyRequest moneyRequest2 = new MoneyRequest();
        assertThat(moneyRequest1).isNotEqualTo(moneyRequest2);

        moneyRequest2.setId(moneyRequest1.getId());
        assertThat(moneyRequest1).isEqualTo(moneyRequest2);

        moneyRequest2 = getMoneyRequestSample2();
        assertThat(moneyRequest1).isNotEqualTo(moneyRequest2);
    }
}
