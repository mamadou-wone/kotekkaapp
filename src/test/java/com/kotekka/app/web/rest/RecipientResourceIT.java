package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.RecipientAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Recipient;
import com.kotekka.app.domain.enumeration.DefaultStatus;
import com.kotekka.app.repository.RecipientRepository;
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
 * Integration tests for the {@link RecipientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipientResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final DefaultStatus DEFAULT_STATUS = DefaultStatus.PENDING;
    private static final DefaultStatus UPDATED_STATUS = DefaultStatus.ACTIVE;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/recipients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipientMockMvc;

    private Recipient recipient;

    private Recipient insertedRecipient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipient createEntity() {
        return new Recipient()
            .uuid(DEFAULT_UUID)
            .status(DEFAULT_STATUS)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
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
    public static Recipient createUpdatedEntity() {
        return new Recipient()
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        recipient = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRecipient != null) {
            recipientRepository.delete(insertedRecipient);
            insertedRecipient = null;
        }
    }

    @Test
    @Transactional
    void createRecipient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recipient
        var returnedRecipient = om.readValue(
            restRecipientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipient)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Recipient.class
        );

        // Validate the Recipient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecipientUpdatableFieldsEquals(returnedRecipient, getPersistedRecipient(returnedRecipient));

        insertedRecipient = returnedRecipient;
    }

    @Test
    @Transactional
    void createRecipientWithExistingId() throws Exception {
        // Create the Recipient with an existing ID
        recipient.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipient)))
            .andExpect(status().isBadRequest());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipient.setUuid(null);

        // Create the Recipient, which fails.

        restRecipientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipients() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        // Get all the recipientList
        restRecipientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipient.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRecipient() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        // Get the recipient
        restRecipientMockMvc
            .perform(get(ENTITY_API_URL_ID, recipient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipient.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRecipient() throws Exception {
        // Get the recipient
        restRecipientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipient() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipient
        Recipient updatedRecipient = recipientRepository.findById(recipient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecipient are not directly saved in db
        em.detach(updatedRecipient);
        updatedRecipient
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRecipientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecipient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecipient))
            )
            .andExpect(status().isOk());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipientToMatchAllProperties(updatedRecipient);
    }

    @Test
    @Transactional
    void putNonExistingRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipient.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipientWithPatch() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipient using partial update
        Recipient partialUpdatedRecipient = new Recipient();
        partialUpdatedRecipient.setId(recipient.getId());

        partialUpdatedRecipient
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restRecipientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipient))
            )
            .andExpect(status().isOk());

        // Validate the Recipient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecipient, recipient),
            getPersistedRecipient(recipient)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecipientWithPatch() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipient using partial update
        Recipient partialUpdatedRecipient = new Recipient();
        partialUpdatedRecipient.setId(recipient.getId());

        partialUpdatedRecipient
            .uuid(UPDATED_UUID)
            .status(UPDATED_STATUS)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restRecipientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipient))
            )
            .andExpect(status().isOk());

        // Validate the Recipient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipientUpdatableFieldsEquals(partialUpdatedRecipient, getPersistedRecipient(partialUpdatedRecipient));
    }

    @Test
    @Transactional
    void patchNonExistingRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipient() throws Exception {
        // Initialize the database
        insertedRecipient = recipientRepository.saveAndFlush(recipient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipient
        restRecipientMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipientRepository.count();
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

    protected Recipient getPersistedRecipient(Recipient recipient) {
        return recipientRepository.findById(recipient.getId()).orElseThrow();
    }

    protected void assertPersistedRecipientToMatchAllProperties(Recipient expectedRecipient) {
        assertRecipientAllPropertiesEquals(expectedRecipient, getPersistedRecipient(expectedRecipient));
    }

    protected void assertPersistedRecipientToMatchUpdatableProperties(Recipient expectedRecipient) {
        assertRecipientAllUpdatablePropertiesEquals(expectedRecipient, getPersistedRecipient(expectedRecipient));
    }
}
