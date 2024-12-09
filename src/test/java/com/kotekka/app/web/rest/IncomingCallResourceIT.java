package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.IncomingCallAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.IncomingCall;
import com.kotekka.app.domain.enumeration.HttpMethod;
import com.kotekka.app.domain.enumeration.Partner;
import com.kotekka.app.repository.IncomingCallRepository;
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
 * Integration tests for the {@link IncomingCallResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncomingCallResourceIT {

    private static final Partner DEFAULT_PARTNER = Partner.CIH;
    private static final Partner UPDATED_PARTNER = Partner.CMI;

    private static final String DEFAULT_API = "AAAAAAAAAA";
    private static final String UPDATED_API = "BBBBBBBBBB";

    private static final HttpMethod DEFAULT_METHOD = HttpMethod.GET;
    private static final HttpMethod UPDATED_METHOD = HttpMethod.POST;

    private static final String DEFAULT_REQUEST_HEADERS = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_HEADERS = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_BODY = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RESPONSE_STATUS_CODE = 999;
    private static final Integer UPDATED_RESPONSE_STATUS_CODE = 998;
    private static final Integer SMALLER_RESPONSE_STATUS_CODE = 999 - 1;

    private static final Instant DEFAULT_RESPONSE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONSE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/incoming-calls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncomingCallRepository incomingCallRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomingCallMockMvc;

    private IncomingCall incomingCall;

    private IncomingCall insertedIncomingCall;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomingCall createEntity() {
        return new IncomingCall()
            .partner(DEFAULT_PARTNER)
            .api(DEFAULT_API)
            .method(DEFAULT_METHOD)
            .requestHeaders(DEFAULT_REQUEST_HEADERS)
            .requestBody(DEFAULT_REQUEST_BODY)
            .createdDate(DEFAULT_CREATED_DATE)
            .responseStatusCode(DEFAULT_RESPONSE_STATUS_CODE)
            .responseTime(DEFAULT_RESPONSE_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomingCall createUpdatedEntity() {
        return new IncomingCall()
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .createdDate(UPDATED_CREATED_DATE)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseTime(UPDATED_RESPONSE_TIME);
    }

    @BeforeEach
    public void initTest() {
        incomingCall = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIncomingCall != null) {
            incomingCallRepository.delete(insertedIncomingCall);
            insertedIncomingCall = null;
        }
    }

    @Test
    @Transactional
    void createIncomingCall() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IncomingCall
        var returnedIncomingCall = om.readValue(
            restIncomingCallMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomingCall)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncomingCall.class
        );

        // Validate the IncomingCall in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIncomingCallUpdatableFieldsEquals(returnedIncomingCall, getPersistedIncomingCall(returnedIncomingCall));

        insertedIncomingCall = returnedIncomingCall;
    }

    @Test
    @Transactional
    void createIncomingCallWithExistingId() throws Exception {
        // Create the IncomingCall with an existing ID
        incomingCall.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomingCallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomingCall)))
            .andExpect(status().isBadRequest());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIncomingCalls() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomingCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.toString())))
            .andExpect(jsonPath("$.[*].api").value(hasItem(DEFAULT_API)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].requestHeaders").value(hasItem(DEFAULT_REQUEST_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseStatusCode").value(hasItem(DEFAULT_RESPONSE_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())));
    }

    @Test
    @Transactional
    void getIncomingCall() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get the incomingCall
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL_ID, incomingCall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomingCall.getId().intValue()))
            .andExpect(jsonPath("$.partner").value(DEFAULT_PARTNER.toString()))
            .andExpect(jsonPath("$.api").value(DEFAULT_API))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.requestHeaders").value(DEFAULT_REQUEST_HEADERS.toString()))
            .andExpect(jsonPath("$.requestBody").value(DEFAULT_REQUEST_BODY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.responseStatusCode").value(DEFAULT_RESPONSE_STATUS_CODE))
            .andExpect(jsonPath("$.responseTime").value(DEFAULT_RESPONSE_TIME.toString()));
    }

    @Test
    @Transactional
    void getIncomingCallsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        Long id = incomingCall.getId();

        defaultIncomingCallFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIncomingCallFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIncomingCallFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByPartnerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where partner equals to
        defaultIncomingCallFiltering("partner.equals=" + DEFAULT_PARTNER, "partner.equals=" + UPDATED_PARTNER);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByPartnerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where partner in
        defaultIncomingCallFiltering("partner.in=" + DEFAULT_PARTNER + "," + UPDATED_PARTNER, "partner.in=" + UPDATED_PARTNER);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByPartnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where partner is not null
        defaultIncomingCallFiltering("partner.specified=true", "partner.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomingCallsByApiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where api equals to
        defaultIncomingCallFiltering("api.equals=" + DEFAULT_API, "api.equals=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByApiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where api in
        defaultIncomingCallFiltering("api.in=" + DEFAULT_API + "," + UPDATED_API, "api.in=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByApiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where api is not null
        defaultIncomingCallFiltering("api.specified=true", "api.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomingCallsByApiContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where api contains
        defaultIncomingCallFiltering("api.contains=" + DEFAULT_API, "api.contains=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByApiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where api does not contain
        defaultIncomingCallFiltering("api.doesNotContain=" + UPDATED_API, "api.doesNotContain=" + DEFAULT_API);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where method equals to
        defaultIncomingCallFiltering("method.equals=" + DEFAULT_METHOD, "method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where method in
        defaultIncomingCallFiltering("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD, "method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where method is not null
        defaultIncomingCallFiltering("method.specified=true", "method.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomingCallsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where createdDate equals to
        defaultIncomingCallFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where createdDate in
        defaultIncomingCallFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where createdDate is not null
        defaultIncomingCallFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode equals to
        defaultIncomingCallFiltering(
            "responseStatusCode.equals=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.equals=" + UPDATED_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode in
        defaultIncomingCallFiltering(
            "responseStatusCode.in=" + DEFAULT_RESPONSE_STATUS_CODE + "," + UPDATED_RESPONSE_STATUS_CODE,
            "responseStatusCode.in=" + UPDATED_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode is not null
        defaultIncomingCallFiltering("responseStatusCode.specified=true", "responseStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode is greater than or equal to
        defaultIncomingCallFiltering(
            "responseStatusCode.greaterThanOrEqual=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.greaterThanOrEqual=" + (DEFAULT_RESPONSE_STATUS_CODE + 1)
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode is less than or equal to
        defaultIncomingCallFiltering(
            "responseStatusCode.lessThanOrEqual=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.lessThanOrEqual=" + SMALLER_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode is less than
        defaultIncomingCallFiltering(
            "responseStatusCode.lessThan=" + (DEFAULT_RESPONSE_STATUS_CODE + 1),
            "responseStatusCode.lessThan=" + DEFAULT_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseStatusCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseStatusCode is greater than
        defaultIncomingCallFiltering(
            "responseStatusCode.greaterThan=" + SMALLER_RESPONSE_STATUS_CODE,
            "responseStatusCode.greaterThan=" + DEFAULT_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseTime equals to
        defaultIncomingCallFiltering("responseTime.equals=" + DEFAULT_RESPONSE_TIME, "responseTime.equals=" + UPDATED_RESPONSE_TIME);
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseTime in
        defaultIncomingCallFiltering(
            "responseTime.in=" + DEFAULT_RESPONSE_TIME + "," + UPDATED_RESPONSE_TIME,
            "responseTime.in=" + UPDATED_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllIncomingCallsByResponseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        // Get all the incomingCallList where responseTime is not null
        defaultIncomingCallFiltering("responseTime.specified=true", "responseTime.specified=false");
    }

    private void defaultIncomingCallFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIncomingCallShouldBeFound(shouldBeFound);
        defaultIncomingCallShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIncomingCallShouldBeFound(String filter) throws Exception {
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomingCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.toString())))
            .andExpect(jsonPath("$.[*].api").value(hasItem(DEFAULT_API)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].requestHeaders").value(hasItem(DEFAULT_REQUEST_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].responseStatusCode").value(hasItem(DEFAULT_RESPONSE_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())));

        // Check, that the count call also returns 1
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIncomingCallShouldNotBeFound(String filter) throws Exception {
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIncomingCallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIncomingCall() throws Exception {
        // Get the incomingCall
        restIncomingCallMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncomingCall() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incomingCall
        IncomingCall updatedIncomingCall = incomingCallRepository.findById(incomingCall.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIncomingCall are not directly saved in db
        em.detach(updatedIncomingCall);
        updatedIncomingCall
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .createdDate(UPDATED_CREATED_DATE)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseTime(UPDATED_RESPONSE_TIME);

        restIncomingCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncomingCall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIncomingCall))
            )
            .andExpect(status().isOk());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncomingCallToMatchAllProperties(updatedIncomingCall);
    }

    @Test
    @Transactional
    void putNonExistingIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomingCall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incomingCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incomingCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incomingCall)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncomingCallWithPatch() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incomingCall using partial update
        IncomingCall partialUpdatedIncomingCall = new IncomingCall();
        partialUpdatedIncomingCall.setId(incomingCall.getId());

        partialUpdatedIncomingCall
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .createdDate(UPDATED_CREATED_DATE)
            .responseTime(UPDATED_RESPONSE_TIME);

        restIncomingCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomingCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncomingCall))
            )
            .andExpect(status().isOk());

        // Validate the IncomingCall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncomingCallUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncomingCall, incomingCall),
            getPersistedIncomingCall(incomingCall)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncomingCallWithPatch() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incomingCall using partial update
        IncomingCall partialUpdatedIncomingCall = new IncomingCall();
        partialUpdatedIncomingCall.setId(incomingCall.getId());

        partialUpdatedIncomingCall
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .createdDate(UPDATED_CREATED_DATE)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseTime(UPDATED_RESPONSE_TIME);

        restIncomingCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomingCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncomingCall))
            )
            .andExpect(status().isOk());

        // Validate the IncomingCall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncomingCallUpdatableFieldsEquals(partialUpdatedIncomingCall, getPersistedIncomingCall(partialUpdatedIncomingCall));
    }

    @Test
    @Transactional
    void patchNonExistingIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incomingCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incomingCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incomingCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncomingCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incomingCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomingCallMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(incomingCall)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomingCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncomingCall() throws Exception {
        // Initialize the database
        insertedIncomingCall = incomingCallRepository.saveAndFlush(incomingCall);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the incomingCall
        restIncomingCallMockMvc
            .perform(delete(ENTITY_API_URL_ID, incomingCall.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return incomingCallRepository.count();
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

    protected IncomingCall getPersistedIncomingCall(IncomingCall incomingCall) {
        return incomingCallRepository.findById(incomingCall.getId()).orElseThrow();
    }

    protected void assertPersistedIncomingCallToMatchAllProperties(IncomingCall expectedIncomingCall) {
        assertIncomingCallAllPropertiesEquals(expectedIncomingCall, getPersistedIncomingCall(expectedIncomingCall));
    }

    protected void assertPersistedIncomingCallToMatchUpdatableProperties(IncomingCall expectedIncomingCall) {
        assertIncomingCallAllUpdatablePropertiesEquals(expectedIncomingCall, getPersistedIncomingCall(expectedIncomingCall));
    }
}
