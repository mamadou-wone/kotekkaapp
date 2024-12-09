package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.DiscountAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Discount;
import com.kotekka.app.domain.enumeration.DefaultStatus;
import com.kotekka.app.domain.enumeration.DiscountType;
import com.kotekka.app.repository.DiscountRepository;
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
 * Integration tests for the {@link DiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiscountResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DiscountType DEFAULT_TYPE = DiscountType.FIXED;
    private static final DiscountType UPDATED_TYPE = DiscountType.PERCENTAGE;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALUE = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DefaultStatus DEFAULT_STATUS = DefaultStatus.PENDING;
    private static final DefaultStatus UPDATED_STATUS = DefaultStatus.ACTIVE;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/discounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiscountMockMvc;

    private Discount discount;

    private Discount insertedDiscount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discount createEntity() {
        return new Discount()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discount createUpdatedEntity() {
        return new Discount()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        discount = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDiscount != null) {
            discountRepository.delete(insertedDiscount);
            insertedDiscount = null;
        }
    }

    @Test
    @Transactional
    void createDiscount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Discount
        var returnedDiscount = om.readValue(
            restDiscountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Discount.class
        );

        // Validate the Discount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDiscountUpdatableFieldsEquals(returnedDiscount, getPersistedDiscount(returnedDiscount));

        insertedDiscount = returnedDiscount;
    }

    @Test
    @Transactional
    void createDiscountWithExistingId() throws Exception {
        // Create the Discount with an existing ID
        discount.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        discount.setUuid(null);

        // Create the Discount, which fails.

        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        discount.setName(null);

        // Create the Discount, which fails.

        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        discount.setType(null);

        // Create the Discount, which fails.

        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        discount.setValue(null);

        // Create the Discount, which fails.

        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiscounts() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDiscount() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL_ID, discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDiscountsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        Long id = discount.getId();

        defaultDiscountFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDiscountFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDiscountFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDiscountsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where uuid equals to
        defaultDiscountFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllDiscountsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where uuid in
        defaultDiscountFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllDiscountsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where uuid is not null
        defaultDiscountFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where name equals to
        defaultDiscountFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDiscountsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where name in
        defaultDiscountFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDiscountsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where name is not null
        defaultDiscountFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where name contains
        defaultDiscountFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDiscountsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where name does not contain
        defaultDiscountFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDiscountsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where type equals to
        defaultDiscountFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where type in
        defaultDiscountFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where type is not null
        defaultDiscountFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value equals to
        defaultDiscountFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value in
        defaultDiscountFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value is not null
        defaultDiscountFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value is greater than or equal to
        defaultDiscountFiltering("value.greaterThanOrEqual=" + DEFAULT_VALUE, "value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value is less than or equal to
        defaultDiscountFiltering("value.lessThanOrEqual=" + DEFAULT_VALUE, "value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value is less than
        defaultDiscountFiltering("value.lessThan=" + UPDATED_VALUE, "value.lessThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where value is greater than
        defaultDiscountFiltering("value.greaterThan=" + SMALLER_VALUE, "value.greaterThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where startDate equals to
        defaultDiscountFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDiscountsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where startDate in
        defaultDiscountFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllDiscountsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where startDate is not null
        defaultDiscountFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where endDate equals to
        defaultDiscountFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDiscountsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where endDate in
        defaultDiscountFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllDiscountsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where endDate is not null
        defaultDiscountFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where status equals to
        defaultDiscountFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDiscountsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where status in
        defaultDiscountFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDiscountsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where status is not null
        defaultDiscountFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdBy equals to
        defaultDiscountFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdBy in
        defaultDiscountFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdBy is not null
        defaultDiscountFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdBy contains
        defaultDiscountFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdBy does not contain
        defaultDiscountFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdDate equals to
        defaultDiscountFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdDate in
        defaultDiscountFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where createdDate is not null
        defaultDiscountFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedBy equals to
        defaultDiscountFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedBy in
        defaultDiscountFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedBy is not null
        defaultDiscountFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedBy contains
        defaultDiscountFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedBy does not contain
        defaultDiscountFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedDate equals to
        defaultDiscountFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedDate in
        defaultDiscountFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDiscountsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        // Get all the discountList where lastModifiedDate is not null
        defaultDiscountFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultDiscountFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDiscountShouldBeFound(shouldBeFound);
        defaultDiscountShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscountShouldBeFound(String filter) throws Exception {
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscountShouldNotBeFound(String filter) throws Exception {
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiscount() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the discount
        Discount updatedDiscount = discountRepository.findById(discount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiscount are not directly saved in db
        em.detach(updatedDiscount);
        updatedDiscount
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDiscount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDiscount))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiscountToMatchAllProperties(updatedDiscount);
    }

    @Test
    @Transactional
    void putNonExistingDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discount.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(discount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(discount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiscountWithPatch() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the discount using partial update
        Discount partialUpdatedDiscount = new Discount();
        partialUpdatedDiscount.setId(discount.getId());

        partialUpdatedDiscount
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiscount))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiscountUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDiscount, discount), getPersistedDiscount(discount));
    }

    @Test
    @Transactional
    void fullUpdateDiscountWithPatch() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the discount using partial update
        Discount partialUpdatedDiscount = new Discount();
        partialUpdatedDiscount.setId(discount.getId());

        partialUpdatedDiscount
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiscount))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiscountUpdatableFieldsEquals(partialUpdatedDiscount, getPersistedDiscount(partialUpdatedDiscount));
    }

    @Test
    @Transactional
    void patchNonExistingDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, discount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(discount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(discount))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiscount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        discount.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(discount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiscount() throws Exception {
        // Initialize the database
        insertedDiscount = discountRepository.saveAndFlush(discount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the discount
        restDiscountMockMvc
            .perform(delete(ENTITY_API_URL_ID, discount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return discountRepository.count();
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

    protected Discount getPersistedDiscount(Discount discount) {
        return discountRepository.findById(discount.getId()).orElseThrow();
    }

    protected void assertPersistedDiscountToMatchAllProperties(Discount expectedDiscount) {
        assertDiscountAllPropertiesEquals(expectedDiscount, getPersistedDiscount(expectedDiscount));
    }

    protected void assertPersistedDiscountToMatchUpdatableProperties(Discount expectedDiscount) {
        assertDiscountAllUpdatablePropertiesEquals(expectedDiscount, getPersistedDiscount(expectedDiscount));
    }
}
