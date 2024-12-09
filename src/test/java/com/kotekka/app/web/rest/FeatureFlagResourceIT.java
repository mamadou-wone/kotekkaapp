package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.FeatureFlagAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.FeatureFlag;
import com.kotekka.app.repository.FeatureFlagRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
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
 * Integration tests for the {@link FeatureFlagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeatureFlagResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feature-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeatureFlagMockMvc;

    private FeatureFlag featureFlag;

    private FeatureFlag insertedFeatureFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureFlag createEntity() {
        return new FeatureFlag()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedDate(DEFAULT_UPDATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeatureFlag createUpdatedEntity() {
        return new FeatureFlag()
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        featureFlag = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeatureFlag != null) {
            featureFlagRepository.delete(insertedFeatureFlag);
            insertedFeatureFlag = null;
        }
    }

    @Test
    @Transactional
    void createFeatureFlag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeatureFlag
        var returnedFeatureFlag = om.readValue(
            restFeatureFlagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlag)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeatureFlag.class
        );

        // Validate the FeatureFlag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeatureFlagUpdatableFieldsEquals(returnedFeatureFlag, getPersistedFeatureFlag(returnedFeatureFlag));

        insertedFeatureFlag = returnedFeatureFlag;
    }

    @Test
    @Transactional
    void createFeatureFlagWithExistingId() throws Exception {
        // Create the FeatureFlag with an existing ID
        featureFlag.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeatureFlagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlag)))
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeatureFlags() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(featureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get the featureFlag
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, featureFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(featureFlag.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getFeatureFlagsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        Long id = featureFlag.getId();

        defaultFeatureFlagFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFeatureFlagFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFeatureFlagFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where name equals to
        defaultFeatureFlagFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where name in
        defaultFeatureFlagFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where name is not null
        defaultFeatureFlagFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where name contains
        defaultFeatureFlagFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where name does not contain
        defaultFeatureFlagFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where enabled equals to
        defaultFeatureFlagFiltering("enabled.equals=" + DEFAULT_ENABLED, "enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where enabled in
        defaultFeatureFlagFiltering("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED, "enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where enabled is not null
        defaultFeatureFlagFiltering("enabled.specified=true", "enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where description equals to
        defaultFeatureFlagFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where description in
        defaultFeatureFlagFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where description is not null
        defaultFeatureFlagFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where description contains
        defaultFeatureFlagFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where description does not contain
        defaultFeatureFlagFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdBy equals to
        defaultFeatureFlagFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdBy in
        defaultFeatureFlagFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdBy is not null
        defaultFeatureFlagFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdBy contains
        defaultFeatureFlagFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdBy does not contain
        defaultFeatureFlagFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdDate equals to
        defaultFeatureFlagFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdDate in
        defaultFeatureFlagFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where createdDate is not null
        defaultFeatureFlagFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedBy equals to
        defaultFeatureFlagFiltering("updatedBy.equals=" + DEFAULT_UPDATED_BY, "updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedBy in
        defaultFeatureFlagFiltering("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY, "updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedBy is not null
        defaultFeatureFlagFiltering("updatedBy.specified=true", "updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedBy contains
        defaultFeatureFlagFiltering("updatedBy.contains=" + DEFAULT_UPDATED_BY, "updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedBy does not contain
        defaultFeatureFlagFiltering("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY, "updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedDate equals to
        defaultFeatureFlagFiltering("updatedDate.equals=" + DEFAULT_UPDATED_DATE, "updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedDate in
        defaultFeatureFlagFiltering(
            "updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE,
            "updatedDate.in=" + UPDATED_UPDATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFeatureFlagsByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        // Get all the featureFlagList where updatedDate is not null
        defaultFeatureFlagFiltering("updatedDate.specified=true", "updatedDate.specified=false");
    }

    private void defaultFeatureFlagFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFeatureFlagShouldBeFound(shouldBeFound);
        defaultFeatureFlagShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeatureFlagShouldBeFound(String filter) throws Exception {
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(featureFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));

        // Check, that the count call also returns 1
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeatureFlagShouldNotBeFound(String filter) throws Exception {
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeatureFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeatureFlag() throws Exception {
        // Get the featureFlag
        restFeatureFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag
        FeatureFlag updatedFeatureFlag = featureFlagRepository.findById(featureFlag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeatureFlag are not directly saved in db
        em.detach(updatedFeatureFlag);
        updatedFeatureFlag
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeatureFlag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeatureFlag))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeatureFlagToMatchAllProperties(updatedFeatureFlag);
    }

    @Test
    @Transactional
    void putNonExistingFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, featureFlag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureFlag))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(featureFlag))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(featureFlag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeatureFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag using partial update
        FeatureFlag partialUpdatedFeatureFlag = new FeatureFlag();
        partialUpdatedFeatureFlag.setId(featureFlag.getId());

        partialUpdatedFeatureFlag
            .enabled(UPDATED_ENABLED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureFlag))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureFlagUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeatureFlag, featureFlag),
            getPersistedFeatureFlag(featureFlag)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeatureFlagWithPatch() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the featureFlag using partial update
        FeatureFlag partialUpdatedFeatureFlag = new FeatureFlag();
        partialUpdatedFeatureFlag.setId(featureFlag.getId());

        partialUpdatedFeatureFlag
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeatureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeatureFlag))
            )
            .andExpect(status().isOk());

        // Validate the FeatureFlag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeatureFlagUpdatableFieldsEquals(partialUpdatedFeatureFlag, getPersistedFeatureFlag(partialUpdatedFeatureFlag));
    }

    @Test
    @Transactional
    void patchNonExistingFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, featureFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureFlag))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(featureFlag))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeatureFlag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        featureFlag.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeatureFlagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(featureFlag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeatureFlag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeatureFlag() throws Exception {
        // Initialize the database
        insertedFeatureFlag = featureFlagRepository.saveAndFlush(featureFlag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the featureFlag
        restFeatureFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, featureFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return featureFlagRepository.count();
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

    protected FeatureFlag getPersistedFeatureFlag(FeatureFlag featureFlag) {
        return featureFlagRepository.findById(featureFlag.getId()).orElseThrow();
    }

    protected void assertPersistedFeatureFlagToMatchAllProperties(FeatureFlag expectedFeatureFlag) {
        assertFeatureFlagAllPropertiesEquals(expectedFeatureFlag, getPersistedFeatureFlag(expectedFeatureFlag));
    }

    protected void assertPersistedFeatureFlagToMatchUpdatableProperties(FeatureFlag expectedFeatureFlag) {
        assertFeatureFlagAllUpdatablePropertiesEquals(expectedFeatureFlag, getPersistedFeatureFlag(expectedFeatureFlag));
    }
}
