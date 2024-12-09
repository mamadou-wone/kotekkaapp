package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.TransactionAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Transaction;
import com.kotekka.app.domain.enumeration.CounterpartyType;
import com.kotekka.app.domain.enumeration.Direction;
import com.kotekka.app.domain.enumeration.TransactionStatus;
import com.kotekka.app.domain.enumeration.TransactionType;
import com.kotekka.app.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final TransactionType DEFAULT_TYPE = TransactionType.PAY;
    private static final TransactionType UPDATED_TYPE = TransactionType.RECEIVE;

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.PENDING;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.TO_CONFIRM;

    private static final Direction DEFAULT_DIRECTION = Direction.DEBIT;
    private static final Direction UPDATED_DIRECTION = Direction.CREDIT;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FEE = new BigDecimal(2);
    private static final BigDecimal SMALLER_FEE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COMMISSION = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISSION = new BigDecimal(2);
    private static final BigDecimal SMALLER_COMMISSION = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final CounterpartyType DEFAULT_COUNTERPARTY_TYPE = CounterpartyType.WALLET;
    private static final CounterpartyType UPDATED_COUNTERPARTY_TYPE = CounterpartyType.CARD;

    private static final String DEFAULT_COUNTERPARTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_COUNTERPARTY_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ENTRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENTRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EFFECTIVE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final UUID DEFAULT_PARENT = UUID.randomUUID();
    private static final UUID UPDATED_PARENT = UUID.randomUUID();

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PARTNER_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_PARTNER_TOKEN = "BBBBBBBBBB";

    private static final UUID DEFAULT_WALLET = UUID.randomUUID();
    private static final UUID UPDATED_WALLET = UUID.randomUUID();

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    private Transaction insertedTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity() {
        return new Transaction()
            .uuid(DEFAULT_UUID)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .direction(DEFAULT_DIRECTION)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .fee(DEFAULT_FEE)
            .commission(DEFAULT_COMMISSION)
            .currency(DEFAULT_CURRENCY)
            .counterpartyType(DEFAULT_COUNTERPARTY_TYPE)
            .counterpartyId(DEFAULT_COUNTERPARTY_ID)
            .entryDate(DEFAULT_ENTRY_DATE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .parent(DEFAULT_PARENT)
            .reference(DEFAULT_REFERENCE)
            .externalId(DEFAULT_EXTERNAL_ID)
            .partnerToken(DEFAULT_PARTNER_TOKEN)
            .wallet(DEFAULT_WALLET)
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
    public static Transaction createUpdatedEntity() {
        return new Transaction()
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .direction(UPDATED_DIRECTION)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .fee(UPDATED_FEE)
            .commission(UPDATED_COMMISSION)
            .currency(UPDATED_CURRENCY)
            .counterpartyType(UPDATED_COUNTERPARTY_TYPE)
            .counterpartyId(UPDATED_COUNTERPARTY_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .parent(UPDATED_PARENT)
            .reference(UPDATED_REFERENCE)
            .externalId(UPDATED_EXTERNAL_ID)
            .partnerToken(UPDATED_PARTNER_TOKEN)
            .wallet(UPDATED_WALLET)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransaction != null) {
            transactionRepository.delete(insertedTransaction);
            insertedTransaction = null;
        }
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transaction
        var returnedTransaction = om.readValue(
            restTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transaction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Transaction.class
        );

        // Validate the Transaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionUpdatableFieldsEquals(returnedTransaction, getPersistedTransaction(returnedTransaction));

        insertedTransaction = returnedTransaction;
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transaction)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transaction.setUuid(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transaction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(sameNumber(DEFAULT_FEE))))
            .andExpect(jsonPath("$.[*].commission").value(hasItem(sameNumber(DEFAULT_COMMISSION))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].counterpartyType").value(hasItem(DEFAULT_COUNTERPARTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].counterpartyId").value(hasItem(DEFAULT_COUNTERPARTY_ID)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].partnerToken").value(hasItem(DEFAULT_PARTNER_TOKEN)))
            .andExpect(jsonPath("$.[*].wallet").value(hasItem(DEFAULT_WALLET.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fee").value(sameNumber(DEFAULT_FEE)))
            .andExpect(jsonPath("$.commission").value(sameNumber(DEFAULT_COMMISSION)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.counterpartyType").value(DEFAULT_COUNTERPARTY_TYPE.toString()))
            .andExpect(jsonPath("$.counterpartyId").value(DEFAULT_COUNTERPARTY_ID))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.partnerToken").value(DEFAULT_PARTNER_TOKEN))
            .andExpect(jsonPath("$.wallet").value(DEFAULT_WALLET.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        Long id = transaction.getId();

        defaultTransactionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransactionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransactionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where uuid equals to
        defaultTransactionFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllTransactionsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where uuid in
        defaultTransactionFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllTransactionsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where uuid is not null
        defaultTransactionFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where type equals to
        defaultTransactionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where type in
        defaultTransactionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where type is not null
        defaultTransactionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where status equals to
        defaultTransactionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransactionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where status in
        defaultTransactionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransactionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where status is not null
        defaultTransactionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where direction equals to
        defaultTransactionFiltering("direction.equals=" + DEFAULT_DIRECTION, "direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where direction in
        defaultTransactionFiltering("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION, "direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where direction is not null
        defaultTransactionFiltering("direction.specified=true", "direction.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount equals to
        defaultTransactionFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount in
        defaultTransactionFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is not null
        defaultTransactionFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is greater than or equal to
        defaultTransactionFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is less than or equal to
        defaultTransactionFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is less than
        defaultTransactionFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is greater than
        defaultTransactionFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description equals to
        defaultTransactionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description in
        defaultTransactionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description is not null
        defaultTransactionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description contains
        defaultTransactionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description does not contain
        defaultTransactionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee equals to
        defaultTransactionFiltering("fee.equals=" + DEFAULT_FEE, "fee.equals=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee in
        defaultTransactionFiltering("fee.in=" + DEFAULT_FEE + "," + UPDATED_FEE, "fee.in=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee is not null
        defaultTransactionFiltering("fee.specified=true", "fee.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee is greater than or equal to
        defaultTransactionFiltering("fee.greaterThanOrEqual=" + DEFAULT_FEE, "fee.greaterThanOrEqual=" + UPDATED_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee is less than or equal to
        defaultTransactionFiltering("fee.lessThanOrEqual=" + DEFAULT_FEE, "fee.lessThanOrEqual=" + SMALLER_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee is less than
        defaultTransactionFiltering("fee.lessThan=" + UPDATED_FEE, "fee.lessThan=" + DEFAULT_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where fee is greater than
        defaultTransactionFiltering("fee.greaterThan=" + SMALLER_FEE, "fee.greaterThan=" + DEFAULT_FEE);
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission equals to
        defaultTransactionFiltering("commission.equals=" + DEFAULT_COMMISSION, "commission.equals=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission in
        defaultTransactionFiltering(
            "commission.in=" + DEFAULT_COMMISSION + "," + UPDATED_COMMISSION,
            "commission.in=" + UPDATED_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission is not null
        defaultTransactionFiltering("commission.specified=true", "commission.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission is greater than or equal to
        defaultTransactionFiltering(
            "commission.greaterThanOrEqual=" + DEFAULT_COMMISSION,
            "commission.greaterThanOrEqual=" + UPDATED_COMMISSION
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission is less than or equal to
        defaultTransactionFiltering("commission.lessThanOrEqual=" + DEFAULT_COMMISSION, "commission.lessThanOrEqual=" + SMALLER_COMMISSION);
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission is less than
        defaultTransactionFiltering("commission.lessThan=" + UPDATED_COMMISSION, "commission.lessThan=" + DEFAULT_COMMISSION);
    }

    @Test
    @Transactional
    void getAllTransactionsByCommissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where commission is greater than
        defaultTransactionFiltering("commission.greaterThan=" + SMALLER_COMMISSION, "commission.greaterThan=" + DEFAULT_COMMISSION);
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where currency equals to
        defaultTransactionFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where currency in
        defaultTransactionFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where currency is not null
        defaultTransactionFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where currency contains
        defaultTransactionFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where currency does not contain
        defaultTransactionFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyType equals to
        defaultTransactionFiltering(
            "counterpartyType.equals=" + DEFAULT_COUNTERPARTY_TYPE,
            "counterpartyType.equals=" + UPDATED_COUNTERPARTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyType in
        defaultTransactionFiltering(
            "counterpartyType.in=" + DEFAULT_COUNTERPARTY_TYPE + "," + UPDATED_COUNTERPARTY_TYPE,
            "counterpartyType.in=" + UPDATED_COUNTERPARTY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyType is not null
        defaultTransactionFiltering("counterpartyType.specified=true", "counterpartyType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyId equals to
        defaultTransactionFiltering("counterpartyId.equals=" + DEFAULT_COUNTERPARTY_ID, "counterpartyId.equals=" + UPDATED_COUNTERPARTY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyId in
        defaultTransactionFiltering(
            "counterpartyId.in=" + DEFAULT_COUNTERPARTY_ID + "," + UPDATED_COUNTERPARTY_ID,
            "counterpartyId.in=" + UPDATED_COUNTERPARTY_ID
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyId is not null
        defaultTransactionFiltering("counterpartyId.specified=true", "counterpartyId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyIdContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyId contains
        defaultTransactionFiltering(
            "counterpartyId.contains=" + DEFAULT_COUNTERPARTY_ID,
            "counterpartyId.contains=" + UPDATED_COUNTERPARTY_ID
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCounterpartyIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where counterpartyId does not contain
        defaultTransactionFiltering(
            "counterpartyId.doesNotContain=" + UPDATED_COUNTERPARTY_ID,
            "counterpartyId.doesNotContain=" + DEFAULT_COUNTERPARTY_ID
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate equals to
        defaultTransactionFiltering("entryDate.equals=" + DEFAULT_ENTRY_DATE, "entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate in
        defaultTransactionFiltering("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE, "entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate is not null
        defaultTransactionFiltering("entryDate.specified=true", "entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate is greater than or equal to
        defaultTransactionFiltering(
            "entryDate.greaterThanOrEqual=" + DEFAULT_ENTRY_DATE,
            "entryDate.greaterThanOrEqual=" + UPDATED_ENTRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate is less than or equal to
        defaultTransactionFiltering("entryDate.lessThanOrEqual=" + DEFAULT_ENTRY_DATE, "entryDate.lessThanOrEqual=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate is less than
        defaultTransactionFiltering("entryDate.lessThan=" + UPDATED_ENTRY_DATE, "entryDate.lessThan=" + DEFAULT_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEntryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where entryDate is greater than
        defaultTransactionFiltering("entryDate.greaterThan=" + SMALLER_ENTRY_DATE, "entryDate.greaterThan=" + DEFAULT_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate equals to
        defaultTransactionFiltering("effectiveDate.equals=" + DEFAULT_EFFECTIVE_DATE, "effectiveDate.equals=" + UPDATED_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate in
        defaultTransactionFiltering(
            "effectiveDate.in=" + DEFAULT_EFFECTIVE_DATE + "," + UPDATED_EFFECTIVE_DATE,
            "effectiveDate.in=" + UPDATED_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate is not null
        defaultTransactionFiltering("effectiveDate.specified=true", "effectiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate is greater than or equal to
        defaultTransactionFiltering(
            "effectiveDate.greaterThanOrEqual=" + DEFAULT_EFFECTIVE_DATE,
            "effectiveDate.greaterThanOrEqual=" + UPDATED_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate is less than or equal to
        defaultTransactionFiltering(
            "effectiveDate.lessThanOrEqual=" + DEFAULT_EFFECTIVE_DATE,
            "effectiveDate.lessThanOrEqual=" + SMALLER_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate is less than
        defaultTransactionFiltering("effectiveDate.lessThan=" + UPDATED_EFFECTIVE_DATE, "effectiveDate.lessThan=" + DEFAULT_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByEffectiveDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where effectiveDate is greater than
        defaultTransactionFiltering(
            "effectiveDate.greaterThan=" + SMALLER_EFFECTIVE_DATE,
            "effectiveDate.greaterThan=" + DEFAULT_EFFECTIVE_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where startTime equals to
        defaultTransactionFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTransactionsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where startTime in
        defaultTransactionFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTransactionsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where startTime is not null
        defaultTransactionFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where endTime equals to
        defaultTransactionFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTransactionsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where endTime in
        defaultTransactionFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTransactionsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where endTime is not null
        defaultTransactionFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where parent equals to
        defaultTransactionFiltering("parent.equals=" + DEFAULT_PARENT, "parent.equals=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    void getAllTransactionsByParentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where parent in
        defaultTransactionFiltering("parent.in=" + DEFAULT_PARENT + "," + UPDATED_PARENT, "parent.in=" + UPDATED_PARENT);
    }

    @Test
    @Transactional
    void getAllTransactionsByParentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where parent is not null
        defaultTransactionFiltering("parent.specified=true", "parent.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where reference equals to
        defaultTransactionFiltering("reference.equals=" + DEFAULT_REFERENCE, "reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionsByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where reference in
        defaultTransactionFiltering("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE, "reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionsByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where reference is not null
        defaultTransactionFiltering("reference.specified=true", "reference.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByReferenceContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where reference contains
        defaultTransactionFiltering("reference.contains=" + DEFAULT_REFERENCE, "reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionsByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where reference does not contain
        defaultTransactionFiltering("reference.doesNotContain=" + UPDATED_REFERENCE, "reference.doesNotContain=" + DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionsByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where externalId equals to
        defaultTransactionFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where externalId in
        defaultTransactionFiltering(
            "externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID,
            "externalId.in=" + UPDATED_EXTERNAL_ID
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where externalId is not null
        defaultTransactionFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where externalId contains
        defaultTransactionFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where externalId does not contain
        defaultTransactionFiltering("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID, "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByPartnerTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where partnerToken equals to
        defaultTransactionFiltering("partnerToken.equals=" + DEFAULT_PARTNER_TOKEN, "partnerToken.equals=" + UPDATED_PARTNER_TOKEN);
    }

    @Test
    @Transactional
    void getAllTransactionsByPartnerTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where partnerToken in
        defaultTransactionFiltering(
            "partnerToken.in=" + DEFAULT_PARTNER_TOKEN + "," + UPDATED_PARTNER_TOKEN,
            "partnerToken.in=" + UPDATED_PARTNER_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByPartnerTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where partnerToken is not null
        defaultTransactionFiltering("partnerToken.specified=true", "partnerToken.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByPartnerTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where partnerToken contains
        defaultTransactionFiltering("partnerToken.contains=" + DEFAULT_PARTNER_TOKEN, "partnerToken.contains=" + UPDATED_PARTNER_TOKEN);
    }

    @Test
    @Transactional
    void getAllTransactionsByPartnerTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where partnerToken does not contain
        defaultTransactionFiltering(
            "partnerToken.doesNotContain=" + UPDATED_PARTNER_TOKEN,
            "partnerToken.doesNotContain=" + DEFAULT_PARTNER_TOKEN
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where wallet equals to
        defaultTransactionFiltering("wallet.equals=" + DEFAULT_WALLET, "wallet.equals=" + UPDATED_WALLET);
    }

    @Test
    @Transactional
    void getAllTransactionsByWalletIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where wallet in
        defaultTransactionFiltering("wallet.in=" + DEFAULT_WALLET + "," + UPDATED_WALLET, "wallet.in=" + UPDATED_WALLET);
    }

    @Test
    @Transactional
    void getAllTransactionsByWalletIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where wallet is not null
        defaultTransactionFiltering("wallet.specified=true", "wallet.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy equals to
        defaultTransactionFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy in
        defaultTransactionFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy is not null
        defaultTransactionFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy contains
        defaultTransactionFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy does not contain
        defaultTransactionFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate equals to
        defaultTransactionFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate in
        defaultTransactionFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate is not null
        defaultTransactionFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy equals to
        defaultTransactionFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy in
        defaultTransactionFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy is not null
        defaultTransactionFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy contains
        defaultTransactionFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy does not contain
        defaultTransactionFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate equals to
        defaultTransactionFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate in
        defaultTransactionFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransactionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate is not null
        defaultTransactionFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultTransactionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransactionShouldBeFound(shouldBeFound);
        defaultTransactionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(sameNumber(DEFAULT_FEE))))
            .andExpect(jsonPath("$.[*].commission").value(hasItem(sameNumber(DEFAULT_COMMISSION))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].counterpartyType").value(hasItem(DEFAULT_COUNTERPARTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].counterpartyId").value(hasItem(DEFAULT_COUNTERPARTY_ID)))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].partnerToken").value(hasItem(DEFAULT_PARTNER_TOKEN)))
            .andExpect(jsonPath("$.[*].wallet").value(hasItem(DEFAULT_WALLET.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransaction() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .direction(UPDATED_DIRECTION)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .fee(UPDATED_FEE)
            .commission(UPDATED_COMMISSION)
            .currency(UPDATED_CURRENCY)
            .counterpartyType(UPDATED_COUNTERPARTY_TYPE)
            .counterpartyId(UPDATED_COUNTERPARTY_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .parent(UPDATED_PARENT)
            .reference(UPDATED_REFERENCE)
            .externalId(UPDATED_EXTERNAL_ID)
            .partnerToken(UPDATED_PARTNER_TOKEN)
            .wallet(UPDATED_WALLET)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionToMatchAllProperties(updatedTransaction);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .amount(UPDATED_AMOUNT)
            .commission(UPDATED_COMMISSION)
            .entryDate(UPDATED_ENTRY_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .parent(UPDATED_PARENT)
            .reference(UPDATED_REFERENCE)
            .externalId(UPDATED_EXTERNAL_ID)
            .partnerToken(UPDATED_PARTNER_TOKEN)
            .wallet(UPDATED_WALLET)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransaction, transaction),
            getPersistedTransaction(transaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .direction(UPDATED_DIRECTION)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .fee(UPDATED_FEE)
            .commission(UPDATED_COMMISSION)
            .currency(UPDATED_CURRENCY)
            .counterpartyType(UPDATED_COUNTERPARTY_TYPE)
            .counterpartyId(UPDATED_COUNTERPARTY_ID)
            .entryDate(UPDATED_ENTRY_DATE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .parent(UPDATED_PARENT)
            .reference(UPDATED_REFERENCE)
            .externalId(UPDATED_EXTERNAL_ID)
            .partnerToken(UPDATED_PARTNER_TOKEN)
            .wallet(UPDATED_WALLET)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionUpdatableFieldsEquals(partialUpdatedTransaction, getPersistedTransaction(partialUpdatedTransaction));
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        insertedTransaction = transactionRepository.saveAndFlush(transaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionRepository.count();
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

    protected Transaction getPersistedTransaction(Transaction transaction) {
        return transactionRepository.findById(transaction.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionToMatchAllProperties(Transaction expectedTransaction) {
        assertTransactionAllPropertiesEquals(expectedTransaction, getPersistedTransaction(expectedTransaction));
    }

    protected void assertPersistedTransactionToMatchUpdatableProperties(Transaction expectedTransaction) {
        assertTransactionAllUpdatablePropertiesEquals(expectedTransaction, getPersistedTransaction(expectedTransaction));
    }
}
