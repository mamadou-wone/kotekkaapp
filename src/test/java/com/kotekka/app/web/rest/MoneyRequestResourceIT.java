package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.MoneyRequestAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.MoneyRequest;
import com.kotekka.app.domain.enumeration.RequestStatus;
import com.kotekka.app.repository.MoneyRequestRepository;
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
 * Integration tests for the {@link MoneyRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyRequestResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.SENT;

    private static final UUID DEFAULT_OTHER_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_OTHER_HOLDER = UUID.randomUUID();

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/money-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoneyRequestRepository moneyRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoneyRequestMockMvc;

    private MoneyRequest moneyRequest;

    private MoneyRequest insertedMoneyRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyRequest createEntity() {
        return new MoneyRequest()
            .uuid(DEFAULT_UUID)
            .status(DEFAULT_STATUS)
            .otherHolder(DEFAULT_OTHER_HOLDER)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .currency(DEFAULT_CURRENCY)
            .walletHolder(DEFAULT_WALLET_HOLDER)
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
    public static MoneyRequest createUpdatedEntity() {
        return new MoneyRequest()
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .otherHolder(UPDATED_OTHER_HOLDER)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        moneyRequest = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMoneyRequest != null) {
            moneyRequestRepository.delete(insertedMoneyRequest);
            insertedMoneyRequest = null;
        }
    }

    @Test
    @Transactional
    void createMoneyRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MoneyRequest
        var returnedMoneyRequest = om.readValue(
            restMoneyRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoneyRequest.class
        );

        // Validate the MoneyRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMoneyRequestUpdatableFieldsEquals(returnedMoneyRequest, getPersistedMoneyRequest(returnedMoneyRequest));

        insertedMoneyRequest = returnedMoneyRequest;
    }

    @Test
    @Transactional
    void createMoneyRequestWithExistingId() throws Exception {
        // Create the MoneyRequest with an existing ID
        moneyRequest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyRequest)))
            .andExpect(status().isBadRequest());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moneyRequest.setUuid(null);

        // Create the MoneyRequest, which fails.

        restMoneyRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyRequest)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoneyRequests() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].otherHolder").value(hasItem(DEFAULT_OTHER_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMoneyRequest() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get the moneyRequest
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, moneyRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moneyRequest.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.otherHolder").value(DEFAULT_OTHER_HOLDER.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMoneyRequestsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        Long id = moneyRequest.getId();

        defaultMoneyRequestFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMoneyRequestFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMoneyRequestFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where uuid equals to
        defaultMoneyRequestFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where uuid in
        defaultMoneyRequestFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where uuid is not null
        defaultMoneyRequestFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where status equals to
        defaultMoneyRequestFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where status in
        defaultMoneyRequestFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where status is not null
        defaultMoneyRequestFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByOtherHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where otherHolder equals to
        defaultMoneyRequestFiltering("otherHolder.equals=" + DEFAULT_OTHER_HOLDER, "otherHolder.equals=" + UPDATED_OTHER_HOLDER);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByOtherHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where otherHolder in
        defaultMoneyRequestFiltering(
            "otherHolder.in=" + DEFAULT_OTHER_HOLDER + "," + UPDATED_OTHER_HOLDER,
            "otherHolder.in=" + UPDATED_OTHER_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByOtherHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where otherHolder is not null
        defaultMoneyRequestFiltering("otherHolder.specified=true", "otherHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount equals to
        defaultMoneyRequestFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount in
        defaultMoneyRequestFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount is not null
        defaultMoneyRequestFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount is greater than or equal to
        defaultMoneyRequestFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount is less than or equal to
        defaultMoneyRequestFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount is less than
        defaultMoneyRequestFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where amount is greater than
        defaultMoneyRequestFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where description equals to
        defaultMoneyRequestFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where description in
        defaultMoneyRequestFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where description is not null
        defaultMoneyRequestFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where description contains
        defaultMoneyRequestFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where description does not contain
        defaultMoneyRequestFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where currency equals to
        defaultMoneyRequestFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where currency in
        defaultMoneyRequestFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where currency is not null
        defaultMoneyRequestFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where currency contains
        defaultMoneyRequestFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where currency does not contain
        defaultMoneyRequestFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where walletHolder equals to
        defaultMoneyRequestFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where walletHolder in
        defaultMoneyRequestFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where walletHolder is not null
        defaultMoneyRequestFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdBy equals to
        defaultMoneyRequestFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdBy in
        defaultMoneyRequestFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdBy is not null
        defaultMoneyRequestFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdBy contains
        defaultMoneyRequestFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdBy does not contain
        defaultMoneyRequestFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdDate equals to
        defaultMoneyRequestFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdDate in
        defaultMoneyRequestFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where createdDate is not null
        defaultMoneyRequestFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedBy equals to
        defaultMoneyRequestFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedBy in
        defaultMoneyRequestFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedBy is not null
        defaultMoneyRequestFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedBy contains
        defaultMoneyRequestFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedBy does not contain
        defaultMoneyRequestFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedDate equals to
        defaultMoneyRequestFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedDate in
        defaultMoneyRequestFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMoneyRequestsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        // Get all the moneyRequestList where lastModifiedDate is not null
        defaultMoneyRequestFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultMoneyRequestFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMoneyRequestShouldBeFound(shouldBeFound);
        defaultMoneyRequestShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMoneyRequestShouldBeFound(String filter) throws Exception {
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moneyRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].otherHolder").value(hasItem(DEFAULT_OTHER_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMoneyRequestShouldNotBeFound(String filter) throws Exception {
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMoneyRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMoneyRequest() throws Exception {
        // Get the moneyRequest
        restMoneyRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoneyRequest() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyRequest
        MoneyRequest updatedMoneyRequest = moneyRequestRepository.findById(moneyRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoneyRequest are not directly saved in db
        em.detach(updatedMoneyRequest);
        updatedMoneyRequest
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .otherHolder(UPDATED_OTHER_HOLDER)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMoneyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMoneyRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMoneyRequest))
            )
            .andExpect(status().isOk());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoneyRequestToMatchAllProperties(updatedMoneyRequest);
    }

    @Test
    @Transactional
    void putNonExistingMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moneyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moneyRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoneyRequestWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyRequest using partial update
        MoneyRequest partialUpdatedMoneyRequest = new MoneyRequest();
        partialUpdatedMoneyRequest.setId(moneyRequest.getId());

        partialUpdatedMoneyRequest
            .status(UPDATED_STATUS)
            .otherHolder(UPDATED_OTHER_HOLDER)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdDate(UPDATED_CREATED_DATE);

        restMoneyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyRequest))
            )
            .andExpect(status().isOk());

        // Validate the MoneyRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoneyRequest, moneyRequest),
            getPersistedMoneyRequest(moneyRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoneyRequestWithPatch() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moneyRequest using partial update
        MoneyRequest partialUpdatedMoneyRequest = new MoneyRequest();
        partialUpdatedMoneyRequest.setId(moneyRequest.getId());

        partialUpdatedMoneyRequest
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .otherHolder(UPDATED_OTHER_HOLDER)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMoneyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoneyRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoneyRequest))
            )
            .andExpect(status().isOk());

        // Validate the MoneyRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoneyRequestUpdatableFieldsEquals(partialUpdatedMoneyRequest, getPersistedMoneyRequest(partialUpdatedMoneyRequest));
    }

    @Test
    @Transactional
    void patchNonExistingMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moneyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoneyRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moneyRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moneyRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoneyRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoneyRequest() throws Exception {
        // Initialize the database
        insertedMoneyRequest = moneyRequestRepository.saveAndFlush(moneyRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moneyRequest
        restMoneyRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, moneyRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moneyRequestRepository.count();
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

    protected MoneyRequest getPersistedMoneyRequest(MoneyRequest moneyRequest) {
        return moneyRequestRepository.findById(moneyRequest.getId()).orElseThrow();
    }

    protected void assertPersistedMoneyRequestToMatchAllProperties(MoneyRequest expectedMoneyRequest) {
        assertMoneyRequestAllPropertiesEquals(expectedMoneyRequest, getPersistedMoneyRequest(expectedMoneyRequest));
    }

    protected void assertPersistedMoneyRequestToMatchUpdatableProperties(MoneyRequest expectedMoneyRequest) {
        assertMoneyRequestAllUpdatablePropertiesEquals(expectedMoneyRequest, getPersistedMoneyRequest(expectedMoneyRequest));
    }
}
