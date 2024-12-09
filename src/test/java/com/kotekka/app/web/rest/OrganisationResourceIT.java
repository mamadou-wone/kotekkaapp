package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.OrganisationAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Organisation;
import com.kotekka.app.domain.enumeration.OrgType;
import com.kotekka.app.repository.OrganisationRepository;
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
 * Integration tests for the {@link OrganisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganisationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final OrgType DEFAULT_TYPE = OrgType.UNIVERSITY;
    private static final OrgType UPDATED_TYPE = OrgType.CORPORATION;

    private static final String DEFAULT_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEADCOUNT = 1;
    private static final Integer UPDATED_HEADCOUNT = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    private Organisation insertedOrganisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createEntity() {
        return new Organisation()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .parent(DEFAULT_PARENT)
            .location(DEFAULT_LOCATION)
            .headcount(DEFAULT_HEADCOUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createUpdatedEntity() {
        return new Organisation()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .parent(UPDATED_PARENT)
            .location(UPDATED_LOCATION)
            .headcount(UPDATED_HEADCOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        organisation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrganisation != null) {
            organisationRepository.delete(insertedOrganisation);
            insertedOrganisation = null;
        }
    }

    @Test
    @Transactional
    void createOrganisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Organisation
        var returnedOrganisation = om.readValue(
            restOrganisationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Organisation.class
        );

        // Validate the Organisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrganisationUpdatableFieldsEquals(returnedOrganisation, getPersistedOrganisation(returnedOrganisation));

        insertedOrganisation = returnedOrganisation;
    }

    @Test
    @Transactional
    void createOrganisationWithExistingId() throws Exception {
        // Create the Organisation with an existing ID
        organisation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisation)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        organisation.setName(null);

        // Create the Organisation, which fails.

        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganisations() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].headcount").value(hasItem(DEFAULT_HEADCOUNT)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get the organisation
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL_ID, organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.headcount").value(DEFAULT_HEADCOUNT))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganisation are not directly saved in db
        em.detach(updatedOrganisation);
        updatedOrganisation
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .parent(UPDATED_PARENT)
            .location(UPDATED_LOCATION)
            .headcount(UPDATED_HEADCOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrganisationToMatchAllProperties(updatedOrganisation);
    }

    @Test
    @Transactional
    void putNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.name(UPDATED_NAME).type(UPDATED_TYPE).createdBy(UPDATED_CREATED_BY);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrganisation, organisation),
            getPersistedOrganisation(organisation)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .parent(UPDATED_PARENT)
            .location(UPDATED_LOCATION)
            .headcount(UPDATED_HEADCOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(partialUpdatedOrganisation, getPersistedOrganisation(partialUpdatedOrganisation));
    }

    @Test
    @Transactional
    void patchNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(organisation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the organisation
        restOrganisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return organisationRepository.count();
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

    protected Organisation getPersistedOrganisation(Organisation organisation) {
        return organisationRepository.findById(organisation.getId()).orElseThrow();
    }

    protected void assertPersistedOrganisationToMatchAllProperties(Organisation expectedOrganisation) {
        assertOrganisationAllPropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }

    protected void assertPersistedOrganisationToMatchUpdatableProperties(Organisation expectedOrganisation) {
        assertOrganisationAllUpdatablePropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }
}
