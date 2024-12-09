package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.OneTimePasswordAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.OneTimePassword;
import com.kotekka.app.domain.enumeration.OtpStatus;
import com.kotekka.app.repository.OneTimePasswordRepository;
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
 * Integration tests for the {@link OneTimePasswordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OneTimePasswordResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final OtpStatus DEFAULT_STATUS = OtpStatus.PENDING;
    private static final OtpStatus UPDATED_STATUS = OtpStatus.VALID;

    private static final Instant DEFAULT_EXPIRY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/one-time-passwords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOneTimePasswordMockMvc;

    private OneTimePassword oneTimePassword;

    private OneTimePassword insertedOneTimePassword;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OneTimePassword createEntity() {
        return new OneTimePassword()
            .uuid(DEFAULT_UUID)
            .user(DEFAULT_USER)
            .code(DEFAULT_CODE)
            .status(DEFAULT_STATUS)
            .expiry(DEFAULT_EXPIRY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OneTimePassword createUpdatedEntity() {
        return new OneTimePassword()
            .uuid(UPDATED_UUID)
            .user(UPDATED_USER)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .expiry(UPDATED_EXPIRY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        oneTimePassword = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOneTimePassword != null) {
            oneTimePasswordRepository.delete(insertedOneTimePassword);
            insertedOneTimePassword = null;
        }
    }

    @Test
    @Transactional
    void createOneTimePassword() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OneTimePassword
        var returnedOneTimePassword = om.readValue(
            restOneTimePasswordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneTimePassword)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OneTimePassword.class
        );

        // Validate the OneTimePassword in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOneTimePasswordUpdatableFieldsEquals(returnedOneTimePassword, getPersistedOneTimePassword(returnedOneTimePassword));

        insertedOneTimePassword = returnedOneTimePassword;
    }

    @Test
    @Transactional
    void createOneTimePasswordWithExistingId() throws Exception {
        // Create the OneTimePassword with an existing ID
        oneTimePassword.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOneTimePasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneTimePassword)))
            .andExpect(status().isBadRequest());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oneTimePassword.setUuid(null);

        // Create the OneTimePassword, which fails.

        restOneTimePasswordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneTimePassword)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOneTimePasswords() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oneTimePassword.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(DEFAULT_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOneTimePassword() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get the oneTimePassword
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL_ID, oneTimePassword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oneTimePassword.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.expiry").value(DEFAULT_EXPIRY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getOneTimePasswordsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        Long id = oneTimePassword.getId();

        defaultOneTimePasswordFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOneTimePasswordFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOneTimePasswordFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where uuid equals to
        defaultOneTimePasswordFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where uuid in
        defaultOneTimePasswordFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where uuid is not null
        defaultOneTimePasswordFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where user equals to
        defaultOneTimePasswordFiltering("user.equals=" + DEFAULT_USER, "user.equals=" + UPDATED_USER);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUserIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where user in
        defaultOneTimePasswordFiltering("user.in=" + DEFAULT_USER + "," + UPDATED_USER, "user.in=" + UPDATED_USER);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where user is not null
        defaultOneTimePasswordFiltering("user.specified=true", "user.specified=false");
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUserContainsSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where user contains
        defaultOneTimePasswordFiltering("user.contains=" + DEFAULT_USER, "user.contains=" + UPDATED_USER);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByUserNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where user does not contain
        defaultOneTimePasswordFiltering("user.doesNotContain=" + UPDATED_USER, "user.doesNotContain=" + DEFAULT_USER);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where code equals to
        defaultOneTimePasswordFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where code in
        defaultOneTimePasswordFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where code is not null
        defaultOneTimePasswordFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where code contains
        defaultOneTimePasswordFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where code does not contain
        defaultOneTimePasswordFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where status equals to
        defaultOneTimePasswordFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where status in
        defaultOneTimePasswordFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where status is not null
        defaultOneTimePasswordFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByExpiryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where expiry equals to
        defaultOneTimePasswordFiltering("expiry.equals=" + DEFAULT_EXPIRY, "expiry.equals=" + UPDATED_EXPIRY);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByExpiryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where expiry in
        defaultOneTimePasswordFiltering("expiry.in=" + DEFAULT_EXPIRY + "," + UPDATED_EXPIRY, "expiry.in=" + UPDATED_EXPIRY);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByExpiryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where expiry is not null
        defaultOneTimePasswordFiltering("expiry.specified=true", "expiry.specified=false");
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where createdDate equals to
        defaultOneTimePasswordFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where createdDate in
        defaultOneTimePasswordFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOneTimePasswordsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        // Get all the oneTimePasswordList where createdDate is not null
        defaultOneTimePasswordFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultOneTimePasswordFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOneTimePasswordShouldBeFound(shouldBeFound);
        defaultOneTimePasswordShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOneTimePasswordShouldBeFound(String filter) throws Exception {
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oneTimePassword.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(DEFAULT_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOneTimePasswordShouldNotBeFound(String filter) throws Exception {
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOneTimePasswordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOneTimePassword() throws Exception {
        // Get the oneTimePassword
        restOneTimePasswordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOneTimePassword() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oneTimePassword
        OneTimePassword updatedOneTimePassword = oneTimePasswordRepository.findById(oneTimePassword.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOneTimePassword are not directly saved in db
        em.detach(updatedOneTimePassword);
        updatedOneTimePassword
            .uuid(UPDATED_UUID)
            .user(UPDATED_USER)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .expiry(UPDATED_EXPIRY)
            .createdDate(UPDATED_CREATED_DATE);

        restOneTimePasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOneTimePassword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOneTimePassword))
            )
            .andExpect(status().isOk());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOneTimePasswordToMatchAllProperties(updatedOneTimePassword);
    }

    @Test
    @Transactional
    void putNonExistingOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oneTimePassword.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oneTimePassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oneTimePassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneTimePassword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOneTimePasswordWithPatch() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oneTimePassword using partial update
        OneTimePassword partialUpdatedOneTimePassword = new OneTimePassword();
        partialUpdatedOneTimePassword.setId(oneTimePassword.getId());

        partialUpdatedOneTimePassword.user(UPDATED_USER).code(UPDATED_CODE).status(UPDATED_STATUS).createdDate(UPDATED_CREATED_DATE);

        restOneTimePasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOneTimePassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOneTimePassword))
            )
            .andExpect(status().isOk());

        // Validate the OneTimePassword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOneTimePasswordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOneTimePassword, oneTimePassword),
            getPersistedOneTimePassword(oneTimePassword)
        );
    }

    @Test
    @Transactional
    void fullUpdateOneTimePasswordWithPatch() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oneTimePassword using partial update
        OneTimePassword partialUpdatedOneTimePassword = new OneTimePassword();
        partialUpdatedOneTimePassword.setId(oneTimePassword.getId());

        partialUpdatedOneTimePassword
            .uuid(UPDATED_UUID)
            .user(UPDATED_USER)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .expiry(UPDATED_EXPIRY)
            .createdDate(UPDATED_CREATED_DATE);

        restOneTimePasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOneTimePassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOneTimePassword))
            )
            .andExpect(status().isOk());

        // Validate the OneTimePassword in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOneTimePasswordUpdatableFieldsEquals(
            partialUpdatedOneTimePassword,
            getPersistedOneTimePassword(partialUpdatedOneTimePassword)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oneTimePassword.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oneTimePassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oneTimePassword))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOneTimePassword() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneTimePassword.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneTimePasswordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(oneTimePassword)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OneTimePassword in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOneTimePassword() throws Exception {
        // Initialize the database
        insertedOneTimePassword = oneTimePasswordRepository.saveAndFlush(oneTimePassword);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the oneTimePassword
        restOneTimePasswordMockMvc
            .perform(delete(ENTITY_API_URL_ID, oneTimePassword.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return oneTimePasswordRepository.count();
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

    protected OneTimePassword getPersistedOneTimePassword(OneTimePassword oneTimePassword) {
        return oneTimePasswordRepository.findById(oneTimePassword.getId()).orElseThrow();
    }

    protected void assertPersistedOneTimePasswordToMatchAllProperties(OneTimePassword expectedOneTimePassword) {
        assertOneTimePasswordAllPropertiesEquals(expectedOneTimePassword, getPersistedOneTimePassword(expectedOneTimePassword));
    }

    protected void assertPersistedOneTimePasswordToMatchUpdatableProperties(OneTimePassword expectedOneTimePassword) {
        assertOneTimePasswordAllUpdatablePropertiesEquals(expectedOneTimePassword, getPersistedOneTimePassword(expectedOneTimePassword));
    }
}
