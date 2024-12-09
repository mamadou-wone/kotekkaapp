package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.WalletAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.kotekka.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Wallet;
import com.kotekka.app.domain.enumeration.AccountLevel;
import com.kotekka.app.domain.enumeration.WalletStatus;
import com.kotekka.app.domain.enumeration.WalletType;
import com.kotekka.app.repository.WalletRepository;
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
 * Integration tests for the {@link WalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalletResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final WalletType DEFAULT_TYPE = WalletType.PRIMARY;
    private static final WalletType UPDATED_TYPE = WalletType.SAVINGS;

    private static final WalletStatus DEFAULT_STATUS = WalletStatus.PENDING;
    private static final WalletStatus UPDATED_STATUS = WalletStatus.ACTIVE;

    private static final AccountLevel DEFAULT_LEVEL = AccountLevel.ONE;
    private static final AccountLevel UPDATED_LEVEL = AccountLevel.TWO;

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_BALANCES_AS_OF = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BALANCES_AS_OF = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletMockMvc;

    private Wallet wallet;

    private Wallet insertedWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createEntity() {
        return new Wallet()
            .uuid(DEFAULT_UUID)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .level(DEFAULT_LEVEL)
            .iban(DEFAULT_IBAN)
            .currency(DEFAULT_CURRENCY)
            .balance(DEFAULT_BALANCE)
            .balancesAsOf(DEFAULT_BALANCES_AS_OF)
            .externalId(DEFAULT_EXTERNAL_ID)
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
    public static Wallet createUpdatedEntity() {
        return new Wallet()
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .iban(UPDATED_IBAN)
            .currency(UPDATED_CURRENCY)
            .balance(UPDATED_BALANCE)
            .balancesAsOf(UPDATED_BALANCES_AS_OF)
            .externalId(UPDATED_EXTERNAL_ID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        wallet = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWallet != null) {
            walletRepository.delete(insertedWallet);
            insertedWallet = null;
        }
    }

    @Test
    @Transactional
    void createWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wallet
        var returnedWallet = om.readValue(
            restWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Wallet.class
        );

        // Validate the Wallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWalletUpdatableFieldsEquals(returnedWallet, getPersistedWallet(returnedWallet));

        insertedWallet = returnedWallet;
    }

    @Test
    @Transactional
    void createWalletWithExistingId() throws Exception {
        // Create the Wallet with an existing ID
        wallet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wallet.setUuid(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWallets() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].balancesAsOf").value(hasItem(DEFAULT_BALANCES_AS_OF.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getWallet() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get the wallet
        restWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wallet.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.balancesAsOf").value(DEFAULT_BALANCES_AS_OF.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getWalletsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        Long id = wallet.getId();

        defaultWalletFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWalletFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWalletFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWalletsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where uuid equals to
        defaultWalletFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWalletsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where uuid in
        defaultWalletFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWalletsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where uuid is not null
        defaultWalletFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where type equals to
        defaultWalletFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWalletsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where type in
        defaultWalletFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWalletsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where type is not null
        defaultWalletFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where status equals to
        defaultWalletFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWalletsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where status in
        defaultWalletFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWalletsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where status is not null
        defaultWalletFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where level equals to
        defaultWalletFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllWalletsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where level in
        defaultWalletFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllWalletsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where level is not null
        defaultWalletFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByIbanIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where iban equals to
        defaultWalletFiltering("iban.equals=" + DEFAULT_IBAN, "iban.equals=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllWalletsByIbanIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where iban in
        defaultWalletFiltering("iban.in=" + DEFAULT_IBAN + "," + UPDATED_IBAN, "iban.in=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllWalletsByIbanIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where iban is not null
        defaultWalletFiltering("iban.specified=true", "iban.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByIbanContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where iban contains
        defaultWalletFiltering("iban.contains=" + DEFAULT_IBAN, "iban.contains=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllWalletsByIbanNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where iban does not contain
        defaultWalletFiltering("iban.doesNotContain=" + UPDATED_IBAN, "iban.doesNotContain=" + DEFAULT_IBAN);
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where currency equals to
        defaultWalletFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where currency in
        defaultWalletFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where currency is not null
        defaultWalletFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where currency contains
        defaultWalletFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where currency does not contain
        defaultWalletFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance equals to
        defaultWalletFiltering("balance.equals=" + DEFAULT_BALANCE, "balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance in
        defaultWalletFiltering("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE, "balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is not null
        defaultWalletFiltering("balance.specified=true", "balance.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is greater than or equal to
        defaultWalletFiltering("balance.greaterThanOrEqual=" + DEFAULT_BALANCE, "balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is less than or equal to
        defaultWalletFiltering("balance.lessThanOrEqual=" + DEFAULT_BALANCE, "balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is less than
        defaultWalletFiltering("balance.lessThan=" + UPDATED_BALANCE, "balance.lessThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is greater than
        defaultWalletFiltering("balance.greaterThan=" + SMALLER_BALANCE, "balance.greaterThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalancesAsOfIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balancesAsOf equals to
        defaultWalletFiltering("balancesAsOf.equals=" + DEFAULT_BALANCES_AS_OF, "balancesAsOf.equals=" + UPDATED_BALANCES_AS_OF);
    }

    @Test
    @Transactional
    void getAllWalletsByBalancesAsOfIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balancesAsOf in
        defaultWalletFiltering(
            "balancesAsOf.in=" + DEFAULT_BALANCES_AS_OF + "," + UPDATED_BALANCES_AS_OF,
            "balancesAsOf.in=" + UPDATED_BALANCES_AS_OF
        );
    }

    @Test
    @Transactional
    void getAllWalletsByBalancesAsOfIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balancesAsOf is not null
        defaultWalletFiltering("balancesAsOf.specified=true", "balancesAsOf.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where externalId equals to
        defaultWalletFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletsByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where externalId in
        defaultWalletFiltering("externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID, "externalId.in=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletsByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where externalId is not null
        defaultWalletFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where externalId contains
        defaultWalletFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletsByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where externalId does not contain
        defaultWalletFiltering("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID, "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where walletHolder equals to
        defaultWalletFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllWalletsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where walletHolder in
        defaultWalletFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllWalletsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where walletHolder is not null
        defaultWalletFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdBy equals to
        defaultWalletFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdBy in
        defaultWalletFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdBy is not null
        defaultWalletFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdBy contains
        defaultWalletFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdBy does not contain
        defaultWalletFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdDate equals to
        defaultWalletFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdDate in
        defaultWalletFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where createdDate is not null
        defaultWalletFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedBy equals to
        defaultWalletFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedBy in
        defaultWalletFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedBy is not null
        defaultWalletFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedBy contains
        defaultWalletFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedBy does not contain
        defaultWalletFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedDate equals to
        defaultWalletFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedDate in
        defaultWalletFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        // Get all the walletList where lastModifiedDate is not null
        defaultWalletFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultWalletFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWalletShouldBeFound(shouldBeFound);
        defaultWalletShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWalletShouldBeFound(String filter) throws Exception {
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))))
            .andExpect(jsonPath("$.[*].balancesAsOf").value(hasItem(DEFAULT_BALANCES_AS_OF.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWalletShouldNotBeFound(String filter) throws Exception {
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWallet() throws Exception {
        // Get the wallet
        restWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWallet() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findById(wallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWallet are not directly saved in db
        em.detach(updatedWallet);
        updatedWallet
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .iban(UPDATED_IBAN)
            .currency(UPDATED_CURRENCY)
            .balance(UPDATED_BALANCE)
            .balancesAsOf(UPDATED_BALANCES_AS_OF)
            .externalId(UPDATED_EXTERNAL_ID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletToMatchAllProperties(updatedWallet);
    }

    @Test
    @Transactional
    void putNonExistingWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(put(ENTITY_API_URL_ID, wallet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet
            .type(UPDATED_TYPE)
            .iban(UPDATED_IBAN)
            .currency(UPDATED_CURRENCY)
            .balance(UPDATED_BALANCE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWallet, wallet), getPersistedWallet(wallet));
    }

    @Test
    @Transactional
    void fullUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .level(UPDATED_LEVEL)
            .iban(UPDATED_IBAN)
            .currency(UPDATED_CURRENCY)
            .balance(UPDATED_BALANCE)
            .balancesAsOf(UPDATED_BALANCES_AS_OF)
            .externalId(UPDATED_EXTERNAL_ID)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletUpdatableFieldsEquals(partialUpdatedWallet, getPersistedWallet(partialUpdatedWallet));
    }

    @Test
    @Transactional
    void patchNonExistingWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wallet.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWallet() throws Exception {
        // Initialize the database
        insertedWallet = walletRepository.saveAndFlush(wallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wallet
        restWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, wallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletRepository.count();
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

    protected Wallet getPersistedWallet(Wallet wallet) {
        return walletRepository.findById(wallet.getId()).orElseThrow();
    }

    protected void assertPersistedWalletToMatchAllProperties(Wallet expectedWallet) {
        assertWalletAllPropertiesEquals(expectedWallet, getPersistedWallet(expectedWallet));
    }

    protected void assertPersistedWalletToMatchUpdatableProperties(Wallet expectedWallet) {
        assertWalletAllUpdatablePropertiesEquals(expectedWallet, getPersistedWallet(expectedWallet));
    }
}
