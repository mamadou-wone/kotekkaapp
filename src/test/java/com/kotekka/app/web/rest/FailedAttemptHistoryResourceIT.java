package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.FailedAttemptHistoryAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.FailedAttemptHistory;
import com.kotekka.app.domain.enumeration.Action;
import com.kotekka.app.domain.enumeration.App;
import com.kotekka.app.repository.FailedAttemptHistoryRepository;
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
 * Integration tests for the {@link FailedAttemptHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FailedAttemptHistoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/failed-attempt-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FailedAttemptHistoryRepository failedAttemptHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFailedAttemptHistoryMockMvc;

    private FailedAttemptHistory failedAttemptHistory;

    private FailedAttemptHistory insertedFailedAttemptHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FailedAttemptHistory createEntity() {
        return new FailedAttemptHistory()
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
    public static FailedAttemptHistory createUpdatedEntity() {
        return new FailedAttemptHistory()
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
        failedAttemptHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFailedAttemptHistory != null) {
            failedAttemptHistoryRepository.delete(insertedFailedAttemptHistory);
            insertedFailedAttemptHistory = null;
        }
    }

    @Test
    @Transactional
    void createFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FailedAttemptHistory
        var returnedFailedAttemptHistory = om.readValue(
            restFailedAttemptHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttemptHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FailedAttemptHistory.class
        );

        // Validate the FailedAttemptHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFailedAttemptHistoryUpdatableFieldsEquals(
            returnedFailedAttemptHistory,
            getPersistedFailedAttemptHistory(returnedFailedAttemptHistory)
        );

        insertedFailedAttemptHistory = returnedFailedAttemptHistory;
    }

    @Test
    @Transactional
    void createFailedAttemptHistoryWithExistingId() throws Exception {
        // Create the FailedAttemptHistory with an existing ID
        failedAttemptHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFailedAttemptHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttemptHistory)))
            .andExpect(status().isBadRequest());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFailedAttemptHistories() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        // Get all the failedAttemptHistoryList
        restFailedAttemptHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failedAttemptHistory.getId().intValue())))
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
    void getFailedAttemptHistory() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        // Get the failedAttemptHistory
        restFailedAttemptHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, failedAttemptHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(failedAttemptHistory.getId().intValue()))
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
    void getNonExistingFailedAttemptHistory() throws Exception {
        // Get the failedAttemptHistory
        restFailedAttemptHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFailedAttemptHistory() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttemptHistory
        FailedAttemptHistory updatedFailedAttemptHistory = failedAttemptHistoryRepository
            .findById(failedAttemptHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFailedAttemptHistory are not directly saved in db
        em.detach(updatedFailedAttemptHistory);
        updatedFailedAttemptHistory
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .app(UPDATED_APP)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE)
            .reason(UPDATED_REASON);

        restFailedAttemptHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFailedAttemptHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFailedAttemptHistory))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFailedAttemptHistoryToMatchAllProperties(updatedFailedAttemptHistory);
    }

    @Test
    @Transactional
    void putNonExistingFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, failedAttemptHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(failedAttemptHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(failedAttemptHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(failedAttemptHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFailedAttemptHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttemptHistory using partial update
        FailedAttemptHistory partialUpdatedFailedAttemptHistory = new FailedAttemptHistory();
        partialUpdatedFailedAttemptHistory.setId(failedAttemptHistory.getId());

        partialUpdatedFailedAttemptHistory
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE);

        restFailedAttemptHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedAttemptHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFailedAttemptHistory))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttemptHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFailedAttemptHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFailedAttemptHistory, failedAttemptHistory),
            getPersistedFailedAttemptHistory(failedAttemptHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateFailedAttemptHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the failedAttemptHistory using partial update
        FailedAttemptHistory partialUpdatedFailedAttemptHistory = new FailedAttemptHistory();
        partialUpdatedFailedAttemptHistory.setId(failedAttemptHistory.getId());

        partialUpdatedFailedAttemptHistory
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .isAfterLock(UPDATED_IS_AFTER_LOCK)
            .app(UPDATED_APP)
            .action(UPDATED_ACTION)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE)
            .reason(UPDATED_REASON);

        restFailedAttemptHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedAttemptHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFailedAttemptHistory))
            )
            .andExpect(status().isOk());

        // Validate the FailedAttemptHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFailedAttemptHistoryUpdatableFieldsEquals(
            partialUpdatedFailedAttemptHistory,
            getPersistedFailedAttemptHistory(partialUpdatedFailedAttemptHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, failedAttemptHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(failedAttemptHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(failedAttemptHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFailedAttemptHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        failedAttemptHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedAttemptHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(failedAttemptHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedAttemptHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFailedAttemptHistory() throws Exception {
        // Initialize the database
        insertedFailedAttemptHistory = failedAttemptHistoryRepository.saveAndFlush(failedAttemptHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the failedAttemptHistory
        restFailedAttemptHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, failedAttemptHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return failedAttemptHistoryRepository.count();
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

    protected FailedAttemptHistory getPersistedFailedAttemptHistory(FailedAttemptHistory failedAttemptHistory) {
        return failedAttemptHistoryRepository.findById(failedAttemptHistory.getId()).orElseThrow();
    }

    protected void assertPersistedFailedAttemptHistoryToMatchAllProperties(FailedAttemptHistory expectedFailedAttemptHistory) {
        assertFailedAttemptHistoryAllPropertiesEquals(
            expectedFailedAttemptHistory,
            getPersistedFailedAttemptHistory(expectedFailedAttemptHistory)
        );
    }

    protected void assertPersistedFailedAttemptHistoryToMatchUpdatableProperties(FailedAttemptHistory expectedFailedAttemptHistory) {
        assertFailedAttemptHistoryAllUpdatablePropertiesEquals(
            expectedFailedAttemptHistory,
            getPersistedFailedAttemptHistory(expectedFailedAttemptHistory)
        );
    }
}
