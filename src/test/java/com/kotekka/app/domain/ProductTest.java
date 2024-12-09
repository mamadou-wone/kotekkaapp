package com.kotekka.app.domain;

import static com.kotekka.app.domain.CategoryTestSamples.*;
import static com.kotekka.app.domain.CollectionTestSamples.*;
import static com.kotekka.app.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kotekka.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void collectionsTest() {
        Product product = getProductRandomSampleGenerator();
        Collection collectionBack = getCollectionRandomSampleGenerator();

        product.addCollections(collectionBack);
        assertThat(product.getCollections()).containsOnly(collectionBack);

        product.removeCollections(collectionBack);
        assertThat(product.getCollections()).doesNotContain(collectionBack);

        product.collections(new HashSet<>(Set.of(collectionBack)));
        assertThat(product.getCollections()).containsOnly(collectionBack);

        product.setCollections(new HashSet<>());
        assertThat(product.getCollections()).doesNotContain(collectionBack);
    }
}
