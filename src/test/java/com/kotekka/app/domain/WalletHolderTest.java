package com.kotekka.app.domain;

import static com.kotekka.app.domain.WalletHolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletHolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletHolder.class);
        WalletHolder walletHolder1 = getWalletHolderSample1();
        WalletHolder walletHolder2 = new WalletHolder();
        assertThat(walletHolder1).isNotEqualTo(walletHolder2);

        walletHolder2.setId(walletHolder1.getId());
        assertThat(walletHolder1).isEqualTo(walletHolder2);

        walletHolder2 = getWalletHolderSample2();
        assertThat(walletHolder1).isNotEqualTo(walletHolder2);
    }
}
