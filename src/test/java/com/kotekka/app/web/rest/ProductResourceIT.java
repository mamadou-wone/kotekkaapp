package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.ProductAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Category;
import com.kotekka.app.domain.Collection;
import com.kotekka.app.domain.Product;
import com.kotekka.app.domain.enumeration.ProductStatus;
import com.kotekka.app.repository.ProductRepository;
import com.kotekka.app.service.ProductService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ProductStatus DEFAULT_STATUS = ProductStatus.Active;
    private static final ProductStatus UPDATED_STATUS = ProductStatus.Inactive;

    private static final byte[] DEFAULT_MEDIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MEDIA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MEDIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MEDIA_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COMPARE_AT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMPARE_AT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_COMPARE_AT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COST_PER_ITEM = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST_PER_ITEM = new BigDecimal(2);
    private static final BigDecimal SMALLER_COST_PER_ITEM = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PROFIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROFIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PROFIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MARGIN = new BigDecimal(1);
    private static final BigDecimal UPDATED_MARGIN = new BigDecimal(2);
    private static final BigDecimal SMALLER_MARGIN = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_INVENTORY_QUANTITY = 1;
    private static final Integer UPDATED_INVENTORY_QUANTITY = 2;
    private static final Integer SMALLER_INVENTORY_QUANTITY = 1 - 1;

    private static final String DEFAULT_INVENTORY_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TRACK_QUANTITY = false;
    private static final Boolean UPDATED_TRACK_QUANTITY = true;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        return new Product()
            .uuid(DEFAULT_UUID)
            .walletHolder(DEFAULT_WALLET_HOLDER)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .media(DEFAULT_MEDIA)
            .mediaContentType(DEFAULT_MEDIA_CONTENT_TYPE)
            .price(DEFAULT_PRICE)
            .compareAtPrice(DEFAULT_COMPARE_AT_PRICE)
            .costPerItem(DEFAULT_COST_PER_ITEM)
            .profit(DEFAULT_PROFIT)
            .margin(DEFAULT_MARGIN)
            .inventoryQuantity(DEFAULT_INVENTORY_QUANTITY)
            .inventoryLocation(DEFAULT_INVENTORY_LOCATION)
            .trackQuantity(DEFAULT_TRACK_QUANTITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        return new Product()
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .media(UPDATED_MEDIA)
            .mediaContentType(UPDATED_MEDIA_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .compareAtPrice(UPDATED_COMPARE_AT_PRICE)
            .costPerItem(UPDATED_COST_PER_ITEM)
            .profit(UPDATED_PROFIT)
            .margin(UPDATED_MARGIN)
            .inventoryQuantity(UPDATED_INVENTORY_QUANTITY)
            .inventoryLocation(UPDATED_INVENTORY_LOCATION)
            .trackQuantity(UPDATED_TRACK_QUANTITY);
    }

    @BeforeEach
    public void initTest() {
        product = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        var returnedProduct = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Product.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setUuid(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setTitle(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setDescription(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setStatus(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].mediaContentType").value(hasItem(DEFAULT_MEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].media").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MEDIA))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].compareAtPrice").value(hasItem(sameNumber(DEFAULT_COMPARE_AT_PRICE))))
            .andExpect(jsonPath("$.[*].costPerItem").value(hasItem(sameNumber(DEFAULT_COST_PER_ITEM))))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(sameNumber(DEFAULT_PROFIT))))
            .andExpect(jsonPath("$.[*].margin").value(hasItem(sameNumber(DEFAULT_MARGIN))))
            .andExpect(jsonPath("$.[*].inventoryQuantity").value(hasItem(DEFAULT_INVENTORY_QUANTITY)))
            .andExpect(jsonPath("$.[*].inventoryLocation").value(hasItem(DEFAULT_INVENTORY_LOCATION)))
            .andExpect(jsonPath("$.[*].trackQuantity").value(hasItem(DEFAULT_TRACK_QUANTITY.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.mediaContentType").value(DEFAULT_MEDIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.media").value(Base64.getEncoder().encodeToString(DEFAULT_MEDIA)))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.compareAtPrice").value(sameNumber(DEFAULT_COMPARE_AT_PRICE)))
            .andExpect(jsonPath("$.costPerItem").value(sameNumber(DEFAULT_COST_PER_ITEM)))
            .andExpect(jsonPath("$.profit").value(sameNumber(DEFAULT_PROFIT)))
            .andExpect(jsonPath("$.margin").value(sameNumber(DEFAULT_MARGIN)))
            .andExpect(jsonPath("$.inventoryQuantity").value(DEFAULT_INVENTORY_QUANTITY))
            .andExpect(jsonPath("$.inventoryLocation").value(DEFAULT_INVENTORY_LOCATION))
            .andExpect(jsonPath("$.trackQuantity").value(DEFAULT_TRACK_QUANTITY.booleanValue()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where uuid equals to
        defaultProductFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllProductsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where uuid in
        defaultProductFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllProductsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where uuid is not null
        defaultProductFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where walletHolder equals to
        defaultProductFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllProductsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where walletHolder in
        defaultProductFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllProductsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where walletHolder is not null
        defaultProductFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where title equals to
        defaultProductFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where title in
        defaultProductFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where title is not null
        defaultProductFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where title contains
        defaultProductFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where title does not contain
        defaultProductFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description equals to
        defaultProductFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description in
        defaultProductFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description contains
        defaultProductFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain
        defaultProductFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status equals to
        defaultProductFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status in
        defaultProductFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProductsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where status is not null
        defaultProductFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price equals to
        defaultProductFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price in
        defaultProductFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to
        defaultProductFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to
        defaultProductFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is less than
        defaultProductFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than
        defaultProductFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice equals to
        defaultProductFiltering("compareAtPrice.equals=" + DEFAULT_COMPARE_AT_PRICE, "compareAtPrice.equals=" + UPDATED_COMPARE_AT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice in
        defaultProductFiltering(
            "compareAtPrice.in=" + DEFAULT_COMPARE_AT_PRICE + "," + UPDATED_COMPARE_AT_PRICE,
            "compareAtPrice.in=" + UPDATED_COMPARE_AT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice is not null
        defaultProductFiltering("compareAtPrice.specified=true", "compareAtPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice is greater than or equal to
        defaultProductFiltering(
            "compareAtPrice.greaterThanOrEqual=" + DEFAULT_COMPARE_AT_PRICE,
            "compareAtPrice.greaterThanOrEqual=" + UPDATED_COMPARE_AT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice is less than or equal to
        defaultProductFiltering(
            "compareAtPrice.lessThanOrEqual=" + DEFAULT_COMPARE_AT_PRICE,
            "compareAtPrice.lessThanOrEqual=" + SMALLER_COMPARE_AT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice is less than
        defaultProductFiltering(
            "compareAtPrice.lessThan=" + UPDATED_COMPARE_AT_PRICE,
            "compareAtPrice.lessThan=" + DEFAULT_COMPARE_AT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCompareAtPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where compareAtPrice is greater than
        defaultProductFiltering(
            "compareAtPrice.greaterThan=" + SMALLER_COMPARE_AT_PRICE,
            "compareAtPrice.greaterThan=" + DEFAULT_COMPARE_AT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem equals to
        defaultProductFiltering("costPerItem.equals=" + DEFAULT_COST_PER_ITEM, "costPerItem.equals=" + UPDATED_COST_PER_ITEM);
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem in
        defaultProductFiltering(
            "costPerItem.in=" + DEFAULT_COST_PER_ITEM + "," + UPDATED_COST_PER_ITEM,
            "costPerItem.in=" + UPDATED_COST_PER_ITEM
        );
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem is not null
        defaultProductFiltering("costPerItem.specified=true", "costPerItem.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem is greater than or equal to
        defaultProductFiltering(
            "costPerItem.greaterThanOrEqual=" + DEFAULT_COST_PER_ITEM,
            "costPerItem.greaterThanOrEqual=" + UPDATED_COST_PER_ITEM
        );
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem is less than or equal to
        defaultProductFiltering(
            "costPerItem.lessThanOrEqual=" + DEFAULT_COST_PER_ITEM,
            "costPerItem.lessThanOrEqual=" + SMALLER_COST_PER_ITEM
        );
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem is less than
        defaultProductFiltering("costPerItem.lessThan=" + UPDATED_COST_PER_ITEM, "costPerItem.lessThan=" + DEFAULT_COST_PER_ITEM);
    }

    @Test
    @Transactional
    void getAllProductsByCostPerItemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where costPerItem is greater than
        defaultProductFiltering("costPerItem.greaterThan=" + SMALLER_COST_PER_ITEM, "costPerItem.greaterThan=" + DEFAULT_COST_PER_ITEM);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit equals to
        defaultProductFiltering("profit.equals=" + DEFAULT_PROFIT, "profit.equals=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit in
        defaultProductFiltering("profit.in=" + DEFAULT_PROFIT + "," + UPDATED_PROFIT, "profit.in=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit is not null
        defaultProductFiltering("profit.specified=true", "profit.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit is greater than or equal to
        defaultProductFiltering("profit.greaterThanOrEqual=" + DEFAULT_PROFIT, "profit.greaterThanOrEqual=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit is less than or equal to
        defaultProductFiltering("profit.lessThanOrEqual=" + DEFAULT_PROFIT, "profit.lessThanOrEqual=" + SMALLER_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit is less than
        defaultProductFiltering("profit.lessThan=" + UPDATED_PROFIT, "profit.lessThan=" + DEFAULT_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where profit is greater than
        defaultProductFiltering("profit.greaterThan=" + SMALLER_PROFIT, "profit.greaterThan=" + DEFAULT_PROFIT);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin equals to
        defaultProductFiltering("margin.equals=" + DEFAULT_MARGIN, "margin.equals=" + UPDATED_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin in
        defaultProductFiltering("margin.in=" + DEFAULT_MARGIN + "," + UPDATED_MARGIN, "margin.in=" + UPDATED_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin is not null
        defaultProductFiltering("margin.specified=true", "margin.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin is greater than or equal to
        defaultProductFiltering("margin.greaterThanOrEqual=" + DEFAULT_MARGIN, "margin.greaterThanOrEqual=" + UPDATED_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin is less than or equal to
        defaultProductFiltering("margin.lessThanOrEqual=" + DEFAULT_MARGIN, "margin.lessThanOrEqual=" + SMALLER_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin is less than
        defaultProductFiltering("margin.lessThan=" + UPDATED_MARGIN, "margin.lessThan=" + DEFAULT_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where margin is greater than
        defaultProductFiltering("margin.greaterThan=" + SMALLER_MARGIN, "margin.greaterThan=" + DEFAULT_MARGIN);
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity equals to
        defaultProductFiltering(
            "inventoryQuantity.equals=" + DEFAULT_INVENTORY_QUANTITY,
            "inventoryQuantity.equals=" + UPDATED_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity in
        defaultProductFiltering(
            "inventoryQuantity.in=" + DEFAULT_INVENTORY_QUANTITY + "," + UPDATED_INVENTORY_QUANTITY,
            "inventoryQuantity.in=" + UPDATED_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity is not null
        defaultProductFiltering("inventoryQuantity.specified=true", "inventoryQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity is greater than or equal to
        defaultProductFiltering(
            "inventoryQuantity.greaterThanOrEqual=" + DEFAULT_INVENTORY_QUANTITY,
            "inventoryQuantity.greaterThanOrEqual=" + UPDATED_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity is less than or equal to
        defaultProductFiltering(
            "inventoryQuantity.lessThanOrEqual=" + DEFAULT_INVENTORY_QUANTITY,
            "inventoryQuantity.lessThanOrEqual=" + SMALLER_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity is less than
        defaultProductFiltering(
            "inventoryQuantity.lessThan=" + UPDATED_INVENTORY_QUANTITY,
            "inventoryQuantity.lessThan=" + DEFAULT_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryQuantity is greater than
        defaultProductFiltering(
            "inventoryQuantity.greaterThan=" + SMALLER_INVENTORY_QUANTITY,
            "inventoryQuantity.greaterThan=" + DEFAULT_INVENTORY_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryLocation equals to
        defaultProductFiltering(
            "inventoryLocation.equals=" + DEFAULT_INVENTORY_LOCATION,
            "inventoryLocation.equals=" + UPDATED_INVENTORY_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryLocation in
        defaultProductFiltering(
            "inventoryLocation.in=" + DEFAULT_INVENTORY_LOCATION + "," + UPDATED_INVENTORY_LOCATION,
            "inventoryLocation.in=" + UPDATED_INVENTORY_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryLocation is not null
        defaultProductFiltering("inventoryLocation.specified=true", "inventoryLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByInventoryLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryLocation contains
        defaultProductFiltering(
            "inventoryLocation.contains=" + DEFAULT_INVENTORY_LOCATION,
            "inventoryLocation.contains=" + UPDATED_INVENTORY_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProductsByInventoryLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where inventoryLocation does not contain
        defaultProductFiltering(
            "inventoryLocation.doesNotContain=" + UPDATED_INVENTORY_LOCATION,
            "inventoryLocation.doesNotContain=" + DEFAULT_INVENTORY_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProductsByTrackQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where trackQuantity equals to
        defaultProductFiltering("trackQuantity.equals=" + DEFAULT_TRACK_QUANTITY, "trackQuantity.equals=" + UPDATED_TRACK_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByTrackQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where trackQuantity in
        defaultProductFiltering(
            "trackQuantity.in=" + DEFAULT_TRACK_QUANTITY + "," + UPDATED_TRACK_QUANTITY,
            "trackQuantity.in=" + UPDATED_TRACK_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllProductsByTrackQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where trackQuantity is not null
        defaultProductFiltering("trackQuantity.specified=true", "trackQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();
        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to (categoryId + 1)
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCollectionsIsEqualToSomething() throws Exception {
        Collection collections;
        if (TestUtil.findAll(em, Collection.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            collections = CollectionResourceIT.createEntity();
        } else {
            collections = TestUtil.findAll(em, Collection.class).get(0);
        }
        em.persist(collections);
        em.flush();
        product.addCollections(collections);
        productRepository.saveAndFlush(product);
        Long collectionsId = collections.getId();
        // Get all the productList where collections equals to collectionsId
        defaultProductShouldBeFound("collectionsId.equals=" + collectionsId);

        // Get all the productList where collections equals to (collectionsId + 1)
        defaultProductShouldNotBeFound("collectionsId.equals=" + (collectionsId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].mediaContentType").value(hasItem(DEFAULT_MEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].media").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MEDIA))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].compareAtPrice").value(hasItem(sameNumber(DEFAULT_COMPARE_AT_PRICE))))
            .andExpect(jsonPath("$.[*].costPerItem").value(hasItem(sameNumber(DEFAULT_COST_PER_ITEM))))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(sameNumber(DEFAULT_PROFIT))))
            .andExpect(jsonPath("$.[*].margin").value(hasItem(sameNumber(DEFAULT_MARGIN))))
            .andExpect(jsonPath("$.[*].inventoryQuantity").value(hasItem(DEFAULT_INVENTORY_QUANTITY)))
            .andExpect(jsonPath("$.[*].inventoryLocation").value(hasItem(DEFAULT_INVENTORY_LOCATION)))
            .andExpect(jsonPath("$.[*].trackQuantity").value(hasItem(DEFAULT_TRACK_QUANTITY.booleanValue())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .media(UPDATED_MEDIA)
            .mediaContentType(UPDATED_MEDIA_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .compareAtPrice(UPDATED_COMPARE_AT_PRICE)
            .costPerItem(UPDATED_COST_PER_ITEM)
            .profit(UPDATED_PROFIT)
            .margin(UPDATED_MARGIN)
            .inventoryQuantity(UPDATED_INVENTORY_QUANTITY)
            .inventoryLocation(UPDATED_INVENTORY_LOCATION)
            .trackQuantity(UPDATED_TRACK_QUANTITY);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL_ID, product.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .media(UPDATED_MEDIA)
            .mediaContentType(UPDATED_MEDIA_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .costPerItem(UPDATED_COST_PER_ITEM)
            .profit(UPDATED_PROFIT)
            .margin(UPDATED_MARGIN)
            .inventoryQuantity(UPDATED_INVENTORY_QUANTITY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .media(UPDATED_MEDIA)
            .mediaContentType(UPDATED_MEDIA_CONTENT_TYPE)
            .price(UPDATED_PRICE)
            .compareAtPrice(UPDATED_COMPARE_AT_PRICE)
            .costPerItem(UPDATED_COST_PER_ITEM)
            .profit(UPDATED_PROFIT)
            .margin(UPDATED_MARGIN)
            .inventoryQuantity(UPDATED_INVENTORY_QUANTITY)
            .inventoryLocation(UPDATED_INVENTORY_LOCATION)
            .trackQuantity(UPDATED_TRACK_QUANTITY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
