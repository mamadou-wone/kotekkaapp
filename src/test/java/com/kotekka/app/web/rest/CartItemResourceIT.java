package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.CartItemAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.CartItem;
import com.kotekka.app.repository.CartItemRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CartItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartItemResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final UUID DEFAULT_CART = UUID.randomUUID();
    private static final UUID UPDATED_CART = UUID.randomUUID();

    private static final UUID DEFAULT_PRODUCT = UUID.randomUUID();
    private static final UUID UPDATED_PRODUCT = UUID.randomUUID();

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/cart-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartItemMockMvc;

    private CartItem cartItem;

    private CartItem insertedCartItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createEntity() {
        return new CartItem()
            .uuid(DEFAULT_UUID)
            .cart(DEFAULT_CART)
            .product(DEFAULT_PRODUCT)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItem createUpdatedEntity() {
        return new CartItem()
            .uuid(UPDATED_UUID)
            .cart(UPDATED_CART)
            .product(UPDATED_PRODUCT)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE);
    }

    @BeforeEach
    public void initTest() {
        cartItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCartItem != null) {
            cartItemRepository.delete(insertedCartItem);
            insertedCartItem = null;
        }
    }

    @Test
    @Transactional
    void createCartItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CartItem
        var returnedCartItem = om.readValue(
            restCartItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CartItem.class
        );

        // Validate the CartItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCartItemUpdatableFieldsEquals(returnedCartItem, getPersistedCartItem(returnedCartItem));

        insertedCartItem = returnedCartItem;
    }

    @Test
    @Transactional
    void createCartItemWithExistingId() throws Exception {
        // Create the CartItem with an existing ID
        cartItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setUuid(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setCart(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setProduct(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setQuantity(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setPrice(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cartItem.setTotalPrice(null);

        // Create the CartItem, which fails.

        restCartItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCartItems() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].cart").value(hasItem(DEFAULT_CART.toString())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));
    }

    @Test
    @Transactional
    void getCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get the cartItem
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL_ID, cartItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartItem.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.cart").value(DEFAULT_CART.toString()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    void getCartItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        Long id = cartItem.getId();

        defaultCartItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCartItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCartItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartItemsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where uuid equals to
        defaultCartItemFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCartItemsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where uuid in
        defaultCartItemFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCartItemsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where uuid is not null
        defaultCartItemFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByCartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where cart equals to
        defaultCartItemFiltering("cart.equals=" + DEFAULT_CART, "cart.equals=" + UPDATED_CART);
    }

    @Test
    @Transactional
    void getAllCartItemsByCartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where cart in
        defaultCartItemFiltering("cart.in=" + DEFAULT_CART + "," + UPDATED_CART, "cart.in=" + UPDATED_CART);
    }

    @Test
    @Transactional
    void getAllCartItemsByCartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where cart is not null
        defaultCartItemFiltering("cart.specified=true", "cart.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where product equals to
        defaultCartItemFiltering("product.equals=" + DEFAULT_PRODUCT, "product.equals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllCartItemsByProductIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where product in
        defaultCartItemFiltering("product.in=" + DEFAULT_PRODUCT + "," + UPDATED_PRODUCT, "product.in=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllCartItemsByProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where product is not null
        defaultCartItemFiltering("product.specified=true", "product.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity equals to
        defaultCartItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity in
        defaultCartItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is not null
        defaultCartItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is greater than or equal to
        defaultCartItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is less than or equal to
        defaultCartItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is less than
        defaultCartItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where quantity is greater than
        defaultCartItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price equals to
        defaultCartItemFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price in
        defaultCartItemFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price is not null
        defaultCartItemFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price is greater than or equal to
        defaultCartItemFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price is less than or equal to
        defaultCartItemFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price is less than
        defaultCartItemFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where price is greater than
        defaultCartItemFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice equals to
        defaultCartItemFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice in
        defaultCartItemFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is not null
        defaultCartItemFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is greater than or equal to
        defaultCartItemFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is less than or equal to
        defaultCartItemFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is less than
        defaultCartItemFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartItemsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        // Get all the cartItemList where totalPrice is greater than
        defaultCartItemFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    private void defaultCartItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCartItemShouldBeFound(shouldBeFound);
        defaultCartItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartItemShouldBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].cart").value(hasItem(DEFAULT_CART.toString())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))));

        // Check, that the count call also returns 1
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartItemShouldNotBeFound(String filter) throws Exception {
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCartItem() throws Exception {
        // Get the cartItem
        restCartItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem
        CartItem updatedCartItem = cartItemRepository.findById(cartItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCartItem are not directly saved in db
        em.detach(updatedCartItem);
        updatedCartItem
            .uuid(UPDATED_UUID)
            .cart(UPDATED_CART)
            .product(UPDATED_PRODUCT)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCartItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCartItemToMatchAllProperties(updatedCartItem);
    }

    @Test
    @Transactional
    void putNonExistingCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cartItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem.cart(UPDATED_CART).product(UPDATED_PRODUCT).quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCartItem, cartItem), getPersistedCartItem(cartItem));
    }

    @Test
    @Transactional
    void fullUpdateCartItemWithPatch() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cartItem using partial update
        CartItem partialUpdatedCartItem = new CartItem();
        partialUpdatedCartItem.setId(cartItem.getId());

        partialUpdatedCartItem
            .uuid(UPDATED_UUID)
            .cart(UPDATED_CART)
            .product(UPDATED_PRODUCT)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCartItem))
            )
            .andExpect(status().isOk());

        // Validate the CartItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartItemUpdatableFieldsEquals(partialUpdatedCartItem, getPersistedCartItem(partialUpdatedCartItem));
    }

    @Test
    @Transactional
    void patchNonExistingCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cartItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cartItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cartItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cartItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartItem() throws Exception {
        // Initialize the database
        insertedCartItem = cartItemRepository.saveAndFlush(cartItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cartItem
        restCartItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cartItemRepository.count();
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

    protected CartItem getPersistedCartItem(CartItem cartItem) {
        return cartItemRepository.findById(cartItem.getId()).orElseThrow();
    }

    protected void assertPersistedCartItemToMatchAllProperties(CartItem expectedCartItem) {
        assertCartItemAllPropertiesEquals(expectedCartItem, getPersistedCartItem(expectedCartItem));
    }

    protected void assertPersistedCartItemToMatchUpdatableProperties(CartItem expectedCartItem) {
        assertCartItemAllUpdatablePropertiesEquals(expectedCartItem, getPersistedCartItem(expectedCartItem));
    }
}
