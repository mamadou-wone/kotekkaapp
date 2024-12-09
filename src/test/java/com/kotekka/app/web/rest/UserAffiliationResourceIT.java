package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.UserAffiliationAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.UserAffiliation;
import com.kotekka.app.repository.UserAffiliationRepository;
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
 * Integration tests for the {@link UserAffiliationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAffiliationResourceIT {

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final String DEFAULT_AFFILIATION = "AAAAAAAAAA";
    private static final String UPDATED_AFFILIATION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-affiliations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAffiliationRepository userAffiliationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAffiliationMockMvc;

    private UserAffiliation userAffiliation;

    private UserAffiliation insertedUserAffiliation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAffiliation createEntity() {
        return new UserAffiliation()
            .walletHolder(DEFAULT_WALLET_HOLDER)
            .affiliation(DEFAULT_AFFILIATION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAffiliation createUpdatedEntity() {
        return new UserAffiliation()
            .walletHolder(UPDATED_WALLET_HOLDER)
            .affiliation(UPDATED_AFFILIATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        userAffiliation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAffiliation != null) {
            userAffiliationRepository.delete(insertedUserAffiliation);
            insertedUserAffiliation = null;
        }
    }

    @Test
    @Transactional
    void createUserAffiliation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAffiliation
        var returnedUserAffiliation = om.readValue(
            restUserAffiliationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAffiliation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAffiliation.class
        );

        // Validate the UserAffiliation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserAffiliationUpdatableFieldsEquals(returnedUserAffiliation, getPersistedUserAffiliation(returnedUserAffiliation));

        insertedUserAffiliation = returnedUserAffiliation;
    }

    @Test
    @Transactional
    void createUserAffiliationWithExistingId() throws Exception {
        // Create the UserAffiliation with an existing ID
        userAffiliation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAffiliationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAffiliation)))
            .andExpect(status().isBadRequest());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWalletHolderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAffiliation.setWalletHolder(null);

        // Create the UserAffiliation, which fails.

        restUserAffiliationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAffiliation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAffiliationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAffiliation.setAffiliation(null);

        // Create the UserAffiliation, which fails.

        restUserAffiliationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAffiliation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAffiliations() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAffiliation.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].affiliation").value(hasItem(DEFAULT_AFFILIATION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserAffiliation() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get the userAffiliation
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL_ID, userAffiliation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAffiliation.getId().intValue()))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.affiliation").value(DEFAULT_AFFILIATION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUserAffiliationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        Long id = userAffiliation.getId();

        defaultUserAffiliationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserAffiliationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserAffiliationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where walletHolder equals to
        defaultUserAffiliationFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where walletHolder in
        defaultUserAffiliationFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where walletHolder is not null
        defaultUserAffiliationFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByAffiliationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where affiliation equals to
        defaultUserAffiliationFiltering("affiliation.equals=" + DEFAULT_AFFILIATION, "affiliation.equals=" + UPDATED_AFFILIATION);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByAffiliationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where affiliation in
        defaultUserAffiliationFiltering(
            "affiliation.in=" + DEFAULT_AFFILIATION + "," + UPDATED_AFFILIATION,
            "affiliation.in=" + UPDATED_AFFILIATION
        );
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByAffiliationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where affiliation is not null
        defaultUserAffiliationFiltering("affiliation.specified=true", "affiliation.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByAffiliationContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where affiliation contains
        defaultUserAffiliationFiltering("affiliation.contains=" + DEFAULT_AFFILIATION, "affiliation.contains=" + UPDATED_AFFILIATION);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByAffiliationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where affiliation does not contain
        defaultUserAffiliationFiltering(
            "affiliation.doesNotContain=" + UPDATED_AFFILIATION,
            "affiliation.doesNotContain=" + DEFAULT_AFFILIATION
        );
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdBy equals to
        defaultUserAffiliationFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdBy in
        defaultUserAffiliationFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdBy is not null
        defaultUserAffiliationFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdBy contains
        defaultUserAffiliationFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdBy does not contain
        defaultUserAffiliationFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdDate equals to
        defaultUserAffiliationFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdDate in
        defaultUserAffiliationFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserAffiliationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        // Get all the userAffiliationList where createdDate is not null
        defaultUserAffiliationFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultUserAffiliationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserAffiliationShouldBeFound(shouldBeFound);
        defaultUserAffiliationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAffiliationShouldBeFound(String filter) throws Exception {
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAffiliation.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].affiliation").value(hasItem(DEFAULT_AFFILIATION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAffiliationShouldNotBeFound(String filter) throws Exception {
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAffiliationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAffiliation() throws Exception {
        // Get the userAffiliation
        restUserAffiliationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAffiliation() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAffiliation
        UserAffiliation updatedUserAffiliation = userAffiliationRepository.findById(userAffiliation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAffiliation are not directly saved in db
        em.detach(updatedUserAffiliation);
        updatedUserAffiliation
            .walletHolder(UPDATED_WALLET_HOLDER)
            .affiliation(UPDATED_AFFILIATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restUserAffiliationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAffiliation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserAffiliation))
            )
            .andExpect(status().isOk());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAffiliationToMatchAllProperties(updatedUserAffiliation);
    }

    @Test
    @Transactional
    void putNonExistingUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAffiliation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAffiliation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAffiliation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAffiliation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAffiliationWithPatch() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAffiliation using partial update
        UserAffiliation partialUpdatedUserAffiliation = new UserAffiliation();
        partialUpdatedUserAffiliation.setId(userAffiliation.getId());

        partialUpdatedUserAffiliation.walletHolder(UPDATED_WALLET_HOLDER).createdBy(UPDATED_CREATED_BY);

        restUserAffiliationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAffiliation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAffiliation))
            )
            .andExpect(status().isOk());

        // Validate the UserAffiliation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAffiliationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAffiliation, userAffiliation),
            getPersistedUserAffiliation(userAffiliation)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAffiliationWithPatch() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAffiliation using partial update
        UserAffiliation partialUpdatedUserAffiliation = new UserAffiliation();
        partialUpdatedUserAffiliation.setId(userAffiliation.getId());

        partialUpdatedUserAffiliation
            .walletHolder(UPDATED_WALLET_HOLDER)
            .affiliation(UPDATED_AFFILIATION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restUserAffiliationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAffiliation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAffiliation))
            )
            .andExpect(status().isOk());

        // Validate the UserAffiliation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAffiliationUpdatableFieldsEquals(
            partialUpdatedUserAffiliation,
            getPersistedUserAffiliation(partialUpdatedUserAffiliation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAffiliation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAffiliation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAffiliation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAffiliation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAffiliation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAffiliationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAffiliation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAffiliation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAffiliation() throws Exception {
        // Initialize the database
        insertedUserAffiliation = userAffiliationRepository.saveAndFlush(userAffiliation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAffiliation
        restUserAffiliationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAffiliation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAffiliationRepository.count();
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

    protected UserAffiliation getPersistedUserAffiliation(UserAffiliation userAffiliation) {
        return userAffiliationRepository.findById(userAffiliation.getId()).orElseThrow();
    }

    protected void assertPersistedUserAffiliationToMatchAllProperties(UserAffiliation expectedUserAffiliation) {
        assertUserAffiliationAllPropertiesEquals(expectedUserAffiliation, getPersistedUserAffiliation(expectedUserAffiliation));
    }

    protected void assertPersistedUserAffiliationToMatchUpdatableProperties(UserAffiliation expectedUserAffiliation) {
        assertUserAffiliationAllUpdatablePropertiesEquals(expectedUserAffiliation, getPersistedUserAffiliation(expectedUserAffiliation));
    }
}
