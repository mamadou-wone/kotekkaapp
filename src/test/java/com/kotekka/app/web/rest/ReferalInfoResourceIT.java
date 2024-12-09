package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.ReferalInfoAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.ReferalInfo;
import com.kotekka.app.domain.enumeration.ReferalStatus;
import com.kotekka.app.repository.ReferalInfoRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ReferalInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferalInfoResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_REFERAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERAL_CODE = "BBBBBBBBBB";

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final UUID DEFAULT_REFERED = UUID.randomUUID();
    private static final UUID UPDATED_REFERED = UUID.randomUUID();

    private static final ReferalStatus DEFAULT_STATUS = ReferalStatus.PENDING;
    private static final ReferalStatus UPDATED_STATUS = ReferalStatus.CONFIRMED;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/referal-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferalInfoRepository referalInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferalInfoMockMvc;

    private ReferalInfo referalInfo;

    private ReferalInfo insertedReferalInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReferalInfo createEntity() {
        return new ReferalInfo()
            .uuid(DEFAULT_UUID)
            .referalCode(DEFAULT_REFERAL_CODE)
            .walletHolder(DEFAULT_WALLET_HOLDER)
            .refered(DEFAULT_REFERED)
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
    public static ReferalInfo createUpdatedEntity() {
        return new ReferalInfo()
            .uuid(UPDATED_UUID)
            .referalCode(UPDATED_REFERAL_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .refered(UPDATED_REFERED)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        referalInfo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReferalInfo != null) {
            referalInfoRepository.delete(insertedReferalInfo);
            insertedReferalInfo = null;
        }
    }

    @Test
    @Transactional
    void createReferalInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReferalInfo
        var returnedReferalInfo = om.readValue(
            restReferalInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referalInfo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReferalInfo.class
        );

        // Validate the ReferalInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReferalInfoUpdatableFieldsEquals(returnedReferalInfo, getPersistedReferalInfo(returnedReferalInfo));

        insertedReferalInfo = returnedReferalInfo;
    }

    @Test
    @Transactional
    void createReferalInfoWithExistingId() throws Exception {
        // Create the ReferalInfo with an existing ID
        referalInfo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferalInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referalInfo)))
            .andExpect(status().isBadRequest());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        referalInfo.setUuid(null);

        // Create the ReferalInfo, which fails.

        restReferalInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referalInfo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferalInfos() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].referalCode").value(hasItem(DEFAULT_REFERAL_CODE)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].refered").value(hasItem(DEFAULT_REFERED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getReferalInfo() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get the referalInfo
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, referalInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referalInfo.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.referalCode").value(DEFAULT_REFERAL_CODE))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.refered").value(DEFAULT_REFERED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getReferalInfosByIdFiltering() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        Long id = referalInfo.getId();

        defaultReferalInfoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReferalInfoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReferalInfoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReferalInfosByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where uuid equals to
        defaultReferalInfoFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllReferalInfosByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where uuid in
        defaultReferalInfoFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllReferalInfosByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where uuid is not null
        defaultReferalInfoFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where referalCode equals to
        defaultReferalInfoFiltering("referalCode.equals=" + DEFAULT_REFERAL_CODE, "referalCode.equals=" + UPDATED_REFERAL_CODE);
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where referalCode in
        defaultReferalInfoFiltering(
            "referalCode.in=" + DEFAULT_REFERAL_CODE + "," + UPDATED_REFERAL_CODE,
            "referalCode.in=" + UPDATED_REFERAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where referalCode is not null
        defaultReferalInfoFiltering("referalCode.specified=true", "referalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where referalCode contains
        defaultReferalInfoFiltering("referalCode.contains=" + DEFAULT_REFERAL_CODE, "referalCode.contains=" + UPDATED_REFERAL_CODE);
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where referalCode does not contain
        defaultReferalInfoFiltering(
            "referalCode.doesNotContain=" + UPDATED_REFERAL_CODE,
            "referalCode.doesNotContain=" + DEFAULT_REFERAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where walletHolder equals to
        defaultReferalInfoFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllReferalInfosByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where walletHolder in
        defaultReferalInfoFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where walletHolder is not null
        defaultReferalInfoFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where refered equals to
        defaultReferalInfoFiltering("refered.equals=" + DEFAULT_REFERED, "refered.equals=" + UPDATED_REFERED);
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where refered in
        defaultReferalInfoFiltering("refered.in=" + DEFAULT_REFERED + "," + UPDATED_REFERED, "refered.in=" + UPDATED_REFERED);
    }

    @Test
    @Transactional
    void getAllReferalInfosByReferedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where refered is not null
        defaultReferalInfoFiltering("refered.specified=true", "refered.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where status equals to
        defaultReferalInfoFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReferalInfosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where status in
        defaultReferalInfoFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReferalInfosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where status is not null
        defaultReferalInfoFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdBy equals to
        defaultReferalInfoFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdBy in
        defaultReferalInfoFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdBy is not null
        defaultReferalInfoFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdBy contains
        defaultReferalInfoFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdBy does not contain
        defaultReferalInfoFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdDate equals to
        defaultReferalInfoFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdDate in
        defaultReferalInfoFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where createdDate is not null
        defaultReferalInfoFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedBy equals to
        defaultReferalInfoFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedBy in
        defaultReferalInfoFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedBy is not null
        defaultReferalInfoFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedBy contains
        defaultReferalInfoFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedBy does not contain
        defaultReferalInfoFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedDate equals to
        defaultReferalInfoFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedDate in
        defaultReferalInfoFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReferalInfosByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        // Get all the referalInfoList where lastModifiedDate is not null
        defaultReferalInfoFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultReferalInfoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReferalInfoShouldBeFound(shouldBeFound);
        defaultReferalInfoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReferalInfoShouldBeFound(String filter) throws Exception {
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referalInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].referalCode").value(hasItem(DEFAULT_REFERAL_CODE)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].refered").value(hasItem(DEFAULT_REFERED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReferalInfoShouldNotBeFound(String filter) throws Exception {
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReferalInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReferalInfo() throws Exception {
        // Get the referalInfo
        restReferalInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferalInfo() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referalInfo
        ReferalInfo updatedReferalInfo = referalInfoRepository.findById(referalInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReferalInfo are not directly saved in db
        em.detach(updatedReferalInfo);
        updatedReferalInfo
            .uuid(UPDATED_UUID)
            .referalCode(UPDATED_REFERAL_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .refered(UPDATED_REFERED)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restReferalInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferalInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReferalInfo))
            )
            .andExpect(status().isOk());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferalInfoToMatchAllProperties(updatedReferalInfo);
    }

    @Test
    @Transactional
    void putNonExistingReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referalInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referalInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferalInfoWithPatch() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referalInfo using partial update
        ReferalInfo partialUpdatedReferalInfo = new ReferalInfo();
        partialUpdatedReferalInfo.setId(referalInfo.getId());

        partialUpdatedReferalInfo
            .uuid(UPDATED_UUID)
            .referalCode(UPDATED_REFERAL_CODE)
            .refered(UPDATED_REFERED)
            .createdDate(UPDATED_CREATED_DATE);

        restReferalInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferalInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferalInfo))
            )
            .andExpect(status().isOk());

        // Validate the ReferalInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferalInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReferalInfo, referalInfo),
            getPersistedReferalInfo(referalInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateReferalInfoWithPatch() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the referalInfo using partial update
        ReferalInfo partialUpdatedReferalInfo = new ReferalInfo();
        partialUpdatedReferalInfo.setId(referalInfo.getId());

        partialUpdatedReferalInfo
            .uuid(UPDATED_UUID)
            .referalCode(UPDATED_REFERAL_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .refered(UPDATED_REFERED)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restReferalInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferalInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReferalInfo))
            )
            .andExpect(status().isOk());

        // Validate the ReferalInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferalInfoUpdatableFieldsEquals(partialUpdatedReferalInfo, getPersistedReferalInfo(partialUpdatedReferalInfo));
    }

    @Test
    @Transactional
    void patchNonExistingReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referalInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referalInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferalInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        referalInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferalInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(referalInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReferalInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferalInfo() throws Exception {
        // Initialize the database
        insertedReferalInfo = referalInfoRepository.saveAndFlush(referalInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the referalInfo
        restReferalInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, referalInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return referalInfoRepository.count();
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

    protected ReferalInfo getPersistedReferalInfo(ReferalInfo referalInfo) {
        return referalInfoRepository.findById(referalInfo.getId()).orElseThrow();
    }

    protected void assertPersistedReferalInfoToMatchAllProperties(ReferalInfo expectedReferalInfo) {
        assertReferalInfoAllPropertiesEquals(expectedReferalInfo, getPersistedReferalInfo(expectedReferalInfo));
    }

    protected void assertPersistedReferalInfoToMatchUpdatableProperties(ReferalInfo expectedReferalInfo) {
        assertReferalInfoAllUpdatablePropertiesEquals(expectedReferalInfo, getPersistedReferalInfo(expectedReferalInfo));
    }
}
