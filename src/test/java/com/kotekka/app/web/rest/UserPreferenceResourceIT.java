package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.UserPreferenceAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.User;
import com.kotekka.app.domain.UserPreference;
import com.kotekka.app.domain.enumeration.App;
import com.kotekka.app.repository.UserPreferenceRepository;
import com.kotekka.app.repository.UserRepository;
import com.kotekka.app.service.UserPreferenceService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserPreferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserPreferenceResourceIT {

    private static final App DEFAULT_APP = App.MOBILE;
    private static final App UPDATED_APP = App.WEB;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SETTING = "AAAAAAAAAA";
    private static final String UPDATED_SETTING = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserPreferenceRepository userPreferenceRepositoryMock;

    @Mock
    private UserPreferenceService userPreferenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPreferenceMockMvc;

    private UserPreference userPreference;

    private UserPreference insertedUserPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreference createEntity() {
        return new UserPreference().app(DEFAULT_APP).name(DEFAULT_NAME).setting(DEFAULT_SETTING).createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreference createUpdatedEntity() {
        return new UserPreference().app(UPDATED_APP).name(UPDATED_NAME).setting(UPDATED_SETTING).createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        userPreference = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserPreference != null) {
            userPreferenceRepository.delete(insertedUserPreference);
            insertedUserPreference = null;
        }
    }

    @Test
    @Transactional
    void createUserPreference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserPreference
        var returnedUserPreference = om.readValue(
            restUserPreferenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreference)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserPreference.class
        );

        // Validate the UserPreference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserPreferenceUpdatableFieldsEquals(returnedUserPreference, getPersistedUserPreference(returnedUserPreference));

        insertedUserPreference = returnedUserPreference;
    }

    @Test
    @Transactional
    void createUserPreferenceWithExistingId() throws Exception {
        // Create the UserPreference with an existing ID
        userPreference.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreference)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].app").value(hasItem(DEFAULT_APP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].setting").value(hasItem(DEFAULT_SETTING)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPreferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userPreferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPreferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userPreferenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get the userPreference
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, userPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPreference.getId().intValue()))
            .andExpect(jsonPath("$.app").value(DEFAULT_APP.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.setting").value(DEFAULT_SETTING))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getUserPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        Long id = userPreference.getId();

        defaultUserPreferenceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserPreferenceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserPreferenceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByAppIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where app equals to
        defaultUserPreferenceFiltering("app.equals=" + DEFAULT_APP, "app.equals=" + UPDATED_APP);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByAppIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where app in
        defaultUserPreferenceFiltering("app.in=" + DEFAULT_APP + "," + UPDATED_APP, "app.in=" + UPDATED_APP);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByAppIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where app is not null
        defaultUserPreferenceFiltering("app.specified=true", "app.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where name equals to
        defaultUserPreferenceFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where name in
        defaultUserPreferenceFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where name is not null
        defaultUserPreferenceFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where name contains
        defaultUserPreferenceFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where name does not contain
        defaultUserPreferenceFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySettingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where setting equals to
        defaultUserPreferenceFiltering("setting.equals=" + DEFAULT_SETTING, "setting.equals=" + UPDATED_SETTING);
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySettingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where setting in
        defaultUserPreferenceFiltering("setting.in=" + DEFAULT_SETTING + "," + UPDATED_SETTING, "setting.in=" + UPDATED_SETTING);
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySettingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where setting is not null
        defaultUserPreferenceFiltering("setting.specified=true", "setting.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySettingContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where setting contains
        defaultUserPreferenceFiltering("setting.contains=" + DEFAULT_SETTING, "setting.contains=" + UPDATED_SETTING);
    }

    @Test
    @Transactional
    void getAllUserPreferencesBySettingNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where setting does not contain
        defaultUserPreferenceFiltering("setting.doesNotContain=" + UPDATED_SETTING, "setting.doesNotContain=" + DEFAULT_SETTING);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where createdDate equals to
        defaultUserPreferenceFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserPreferencesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where createdDate in
        defaultUserPreferenceFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserPreferencesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList where createdDate is not null
        defaultUserPreferenceFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPreferencesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userPreferenceRepository.saveAndFlush(userPreference);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userPreference.setUser(user);
        userPreferenceRepository.saveAndFlush(userPreference);
        Long userId = user.getId();
        // Get all the userPreferenceList where user equals to userId
        defaultUserPreferenceShouldBeFound("userId.equals=" + userId);

        // Get all the userPreferenceList where user equals to (userId + 1)
        defaultUserPreferenceShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultUserPreferenceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserPreferenceShouldBeFound(shouldBeFound);
        defaultUserPreferenceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserPreferenceShouldBeFound(String filter) throws Exception {
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].app").value(hasItem(DEFAULT_APP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].setting").value(hasItem(DEFAULT_SETTING)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserPreferenceShouldNotBeFound(String filter) throws Exception {
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserPreference() throws Exception {
        // Get the userPreference
        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference
        UserPreference updatedUserPreference = userPreferenceRepository.findById(userPreference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserPreference are not directly saved in db
        em.detach(updatedUserPreference);
        updatedUserPreference.app(UPDATED_APP).name(UPDATED_NAME).setting(UPDATED_SETTING).createdDate(UPDATED_CREATED_DATE);

        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserPreference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserPreference))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserPreferenceToMatchAllProperties(updatedUserPreference);
    }

    @Test
    @Transactional
    void putNonExistingUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPreference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference using partial update
        UserPreference partialUpdatedUserPreference = new UserPreference();
        partialUpdatedUserPreference.setId(userPreference.getId());

        partialUpdatedUserPreference.app(UPDATED_APP).name(UPDATED_NAME).setting(UPDATED_SETTING);

        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreference))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserPreference, userPreference),
            getPersistedUserPreference(userPreference)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference using partial update
        UserPreference partialUpdatedUserPreference = new UserPreference();
        partialUpdatedUserPreference.setId(userPreference.getId());

        partialUpdatedUserPreference.app(UPDATED_APP).name(UPDATED_NAME).setting(UPDATED_SETTING).createdDate(UPDATED_CREATED_DATE);

        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreference))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferenceUpdatableFieldsEquals(partialUpdatedUserPreference, getPersistedUserPreference(partialUpdatedUserPreference));
    }

    @Test
    @Transactional
    void patchNonExistingUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userPreference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userPreference
        restUserPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userPreferenceRepository.count();
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

    protected UserPreference getPersistedUserPreference(UserPreference userPreference) {
        return userPreferenceRepository.findById(userPreference.getId()).orElseThrow();
    }

    protected void assertPersistedUserPreferenceToMatchAllProperties(UserPreference expectedUserPreference) {
        assertUserPreferenceAllPropertiesEquals(expectedUserPreference, getPersistedUserPreference(expectedUserPreference));
    }

    protected void assertPersistedUserPreferenceToMatchUpdatableProperties(UserPreference expectedUserPreference) {
        assertUserPreferenceAllUpdatablePropertiesEquals(expectedUserPreference, getPersistedUserPreference(expectedUserPreference));
    }
}
