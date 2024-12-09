package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.UserAccessAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.UserAccess;
import com.kotekka.app.repository.UserAccessRepository;
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
 * Integration tests for the {@link UserAccessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAccessResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final UUID DEFAULT_DEVICE = UUID.randomUUID();
    private static final UUID UPDATED_DEVICE = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-accesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAccessRepository userAccessRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAccessMockMvc;

    private UserAccess userAccess;

    private UserAccess insertedUserAccess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccess createEntity() {
        return new UserAccess().login(DEFAULT_LOGIN).ipAddress(DEFAULT_IP_ADDRESS).device(DEFAULT_DEVICE).createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAccess createUpdatedEntity() {
        return new UserAccess().login(UPDATED_LOGIN).ipAddress(UPDATED_IP_ADDRESS).device(UPDATED_DEVICE).createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        userAccess = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserAccess != null) {
            userAccessRepository.delete(insertedUserAccess);
            insertedUserAccess = null;
        }
    }

    @Test
    @Transactional
    void createUserAccess() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAccess
        var returnedUserAccess = om.readValue(
            restUserAccessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccess)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAccess.class
        );

        // Validate the UserAccess in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserAccessUpdatableFieldsEquals(returnedUserAccess, getPersistedUserAccess(returnedUserAccess));

        insertedUserAccess = returnedUserAccess;
    }

    @Test
    @Transactional
    void createUserAccessWithExistingId() throws Exception {
        // Create the UserAccess with an existing ID
        userAccess.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAccessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccess)))
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAccesses() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserAccess() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get the userAccess
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL_ID, userAccess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAccess.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUserAccessesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        Long id = userAccess.getId();

        defaultUserAccessFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserAccessFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserAccessFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAccessesByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where login equals to
        defaultUserAccessFiltering("login.equals=" + DEFAULT_LOGIN, "login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllUserAccessesByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where login in
        defaultUserAccessFiltering("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN, "login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllUserAccessesByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where login is not null
        defaultUserAccessFiltering("login.specified=true", "login.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByLoginContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where login contains
        defaultUserAccessFiltering("login.contains=" + DEFAULT_LOGIN, "login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void getAllUserAccessesByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where login does not contain
        defaultUserAccessFiltering("login.doesNotContain=" + UPDATED_LOGIN, "login.doesNotContain=" + DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress equals to
        defaultUserAccessFiltering("ipAddress.equals=" + DEFAULT_IP_ADDRESS, "ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress in
        defaultUserAccessFiltering("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS, "ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress is not null
        defaultUserAccessFiltering("ipAddress.specified=true", "ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress contains
        defaultUserAccessFiltering("ipAddress.contains=" + DEFAULT_IP_ADDRESS, "ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where ipAddress does not contain
        defaultUserAccessFiltering("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS, "ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserAccessesByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where device equals to
        defaultUserAccessFiltering("device.equals=" + DEFAULT_DEVICE, "device.equals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllUserAccessesByDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where device in
        defaultUserAccessFiltering("device.in=" + DEFAULT_DEVICE + "," + UPDATED_DEVICE, "device.in=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllUserAccessesByDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where device is not null
        defaultUserAccessFiltering("device.specified=true", "device.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAccessesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where createdDate equals to
        defaultUserAccessFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserAccessesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where createdDate in
        defaultUserAccessFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserAccessesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        // Get all the userAccessList where createdDate is not null
        defaultUserAccessFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultUserAccessFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserAccessShouldBeFound(shouldBeFound);
        defaultUserAccessShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAccessShouldBeFound(String filter) throws Exception {
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAccess.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAccessShouldNotBeFound(String filter) throws Exception {
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAccessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAccess() throws Exception {
        // Get the userAccess
        restUserAccessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAccess() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccess
        UserAccess updatedUserAccess = userAccessRepository.findById(userAccess.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAccess are not directly saved in db
        em.detach(updatedUserAccess);
        updatedUserAccess.login(UPDATED_LOGIN).ipAddress(UPDATED_IP_ADDRESS).device(UPDATED_DEVICE).createdDate(UPDATED_CREATED_DATE);

        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAccess.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserAccess))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAccessToMatchAllProperties(updatedUserAccess);
    }

    @Test
    @Transactional
    void putNonExistingUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAccess.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccess))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAccess))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAccess)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAccessWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccess using partial update
        UserAccess partialUpdatedUserAccess = new UserAccess();
        partialUpdatedUserAccess.setId(userAccess.getId());

        partialUpdatedUserAccess.ipAddress(UPDATED_IP_ADDRESS).device(UPDATED_DEVICE).createdDate(UPDATED_CREATED_DATE);

        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccess))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccessUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAccess, userAccess),
            getPersistedUserAccess(userAccess)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAccessWithPatch() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAccess using partial update
        UserAccess partialUpdatedUserAccess = new UserAccess();
        partialUpdatedUserAccess.setId(userAccess.getId());

        partialUpdatedUserAccess
            .login(UPDATED_LOGIN)
            .ipAddress(UPDATED_IP_ADDRESS)
            .device(UPDATED_DEVICE)
            .createdDate(UPDATED_CREATED_DATE);

        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAccess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAccess))
            )
            .andExpect(status().isOk());

        // Validate the UserAccess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAccessUpdatableFieldsEquals(partialUpdatedUserAccess, getPersistedUserAccess(partialUpdatedUserAccess));
    }

    @Test
    @Transactional
    void patchNonExistingUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAccess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccess))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAccess))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAccess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAccess.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAccessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAccess)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAccess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAccess() throws Exception {
        // Initialize the database
        insertedUserAccess = userAccessRepository.saveAndFlush(userAccess);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAccess
        restUserAccessMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAccess.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAccessRepository.count();
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

    protected UserAccess getPersistedUserAccess(UserAccess userAccess) {
        return userAccessRepository.findById(userAccess.getId()).orElseThrow();
    }

    protected void assertPersistedUserAccessToMatchAllProperties(UserAccess expectedUserAccess) {
        assertUserAccessAllPropertiesEquals(expectedUserAccess, getPersistedUserAccess(expectedUserAccess));
    }

    protected void assertPersistedUserAccessToMatchUpdatableProperties(UserAccess expectedUserAccess) {
        assertUserAccessAllUpdatablePropertiesEquals(expectedUserAccess, getPersistedUserAccess(expectedUserAccess));
    }
}
