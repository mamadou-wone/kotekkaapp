package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.FailedAttemptAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.FailedAttempt;
import com.kotekka.app.domain.enumeration.Action;
import com.kotekka.app.domain.enumeration.App;
import com.kotekka.app.repository.FailedAttemptRepository;
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
 * Integration tests for the {@link FailedAttemptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FailedAttemptResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_AFTER_LOCK = false;
    private static final Boolean UPDATED_IS_AFTER_LOCK = true;

    private static final App DEFAULT_APP = App.MOBILE;
    private static final App UPDATED_APP = App.WEB;

    private static final Action DEFAULT_ACTION = Action.LOGIN;
    private static final Action UPDATED_ACTION = Action.PHONENUMBER_CHECK;

    private static final UUID DEFAULT_DEVICE = UUID.randomUUID();
    private static final UUID UPDATED_DEVICE = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/failed-attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FailedAttemptRepository failedAttemptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFailedAttemptMockMvc;

    private FailedAttempt failedAttempt;

    private FailedAttempt insertedFailedAttempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FailedAttempt createEntity() {
        return new FailedAttempt()
            .login(DEFAULT_LOGIN)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .isAfterLock(DEFAULT_IS_AFTER_LOCK)
            .app(DEFAULT_APP)
            .action(DEFAULT_ACTION)
            .device(DEFAULT_DEVICE)
            .createdDate(DEFAULT_CREATED_DATE)
            .reason(DEFAULT_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FailedAttempt createUpdatedEntity() {
        return new FailedAttempt()
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .app(UPDATED_APP)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE)
            .reason(UPDATED_REASON);
    }

    @BeforeEach
    public void initTest() {
        failedAttempt = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFailedAttempt != null) {
            failedAttemptRepository.delete(insertedFailedAttempt);
            insertedFailedAttempt = null;
        }
    }

    @Test
    @Transactional
    void createFailedAttempt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FailedAttempt
        var returnedFailedAttempt = om.readValue(
            restFailedAttemptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttempt)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FailedAttempt.class
        );

        // Validate the FailedAttempt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFailedAttemptUpdatableFieldsEquals(returnedFailedAttempt, getPersistedFailedAttempt(returnedFailedAttempt));

        insertedFailedAttempt = returnedFailedAttempt;
    }

    @Test
    @Transactional
    void createFailedAttemptWithExistingId() throws Exception {
        // Create the FailedAttempt with an existing ID
        failedAttempt.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFailedAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttempt)))
            .andExpect(status().isBadRequest());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFailedAttempts() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failedAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].isAfterLock").value(hasItem(DEFAULT_IS_AFTER_LOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].app").value(hasItem(DEFAULT_APP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getFailedAttempt() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get the failedAttempt
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, failedAttempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(failedAttempt.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.isAfterLock").value(DEFAULT_IS_AFTER_LOCK.booleanValue()))
            .andExpect(jsonPath("$.app").value(DEFAULT_APP.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getFailedAttemptsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        Long id = failedAttempt.getId();

        defaultFailedAttemptFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFailedAttemptFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFailedAttemptFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where login equals to
        defaultFailedAttemptFiltering("login.equals=" + DEFAULT_LOGIN, "login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where login in
        defaultFailedAttemptFiltering("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN, "login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where login is not null
        defaultFailedAttemptFiltering("login.specified=true", "login.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByLoginContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where login contains
        defaultFailedAttemptFiltering("login.contains=" + DEFAULT_LOGIN, "login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where login does not contain
        defaultFailedAttemptFiltering("login.doesNotContain=" + UPDATED_LOGIN, "login.doesNotContain=" + DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where ipAddress equals to
        defaultFailedAttemptFiltering("ipAddress.equals=" + DEFAULT_IP_ADDRESS, "ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where ipAddress in
        defaultFailedAttemptFiltering(
            "ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS,
            "ipAddress.in=" + UPDATED_IP_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where ipAddress is not null
        defaultFailedAttemptFiltering("ipAddress.specified=true", "ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where ipAddress contains
        defaultFailedAttemptFiltering("ipAddress.contains=" + DEFAULT_IP_ADDRESS, "ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where ipAddress does not contain
        defaultFailedAttemptFiltering("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS, "ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIsAfterLockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where isAfterLock equals to
        defaultFailedAttemptFiltering("isAfterLock.equals=" + DEFAULT_IS_AFTER_LOCK, "isAfterLock.equals=" + UPDATED_IS_AFTER_LOCK);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIsAfterLockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where isAfterLock in
        defaultFailedAttemptFiltering(
            "isAfterLock.in=" + DEFAULT_IS_AFTER_LOCK + "," + UPDATED_IS_AFTER_LOCK,
            "isAfterLock.in=" + UPDATED_IS_AFTER_LOCK
        );
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByIsAfterLockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where isAfterLock is not null
        defaultFailedAttemptFiltering("isAfterLock.specified=true", "isAfterLock.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByAppIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where app equals to
        defaultFailedAttemptFiltering("app.equals=" + DEFAULT_APP, "app.equals=" + UPDATED_APP);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByAppIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where app in
        defaultFailedAttemptFiltering("app.in=" + DEFAULT_APP + "," + UPDATED_APP, "app.in=" + UPDATED_APP);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where app is not null
        defaultFailedAttemptFiltering("app.specified=true", "app.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where action equals to
        defaultFailedAttemptFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where action in
        defaultFailedAttemptFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where action is not null
        defaultFailedAttemptFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where device equals to
        defaultFailedAttemptFiltering("device.equals=" + DEFAULT_DEVICE, "device.equals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where device in
        defaultFailedAttemptFiltering("device.in=" + DEFAULT_DEVICE + "," + UPDATED_DEVICE, "device.in=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where device is not null
        defaultFailedAttemptFiltering("device.specified=true", "device.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where createdDate equals to
        defaultFailedAttemptFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where createdDate in
        defaultFailedAttemptFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where createdDate is not null
        defaultFailedAttemptFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where reason equals to
        defaultFailedAttemptFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where reason in
        defaultFailedAttemptFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where reason is not null
        defaultFailedAttemptFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where reason contains
        defaultFailedAttemptFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllFailedAttemptsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        // Get all the failedAttemptList where reason does not contain
        defaultFailedAttemptFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    private void defaultFailedAttemptFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFailedAttemptShouldBeFound(shouldBeFound);
        defaultFailedAttemptShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFailedAttemptShouldBeFound(String filter) throws Exception {
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failedAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].isAfterLock").value(hasItem(DEFAULT_IS_AFTER_LOCK.booleanValue())))
            .andExpect(jsonPath("$.[*].app").value(hasItem(DEFAULT_APP.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));

        // Check, that the count call also returns 1
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFailedAttemptShouldNotBeFound(String filter) throws Exception {
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFailedAttemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFailedAttempt() throws Exception {
        // Get the failedAttempt
        restFailedAttemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFailedAttempt() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttempt
        FailedAttempt updatedFailedAttempt = failedAttemptRepository.findById(failedAttempt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFailedAttempt are not directly saved in db
        em.detach(updatedFailedAttempt);
        updatedFailedAttempt
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .app(UPDATED_APP)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE)
            .reason(UPDATED_REASON);

        restFailedAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFailedAttempt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFailedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFailedAttemptToMatchAllProperties(updatedFailedAttempt);
    }

    @Test
    @Transactional
    void putNonExistingFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, failedAttempt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(failedAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(failedAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttempt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFailedAttemptWithPatch() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttempt using partial update
        FailedAttempt partialUpdatedFailedAttempt = new FailedAttempt();
        partialUpdatedFailedAttempt.setId(failedAttempt.getId());

        partialUpdatedFailedAttempt.isAfterLock(UPDATED_IS_AFTER_LOCK).app(UPDATED_APP).action(UPDATED_ACTION).reason(UPDATED_REASON);

        restFailedAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFailedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFailedAttemptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFailedAttempt, failedAttempt),
            getPersistedFailedAttempt(failedAttempt)
        );
    }

    @Test
    @Transactional
    void fullUpdateFailedAttemptWithPatch() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttempt using partial update
        FailedAttempt partialUpdatedFailedAttempt = new FailedAttempt();
        partialUpdatedFailedAttempt.setId(failedAttempt.getId());

        partialUpdatedFailedAttempt
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .app(UPDATED_APP)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE)
            .reason(UPDATED_REASON);

        restFailedAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFailedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFailedAttemptUpdatableFieldsEquals(partialUpdatedFailedAttempt, getPersistedFailedAttempt(partialUpdatedFailedAttempt));
    }

    @Test
    @Transactional
    void patchNonExistingFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, failedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(failedAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(failedAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFailedAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttempt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(failedAttempt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedAttempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFailedAttempt() throws Exception {
        // Initialize the database
        insertedFailedAttempt = failedAttemptRepository.saveAndFlush(failedAttempt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the failedAttempt
        restFailedAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, failedAttempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return failedAttemptRepository.count();
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

    protected FailedAttempt getPersistedFailedAttempt(FailedAttempt failedAttempt) {
        return failedAttemptRepository.findById(failedAttempt.getId()).orElseThrow();
    }

    protected void assertPersistedFailedAttemptToMatchAllProperties(FailedAttempt expectedFailedAttempt) {
        assertFailedAttemptAllPropertiesEquals(expectedFailedAttempt, getPersistedFailedAttempt(expectedFailedAttempt));
    }

    protected void assertPersistedFailedAttemptToMatchUpdatableProperties(FailedAttempt expectedFailedAttempt) {
        assertFailedAttemptAllUpdatablePropertiesEquals(expectedFailedAttempt, getPersistedFailedAttempt(expectedFailedAttempt));
    }
}
