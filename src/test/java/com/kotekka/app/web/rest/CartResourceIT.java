package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.CartAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Cart;
import com.kotekka.app.repository.CartRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final Integer DEFAULT_TOTAL_QUANTITY = 1;
    private static final Integer UPDATED_TOTAL_QUANTITY = 2;
    private static final Integer SMALLER_TOTAL_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartMockMvc;

    private Cart cart;

    private Cart insertedCart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity() {
        return new Cart()
            .uuid(DEFAULT_UUID)
            .walletHolder(DEFAULT_WALLET_HOLDER)
            .totalQuantity(DEFAULT_TOTAL_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .currency(DEFAULT_CURRENCY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createUpdatedEntity() {
        return new Cart()
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        cart = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCart != null) {
            cartRepository.delete(insertedCart);
            insertedCart = null;
        }
    }

    @Test
    @Transactional
    void createCart() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cart
        var returnedCart = om.readValue(
            restCartMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cart.class
        );

        // Validate the Cart in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCartUpdatableFieldsEquals(returnedCart, getPersistedCart(returnedCart));

        insertedCart = returnedCart;
    }

    @Test
    @Transactional
    void createCartWithExistingId() throws Exception {
        // Create the Cart with an existing ID
        cart.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cart.setUuid(null);

        // Create the Cart, which fails.

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWalletHolderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cart.setWalletHolder(null);

        // Create the Cart, which fails.

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cart.setTotalQuantity(null);

        // Create the Cart, which fails.

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cart.setTotalPrice(null);

        // Create the Cart, which fails.

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarts() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCart() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc
            .perform(get(ENTITY_API_URL_ID, cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.totalQuantity").value(DEFAULT_TOTAL_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCartsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        Long id = cart.getId();

        defaultCartFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCartFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCartFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where uuid equals to
        defaultCartFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCartsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where uuid in
        defaultCartFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCartsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where uuid is not null
        defaultCartFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where walletHolder equals to
        defaultCartFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllCartsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where walletHolder in
        defaultCartFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllCartsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where walletHolder is not null
        defaultCartFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity equals to
        defaultCartFiltering("totalQuantity.equals=" + DEFAULT_TOTAL_QUANTITY, "totalQuantity.equals=" + UPDATED_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity in
        defaultCartFiltering(
            "totalQuantity.in=" + DEFAULT_TOTAL_QUANTITY + "," + UPDATED_TOTAL_QUANTITY,
            "totalQuantity.in=" + UPDATED_TOTAL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity is not null
        defaultCartFiltering("totalQuantity.specified=true", "totalQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity is greater than or equal to
        defaultCartFiltering(
            "totalQuantity.greaterThanOrEqual=" + DEFAULT_TOTAL_QUANTITY,
            "totalQuantity.greaterThanOrEqual=" + UPDATED_TOTAL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity is less than or equal to
        defaultCartFiltering(
            "totalQuantity.lessThanOrEqual=" + DEFAULT_TOTAL_QUANTITY,
            "totalQuantity.lessThanOrEqual=" + SMALLER_TOTAL_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity is less than
        defaultCartFiltering("totalQuantity.lessThan=" + UPDATED_TOTAL_QUANTITY, "totalQuantity.lessThan=" + DEFAULT_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartsByTotalQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalQuantity is greater than
        defaultCartFiltering("totalQuantity.greaterThan=" + SMALLER_TOTAL_QUANTITY, "totalQuantity.greaterThan=" + DEFAULT_TOTAL_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice equals to
        defaultCartFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice in
        defaultCartFiltering("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE, "totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is not null
        defaultCartFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than or equal to
        defaultCartFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than or equal to
        defaultCartFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than
        defaultCartFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than
        defaultCartFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where currency equals to
        defaultCartFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCartsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where currency in
        defaultCartFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCartsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where currency is not null
        defaultCartFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where currency contains
        defaultCartFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCartsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where currency does not contain
        defaultCartFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate equals to
        defaultCartFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate in
        defaultCartFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCartsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdDate is not null
        defaultCartFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate equals to
        defaultCartFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate in
        defaultCartFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCartsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        // Get all the cartList where lastModifiedDate is not null
        defaultCartFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultCartFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCartShouldBeFound(shouldBeFound);
        defaultCartShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartShouldBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartShouldNotBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCart() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCart are not directly saved in db
        em.detach(updatedCart);
        updatedCart
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCartToMatchAllProperties(updatedCart);
    }

    @Test
    @Transactional
    void putNonExistingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(put(ENTITY_API_URL_ID, cart.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartWithPatch() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCart, cart), getPersistedCart(cart));
    }

    @Test
    @Transactional
    void fullUpdateCartWithPatch() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart
            .uuid(UPDATED_UUID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .currency(UPDATED_CURRENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartUpdatableFieldsEquals(partialUpdatedCart, getPersistedCart(partialUpdatedCart));
    }

    @Test
    @Transactional
    void patchNonExistingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(patch(ENTITY_API_URL_ID, cart.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCart() throws Exception {
        // Initialize the database
        insertedCart = cartRepository.saveAndFlush(cart);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cart
        restCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, cart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cartRepository.count();
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

    protected Cart getPersistedCart(Cart cart) {
        return cartRepository.findById(cart.getId()).orElseThrow();
    }

    protected void assertPersistedCartToMatchAllProperties(Cart expectedCart) {
        assertCartAllPropertiesEquals(expectedCart, getPersistedCart(expectedCart));
    }

    protected void assertPersistedCartToMatchUpdatableProperties(Cart expectedCart) {
        assertCartAllUpdatablePropertiesEquals(expectedCart, getPersistedCart(expectedCart));
    }
}
