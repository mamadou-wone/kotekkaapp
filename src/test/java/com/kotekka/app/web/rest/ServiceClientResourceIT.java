package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.ServiceClientAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.ServiceClient;
import com.kotekka.app.domain.enumeration.AccountType;
import com.kotekka.app.domain.enumeration.DefaultStatus;
import com.kotekka.app.repository.ServiceClientRepository;
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
 * Integration tests for the {@link ServiceClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceClientResourceIT {

    private static final UUID DEFAULT_CLIENT_ID = UUID.randomUUID();
    private static final UUID UPDATED_CLIENT_ID = UUID.randomUUID();

    private static final AccountType DEFAULT_TYPE = AccountType.CUSTOMER;
    private static final AccountType UPDATED_TYPE = AccountType.MERCHANT;

    private static final UUID DEFAULT_API_KEY = UUID.randomUUID();
    private static final UUID UPDATED_API_KEY = UUID.randomUUID();

    private static final DefaultStatus DEFAULT_STATUS = DefaultStatus.PENDING;
    private static final DefaultStatus UPDATED_STATUS = DefaultStatus.ACTIVE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/service-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceClientRepository serviceClientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceClientMockMvc;

    private ServiceClient serviceClient;

    private ServiceClient insertedServiceClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceClient createEntity() {
        return new ServiceClient()
            .clientId(DEFAULT_CLIENT_ID)
            .type(DEFAULT_TYPE)
            .apiKey(DEFAULT_API_KEY)
            .status(DEFAULT_STATUS)
            .note(DEFAULT_NOTE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceClient createUpdatedEntity() {
        return new ServiceClient()
            .clientId(UPDATED_CLIENT_ID)
            .type(UPDATED_TYPE)
            .apiKey(UPDATED_API_KEY)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        serviceClient = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedServiceClient != null) {
            serviceClientRepository.delete(insertedServiceClient);
            insertedServiceClient = null;
        }
    }

    @Test
    @Transactional
    void createServiceClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceClient
        var returnedServiceClient = om.readValue(
            restServiceClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceClient)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceClient.class
        );

        // Validate the ServiceClient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertServiceClientUpdatableFieldsEquals(returnedServiceClient, getPersistedServiceClient(returnedServiceClient));

        insertedServiceClient = returnedServiceClient;
    }

    @Test
    @Transactional
    void createServiceClientWithExistingId() throws Exception {
        // Create the ServiceClient with an existing ID
        serviceClient.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceClient)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceClients() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getServiceClient() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get the serviceClient
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceClient.getId().intValue()))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.apiKey").value(DEFAULT_API_KEY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getServiceClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        Long id = serviceClient.getId();

        defaultServiceClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceClientsByClientIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where clientId equals to
        defaultServiceClientFiltering("clientId.equals=" + DEFAULT_CLIENT_ID, "clientId.equals=" + UPDATED_CLIENT_ID);
    }

    @Test
    @Transactional
    void getAllServiceClientsByClientIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where clientId in
        defaultServiceClientFiltering("clientId.in=" + DEFAULT_CLIENT_ID + "," + UPDATED_CLIENT_ID, "clientId.in=" + UPDATED_CLIENT_ID);
    }

    @Test
    @Transactional
    void getAllServiceClientsByClientIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where clientId is not null
        defaultServiceClientFiltering("clientId.specified=true", "clientId.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where type equals to
        defaultServiceClientFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where type in
        defaultServiceClientFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where type is not null
        defaultServiceClientFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByApiKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where apiKey equals to
        defaultServiceClientFiltering("apiKey.equals=" + DEFAULT_API_KEY, "apiKey.equals=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllServiceClientsByApiKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where apiKey in
        defaultServiceClientFiltering("apiKey.in=" + DEFAULT_API_KEY + "," + UPDATED_API_KEY, "apiKey.in=" + UPDATED_API_KEY);
    }

    @Test
    @Transactional
    void getAllServiceClientsByApiKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where apiKey is not null
        defaultServiceClientFiltering("apiKey.specified=true", "apiKey.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where status equals to
        defaultServiceClientFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceClientsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where status in
        defaultServiceClientFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceClientsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where status is not null
        defaultServiceClientFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where note equals to
        defaultServiceClientFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where note in
        defaultServiceClientFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where note is not null
        defaultServiceClientFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByNoteContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where note contains
        defaultServiceClientFiltering("note.contains=" + DEFAULT_NOTE, "note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where note does not contain
        defaultServiceClientFiltering("note.doesNotContain=" + UPDATED_NOTE, "note.doesNotContain=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where createdDate equals to
        defaultServiceClientFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceClientsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where createdDate in
        defaultServiceClientFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceClientsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where createdDate is not null
        defaultServiceClientFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceClientsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where lastModifiedDate equals to
        defaultServiceClientFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceClientsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where lastModifiedDate in
        defaultServiceClientFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllServiceClientsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        // Get all the serviceClientList where lastModifiedDate is not null
        defaultServiceClientFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultServiceClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceClientShouldBeFound(shouldBeFound);
        defaultServiceClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceClientShouldBeFound(String filter) throws Exception {
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceClientShouldNotBeFound(String filter) throws Exception {
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceClient() throws Exception {
        // Get the serviceClient
        restServiceClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceClient() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceClient
        ServiceClient updatedServiceClient = serviceClientRepository.findById(serviceClient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceClient are not directly saved in db
        em.detach(updatedServiceClient);
        updatedServiceClient
            .clientId(UPDATED_CLIENT_ID)
            .type(UPDATED_TYPE)
            .apiKey(UPDATED_API_KEY)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedServiceClient))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceClientToMatchAllProperties(updatedServiceClient);
    }

    @Test
    @Transactional
    void putNonExistingServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceClient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceClientWithPatch() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceClient using partial update
        ServiceClient partialUpdatedServiceClient = new ServiceClient();
        partialUpdatedServiceClient.setId(serviceClient.getId());

        partialUpdatedServiceClient
            .clientId(UPDATED_CLIENT_ID)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceClient))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceClientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceClient, serviceClient),
            getPersistedServiceClient(serviceClient)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceClientWithPatch() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceClient using partial update
        ServiceClient partialUpdatedServiceClient = new ServiceClient();
        partialUpdatedServiceClient.setId(serviceClient.getId());

        partialUpdatedServiceClient
            .clientId(UPDATED_CLIENT_ID)
            .type(UPDATED_TYPE)
            .apiKey(UPDATED_API_KEY)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restServiceClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceClient))
            )
            .andExpect(status().isOk());

        // Validate the ServiceClient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceClientUpdatableFieldsEquals(partialUpdatedServiceClient, getPersistedServiceClient(partialUpdatedServiceClient));
    }

    @Test
    @Transactional
    void patchNonExistingServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceClient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceClient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceClient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceClient() throws Exception {
        // Initialize the database
        insertedServiceClient = serviceClientRepository.saveAndFlush(serviceClient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceClient
        restServiceClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceClientRepository.count();
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

    protected ServiceClient getPersistedServiceClient(ServiceClient serviceClient) {
        return serviceClientRepository.findById(serviceClient.getId()).orElseThrow();
    }

    protected void assertPersistedServiceClientToMatchAllProperties(ServiceClient expectedServiceClient) {
        assertServiceClientAllPropertiesEquals(expectedServiceClient, getPersistedServiceClient(expectedServiceClient));
    }

    protected void assertPersistedServiceClientToMatchUpdatableProperties(ServiceClient expectedServiceClient) {
        assertServiceClientAllUpdatablePropertiesEquals(expectedServiceClient, getPersistedServiceClient(expectedServiceClient));
    }
}
