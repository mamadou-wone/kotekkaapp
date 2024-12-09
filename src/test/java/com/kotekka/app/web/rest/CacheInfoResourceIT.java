package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.CacheInfoAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.CacheInfo;
import com.kotekka.app.repository.CacheInfoRepository;
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
 * Integration tests for the {@link CacheInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CacheInfoResourceIT {

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cache-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CacheInfoRepository cacheInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCacheInfoMockMvc;

    private CacheInfo cacheInfo;

    private CacheInfo insertedCacheInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CacheInfo createEntity() {
        return new CacheInfo().walletHolder(DEFAULT_WALLET_HOLDER).key(DEFAULT_KEY).value(DEFAULT_VALUE).createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CacheInfo createUpdatedEntity() {
        return new CacheInfo().walletHolder(UPDATED_WALLET_HOLDER).key(UPDATED_KEY).value(UPDATED_VALUE).createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        cacheInfo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCacheInfo != null) {
            cacheInfoRepository.delete(insertedCacheInfo);
            insertedCacheInfo = null;
        }
    }

    @Test
    @Transactional
    void createCacheInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CacheInfo
        var returnedCacheInfo = om.readValue(
            restCacheInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cacheInfo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CacheInfo.class
        );

        // Validate the CacheInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCacheInfoUpdatableFieldsEquals(returnedCacheInfo, getPersistedCacheInfo(returnedCacheInfo));

        insertedCacheInfo = returnedCacheInfo;
    }

    @Test
    @Transactional
    void createCacheInfoWithExistingId() throws Exception {
        // Create the CacheInfo with an existing ID
        cacheInfo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCacheInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cacheInfo)))
            .andExpect(status().isBadRequest());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCacheInfos() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cacheInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCacheInfo() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get the cacheInfo
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, cacheInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cacheInfo.getId().intValue()))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCacheInfosByIdFiltering() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        Long id = cacheInfo.getId();

        defaultCacheInfoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCacheInfoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCacheInfoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCacheInfosByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where walletHolder equals to
        defaultCacheInfoFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllCacheInfosByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where walletHolder in
        defaultCacheInfoFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllCacheInfosByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where walletHolder is not null
        defaultCacheInfoFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllCacheInfosByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where key equals to
        defaultCacheInfoFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllCacheInfosByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where key in
        defaultCacheInfoFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllCacheInfosByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where key is not null
        defaultCacheInfoFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllCacheInfosByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where key contains
        defaultCacheInfoFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllCacheInfosByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where key does not contain
        defaultCacheInfoFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllCacheInfosByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where createdDate equals to
        defaultCacheInfoFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCacheInfosByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where createdDate in
        defaultCacheInfoFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCacheInfosByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        // Get all the cacheInfoList where createdDate is not null
        defaultCacheInfoFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultCacheInfoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCacheInfoShouldBeFound(shouldBeFound);
        defaultCacheInfoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCacheInfoShouldBeFound(String filter) throws Exception {
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cacheInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCacheInfoShouldNotBeFound(String filter) throws Exception {
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCacheInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCacheInfo() throws Exception {
        // Get the cacheInfo
        restCacheInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCacheInfo() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cacheInfo
        CacheInfo updatedCacheInfo = cacheInfoRepository.findById(cacheInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCacheInfo are not directly saved in db
        em.detach(updatedCacheInfo);
        updatedCacheInfo.walletHolder(UPDATED_WALLET_HOLDER).key(UPDATED_KEY).value(UPDATED_VALUE).createdDate(UPDATED_CREATED_DATE);

        restCacheInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCacheInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCacheInfo))
            )
            .andExpect(status().isOk());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCacheInfoToMatchAllProperties(updatedCacheInfo);
    }

    @Test
    @Transactional
    void putNonExistingCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cacheInfo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cacheInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cacheInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cacheInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCacheInfoWithPatch() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cacheInfo using partial update
        CacheInfo partialUpdatedCacheInfo = new CacheInfo();
        partialUpdatedCacheInfo.setId(cacheInfo.getId());

        partialUpdatedCacheInfo.key(UPDATED_KEY).createdDate(UPDATED_CREATED_DATE);

        restCacheInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCacheInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCacheInfo))
            )
            .andExpect(status().isOk());

        // Validate the CacheInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCacheInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCacheInfo, cacheInfo),
            getPersistedCacheInfo(cacheInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateCacheInfoWithPatch() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cacheInfo using partial update
        CacheInfo partialUpdatedCacheInfo = new CacheInfo();
        partialUpdatedCacheInfo.setId(cacheInfo.getId());

        partialUpdatedCacheInfo.walletHolder(UPDATED_WALLET_HOLDER).key(UPDATED_KEY).value(UPDATED_VALUE).createdDate(UPDATED_CREATED_DATE);

        restCacheInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCacheInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCacheInfo))
            )
            .andExpect(status().isOk());

        // Validate the CacheInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCacheInfoUpdatableFieldsEquals(partialUpdatedCacheInfo, getPersistedCacheInfo(partialUpdatedCacheInfo));
    }

    @Test
    @Transactional
    void patchNonExistingCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cacheInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cacheInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cacheInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCacheInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cacheInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCacheInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cacheInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CacheInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCacheInfo() throws Exception {
        // Initialize the database
        insertedCacheInfo = cacheInfoRepository.saveAndFlush(cacheInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cacheInfo
        restCacheInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cacheInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cacheInfoRepository.count();
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

    protected CacheInfo getPersistedCacheInfo(CacheInfo cacheInfo) {
        return cacheInfoRepository.findById(cacheInfo.getId()).orElseThrow();
    }

    protected void assertPersistedCacheInfoToMatchAllProperties(CacheInfo expectedCacheInfo) {
        assertCacheInfoAllPropertiesEquals(expectedCacheInfo, getPersistedCacheInfo(expectedCacheInfo));
    }

    protected void assertPersistedCacheInfoToMatchUpdatableProperties(CacheInfo expectedCacheInfo) {
        assertCacheInfoAllUpdatablePropertiesEquals(expectedCacheInfo, getPersistedCacheInfo(expectedCacheInfo));
    }
}
