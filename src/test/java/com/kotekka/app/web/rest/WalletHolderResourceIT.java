package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.WalletHolderAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.User;
import com.kotekka.app.domain.WalletHolder;
import com.kotekka.app.domain.enumeration.AccountStatus;
import com.kotekka.app.domain.enumeration.AccountType;
import com.kotekka.app.domain.enumeration.LoginStatus;
import com.kotekka.app.domain.enumeration.Network;
import com.kotekka.app.domain.enumeration.OnboardingStatus;
import com.kotekka.app.domain.enumeration.Sex;
import com.kotekka.app.repository.UserRepository;
import com.kotekka.app.repository.WalletHolderRepository;
import com.kotekka.app.service.WalletHolderService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link WalletHolderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WalletHolderResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final AccountType DEFAULT_TYPE = AccountType.CUSTOMER;
    private static final AccountType UPDATED_TYPE = AccountType.MERCHANT;

    private static final AccountStatus DEFAULT_STATUS = AccountStatus.PENDING;
    private static final AccountStatus UPDATED_STATUS = AccountStatus.ACTIVE;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Network DEFAULT_NETWORK = Network.IAM;
    private static final Network UPDATED_NETWORK = Network.INWI;

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final OnboardingStatus DEFAULT_ONBOARDING = OnboardingStatus.PENDING;
    private static final OnboardingStatus UPDATED_ONBOARDING = OnboardingStatus.PERSONAL_INFO;

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Sex DEFAULT_SEX = Sex.M;
    private static final Sex UPDATED_SEX = Sex.F;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LoginStatus DEFAULT_LOGIN_STATUS = LoginStatus.LOGIN_LOCKED;
    private static final LoginStatus UPDATED_LOGIN_STATUS = LoginStatus.LOGIN_UNLOCKED;

    private static final String ENTITY_API_URL = "/api/wallet-holders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletHolderRepository walletHolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private WalletHolderRepository walletHolderRepositoryMock;

    @Mock
    private WalletHolderService walletHolderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletHolderMockMvc;

    private WalletHolder walletHolder;

    private WalletHolder insertedWalletHolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletHolder createEntity() {
        return new WalletHolder()
            .uuid(DEFAULT_UUID)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .network(DEFAULT_NETWORK)
            .tag(DEFAULT_TAG)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .postalCode(DEFAULT_POSTAL_CODE)
            .onboarding(DEFAULT_ONBOARDING)
            .externalId(DEFAULT_EXTERNAL_ID)
            .email(DEFAULT_EMAIL)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .sex(DEFAULT_SEX)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .loginStatus(DEFAULT_LOGIN_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletHolder createUpdatedEntity() {
        return new WalletHolder()
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .network(UPDATED_NETWORK)
            .tag(UPDATED_TAG)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .onboarding(UPDATED_ONBOARDING)
            .externalId(UPDATED_EXTERNAL_ID)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .sex(UPDATED_SEX)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .loginStatus(UPDATED_LOGIN_STATUS);
    }

    @BeforeEach
    public void initTest() {
        walletHolder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWalletHolder != null) {
            walletHolderRepository.delete(insertedWalletHolder);
            insertedWalletHolder = null;
        }
    }

    @Test
    @Transactional
    void createWalletHolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WalletHolder
        var returnedWalletHolder = om.readValue(
            restWalletHolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletHolder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WalletHolder.class
        );

        // Validate the WalletHolder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWalletHolderUpdatableFieldsEquals(returnedWalletHolder, getPersistedWalletHolder(returnedWalletHolder));

        insertedWalletHolder = returnedWalletHolder;
    }

    @Test
    @Transactional
    void createWalletHolderWithExistingId() throws Exception {
        // Create the WalletHolder with an existing ID
        walletHolder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletHolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletHolder)))
            .andExpect(status().isBadRequest());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        walletHolder.setUuid(null);

        // Create the WalletHolder, which fails.

        restWalletHolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletHolder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWalletHolders() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletHolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].onboarding").value(hasItem(DEFAULT_ONBOARDING.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].loginStatus").value(hasItem(DEFAULT_LOGIN_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletHoldersWithEagerRelationshipsIsEnabled() throws Exception {
        when(walletHolderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletHolderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(walletHolderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletHoldersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(walletHolderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletHolderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(walletHolderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWalletHolder() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get the walletHolder
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL_ID, walletHolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walletHolder.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.network").value(DEFAULT_NETWORK.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.onboarding").value(DEFAULT_ONBOARDING.toString()))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.loginStatus").value(DEFAULT_LOGIN_STATUS.toString()));
    }

    @Test
    @Transactional
    void getWalletHoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        Long id = walletHolder.getId();

        defaultWalletHolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWalletHolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWalletHolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where uuid equals to
        defaultWalletHolderFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where uuid in
        defaultWalletHolderFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where uuid is not null
        defaultWalletHolderFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where type equals to
        defaultWalletHolderFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where type in
        defaultWalletHolderFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where type is not null
        defaultWalletHolderFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where status equals to
        defaultWalletHolderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where status in
        defaultWalletHolderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where status is not null
        defaultWalletHolderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where phoneNumber equals to
        defaultWalletHolderFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where phoneNumber in
        defaultWalletHolderFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where phoneNumber is not null
        defaultWalletHolderFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where phoneNumber contains
        defaultWalletHolderFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where phoneNumber does not contain
        defaultWalletHolderFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByNetworkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where network equals to
        defaultWalletHolderFiltering("network.equals=" + DEFAULT_NETWORK, "network.equals=" + UPDATED_NETWORK);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByNetworkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where network in
        defaultWalletHolderFiltering("network.in=" + DEFAULT_NETWORK + "," + UPDATED_NETWORK, "network.in=" + UPDATED_NETWORK);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByNetworkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where network is not null
        defaultWalletHolderFiltering("network.specified=true", "network.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where tag equals to
        defaultWalletHolderFiltering("tag.equals=" + DEFAULT_TAG, "tag.equals=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where tag in
        defaultWalletHolderFiltering("tag.in=" + DEFAULT_TAG + "," + UPDATED_TAG, "tag.in=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where tag is not null
        defaultWalletHolderFiltering("tag.specified=true", "tag.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTagContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where tag contains
        defaultWalletHolderFiltering("tag.contains=" + DEFAULT_TAG, "tag.contains=" + UPDATED_TAG);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByTagNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where tag does not contain
        defaultWalletHolderFiltering("tag.doesNotContain=" + UPDATED_TAG, "tag.doesNotContain=" + DEFAULT_TAG);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where firstName equals to
        defaultWalletHolderFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where firstName in
        defaultWalletHolderFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where firstName is not null
        defaultWalletHolderFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where firstName contains
        defaultWalletHolderFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where firstName does not contain
        defaultWalletHolderFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastName equals to
        defaultWalletHolderFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastName in
        defaultWalletHolderFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastName is not null
        defaultWalletHolderFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastName contains
        defaultWalletHolderFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastName does not contain
        defaultWalletHolderFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where address equals to
        defaultWalletHolderFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where address in
        defaultWalletHolderFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where address is not null
        defaultWalletHolderFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where address contains
        defaultWalletHolderFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where address does not contain
        defaultWalletHolderFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where city equals to
        defaultWalletHolderFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where city in
        defaultWalletHolderFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where city is not null
        defaultWalletHolderFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where city contains
        defaultWalletHolderFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where city does not contain
        defaultWalletHolderFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where country equals to
        defaultWalletHolderFiltering("country.equals=" + DEFAULT_COUNTRY, "country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where country in
        defaultWalletHolderFiltering("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY, "country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where country is not null
        defaultWalletHolderFiltering("country.specified=true", "country.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCountryContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where country contains
        defaultWalletHolderFiltering("country.contains=" + DEFAULT_COUNTRY, "country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where country does not contain
        defaultWalletHolderFiltering("country.doesNotContain=" + UPDATED_COUNTRY, "country.doesNotContain=" + DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where postalCode equals to
        defaultWalletHolderFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where postalCode in
        defaultWalletHolderFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where postalCode is not null
        defaultWalletHolderFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where postalCode contains
        defaultWalletHolderFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where postalCode does not contain
        defaultWalletHolderFiltering(
            "postalCode.doesNotContain=" + UPDATED_POSTAL_CODE,
            "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByOnboardingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where onboarding equals to
        defaultWalletHolderFiltering("onboarding.equals=" + DEFAULT_ONBOARDING, "onboarding.equals=" + UPDATED_ONBOARDING);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByOnboardingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where onboarding in
        defaultWalletHolderFiltering(
            "onboarding.in=" + DEFAULT_ONBOARDING + "," + UPDATED_ONBOARDING,
            "onboarding.in=" + UPDATED_ONBOARDING
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByOnboardingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where onboarding is not null
        defaultWalletHolderFiltering("onboarding.specified=true", "onboarding.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where externalId equals to
        defaultWalletHolderFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where externalId in
        defaultWalletHolderFiltering(
            "externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID,
            "externalId.in=" + UPDATED_EXTERNAL_ID
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where externalId is not null
        defaultWalletHolderFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where externalId contains
        defaultWalletHolderFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where externalId does not contain
        defaultWalletHolderFiltering(
            "externalId.doesNotContain=" + UPDATED_EXTERNAL_ID,
            "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where email equals to
        defaultWalletHolderFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where email in
        defaultWalletHolderFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where email is not null
        defaultWalletHolderFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where email contains
        defaultWalletHolderFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where email does not contain
        defaultWalletHolderFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth equals to
        defaultWalletHolderFiltering("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH, "dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth in
        defaultWalletHolderFiltering(
            "dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH,
            "dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth is not null
        defaultWalletHolderFiltering("dateOfBirth.specified=true", "dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth is greater than or equal to
        defaultWalletHolderFiltering(
            "dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth is less than or equal to
        defaultWalletHolderFiltering(
            "dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH,
            "dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth is less than
        defaultWalletHolderFiltering("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH, "dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where dateOfBirth is greater than
        defaultWalletHolderFiltering(
            "dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH,
            "dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where sex equals to
        defaultWalletHolderFiltering("sex.equals=" + DEFAULT_SEX, "sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllWalletHoldersBySexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where sex in
        defaultWalletHolderFiltering("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX, "sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllWalletHoldersBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where sex is not null
        defaultWalletHolderFiltering("sex.specified=true", "sex.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdBy equals to
        defaultWalletHolderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdBy in
        defaultWalletHolderFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdBy is not null
        defaultWalletHolderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdBy contains
        defaultWalletHolderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdBy does not contain
        defaultWalletHolderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdDate equals to
        defaultWalletHolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdDate in
        defaultWalletHolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where createdDate is not null
        defaultWalletHolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedBy equals to
        defaultWalletHolderFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedBy in
        defaultWalletHolderFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedBy is not null
        defaultWalletHolderFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedBy contains
        defaultWalletHolderFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedBy does not contain
        defaultWalletHolderFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedDate equals to
        defaultWalletHolderFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedDate in
        defaultWalletHolderFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where lastModifiedDate is not null
        defaultWalletHolderFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLoginStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where loginStatus equals to
        defaultWalletHolderFiltering("loginStatus.equals=" + DEFAULT_LOGIN_STATUS, "loginStatus.equals=" + UPDATED_LOGIN_STATUS);
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLoginStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where loginStatus in
        defaultWalletHolderFiltering(
            "loginStatus.in=" + DEFAULT_LOGIN_STATUS + "," + UPDATED_LOGIN_STATUS,
            "loginStatus.in=" + UPDATED_LOGIN_STATUS
        );
    }

    @Test
    @Transactional
    void getAllWalletHoldersByLoginStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        // Get all the walletHolderList where loginStatus is not null
        defaultWalletHolderFiltering("loginStatus.specified=true", "loginStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletHoldersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            walletHolderRepository.saveAndFlush(walletHolder);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        walletHolder.setUser(user);
        walletHolderRepository.saveAndFlush(walletHolder);
        Long userId = user.getId();
        // Get all the walletHolderList where user equals to userId
        defaultWalletHolderShouldBeFound("userId.equals=" + userId);

        // Get all the walletHolderList where user equals to (userId + 1)
        defaultWalletHolderShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultWalletHolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWalletHolderShouldBeFound(shouldBeFound);
        defaultWalletHolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWalletHolderShouldBeFound(String filter) throws Exception {
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletHolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].network").value(hasItem(DEFAULT_NETWORK.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].onboarding").value(hasItem(DEFAULT_ONBOARDING.toString())))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].loginStatus").value(hasItem(DEFAULT_LOGIN_STATUS.toString())));

        // Check, that the count call also returns 1
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWalletHolderShouldNotBeFound(String filter) throws Exception {
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWalletHolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWalletHolder() throws Exception {
        // Get the walletHolder
        restWalletHolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWalletHolder() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletHolder
        WalletHolder updatedWalletHolder = walletHolderRepository.findById(walletHolder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWalletHolder are not directly saved in db
        em.detach(updatedWalletHolder);
        updatedWalletHolder
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .network(UPDATED_NETWORK)
            .tag(UPDATED_TAG)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .onboarding(UPDATED_ONBOARDING)
            .externalId(UPDATED_EXTERNAL_ID)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .sex(UPDATED_SEX)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .loginStatus(UPDATED_LOGIN_STATUS);

        restWalletHolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWalletHolder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWalletHolder))
            )
            .andExpect(status().isOk());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletHolderToMatchAllProperties(updatedWalletHolder);
    }

    @Test
    @Transactional
    void putNonExistingWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletHolder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletHolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletHolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletHolder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletHolderWithPatch() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletHolder using partial update
        WalletHolder partialUpdatedWalletHolder = new WalletHolder();
        partialUpdatedWalletHolder.setId(walletHolder.getId());

        partialUpdatedWalletHolder
            .type(UPDATED_TYPE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .city(UPDATED_CITY)
            .postalCode(UPDATED_POSTAL_CODE)
            .externalId(UPDATED_EXTERNAL_ID)
            .email(UPDATED_EMAIL)
            .sex(UPDATED_SEX)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .loginStatus(UPDATED_LOGIN_STATUS);

        restWalletHolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletHolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletHolder))
            )
            .andExpect(status().isOk());

        // Validate the WalletHolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletHolderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWalletHolder, walletHolder),
            getPersistedWalletHolder(walletHolder)
        );
    }

    @Test
    @Transactional
    void fullUpdateWalletHolderWithPatch() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletHolder using partial update
        WalletHolder partialUpdatedWalletHolder = new WalletHolder();
        partialUpdatedWalletHolder.setId(walletHolder.getId());

        partialUpdatedWalletHolder
            .uuid(UPDATED_UUID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .network(UPDATED_NETWORK)
            .tag(UPDATED_TAG)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE)
            .onboarding(UPDATED_ONBOARDING)
            .externalId(UPDATED_EXTERNAL_ID)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .sex(UPDATED_SEX)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .loginStatus(UPDATED_LOGIN_STATUS);

        restWalletHolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletHolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletHolder))
            )
            .andExpect(status().isOk());

        // Validate the WalletHolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletHolderUpdatableFieldsEquals(partialUpdatedWalletHolder, getPersistedWalletHolder(partialUpdatedWalletHolder));
    }

    @Test
    @Transactional
    void patchNonExistingWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walletHolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletHolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletHolder))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalletHolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletHolder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletHolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(walletHolder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletHolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalletHolder() throws Exception {
        // Initialize the database
        insertedWalletHolder = walletHolderRepository.saveAndFlush(walletHolder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the walletHolder
        restWalletHolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, walletHolder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletHolderRepository.count();
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

    protected WalletHolder getPersistedWalletHolder(WalletHolder walletHolder) {
        return walletHolderRepository.findById(walletHolder.getId()).orElseThrow();
    }

    protected void assertPersistedWalletHolderToMatchAllProperties(WalletHolder expectedWalletHolder) {
        assertWalletHolderAllPropertiesEquals(expectedWalletHolder, getPersistedWalletHolder(expectedWalletHolder));
    }

    protected void assertPersistedWalletHolderToMatchUpdatableProperties(WalletHolder expectedWalletHolder) {
        assertWalletHolderAllUpdatablePropertiesEquals(expectedWalletHolder, getPersistedWalletHolder(expectedWalletHolder));
    }
}
