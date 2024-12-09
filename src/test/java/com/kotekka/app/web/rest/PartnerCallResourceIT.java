package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.PartnerCallAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.PartnerCall;
import com.kotekka.app.domain.enumeration.HttpMethod;
import com.kotekka.app.domain.enumeration.Partner;
import com.kotekka.app.repository.PartnerCallRepository;
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
 * Integration tests for the {@link PartnerCallResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartnerCallResourceIT {

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

    private static final Instant DEFAULT_REQUEST_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RESPONSE_STATUS_CODE = 999;
    private static final Integer UPDATED_RESPONSE_STATUS_CODE = 998;
    private static final Integer SMALLER_RESPONSE_STATUS_CODE = 999 - 1;

    private static final String DEFAULT_RESPONSE_HEADERS = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_HEADERS = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESPONSE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONSE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CORRELATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CORRELATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY_PARAM = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_PARAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/partner-calls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartnerCallRepository partnerCallRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerCallMockMvc;

    private PartnerCall partnerCall;

    private PartnerCall insertedPartnerCall;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerCall createEntity() {
        return new PartnerCall()
            .partner(DEFAULT_PARTNER)
            .api(DEFAULT_API)
            .method(DEFAULT_METHOD)
            .requestHeaders(DEFAULT_REQUEST_HEADERS)
            .requestBody(DEFAULT_REQUEST_BODY)
            .requestTime(DEFAULT_REQUEST_TIME)
            .responseStatusCode(DEFAULT_RESPONSE_STATUS_CODE)
            .responseHeaders(DEFAULT_RESPONSE_HEADERS)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .responseTime(DEFAULT_RESPONSE_TIME)
            .correlationId(DEFAULT_CORRELATION_ID)
            .queryParam(DEFAULT_QUERY_PARAM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerCall createUpdatedEntity() {
        return new PartnerCall()
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .requestTime(UPDATED_REQUEST_TIME)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseHeaders(UPDATED_RESPONSE_HEADERS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .correlationId(UPDATED_CORRELATION_ID)
            .queryParam(UPDATED_QUERY_PARAM);
    }

    @BeforeEach
    public void initTest() {
        partnerCall = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPartnerCall != null) {
            partnerCallRepository.delete(insertedPartnerCall);
            insertedPartnerCall = null;
        }
    }

    @Test
    @Transactional
    void createPartnerCall() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartnerCall
        var returnedPartnerCall = om.readValue(
            restPartnerCallMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerCall)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartnerCall.class
        );

        // Validate the PartnerCall in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartnerCallUpdatableFieldsEquals(returnedPartnerCall, getPersistedPartnerCall(returnedPartnerCall));

        insertedPartnerCall = returnedPartnerCall;
    }

    @Test
    @Transactional
    void createPartnerCallWithExistingId() throws Exception {
        // Create the PartnerCall with an existing ID
        partnerCall.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerCallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerCall)))
            .andExpect(status().isBadRequest());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPartnerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partnerCall.setPartner(null);

        // Create the PartnerCall, which fails.

        restPartnerCallMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerCall)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartnerCalls() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.toString())))
            .andExpect(jsonPath("$.[*].api").value(hasItem(DEFAULT_API)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].requestHeaders").value(hasItem(DEFAULT_REQUEST_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY.toString())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(DEFAULT_REQUEST_TIME.toString())))
            .andExpect(jsonPath("$.[*].responseStatusCode").value(hasItem(DEFAULT_RESPONSE_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].responseHeaders").value(hasItem(DEFAULT_RESPONSE_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY.toString())))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID)))
            .andExpect(jsonPath("$.[*].queryParam").value(hasItem(DEFAULT_QUERY_PARAM)));
    }

    @Test
    @Transactional
    void getPartnerCall() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get the partnerCall
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL_ID, partnerCall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerCall.getId().intValue()))
            .andExpect(jsonPath("$.partner").value(DEFAULT_PARTNER.toString()))
            .andExpect(jsonPath("$.api").value(DEFAULT_API))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD.toString()))
            .andExpect(jsonPath("$.requestHeaders").value(DEFAULT_REQUEST_HEADERS.toString()))
            .andExpect(jsonPath("$.requestBody").value(DEFAULT_REQUEST_BODY.toString()))
            .andExpect(jsonPath("$.requestTime").value(DEFAULT_REQUEST_TIME.toString()))
            .andExpect(jsonPath("$.responseStatusCode").value(DEFAULT_RESPONSE_STATUS_CODE))
            .andExpect(jsonPath("$.responseHeaders").value(DEFAULT_RESPONSE_HEADERS.toString()))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY.toString()))
            .andExpect(jsonPath("$.responseTime").value(DEFAULT_RESPONSE_TIME.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID))
            .andExpect(jsonPath("$.queryParam").value(DEFAULT_QUERY_PARAM));
    }

    @Test
    @Transactional
    void getPartnerCallsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        Long id = partnerCall.getId();

        defaultPartnerCallFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPartnerCallFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPartnerCallFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByPartnerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where partner equals to
        defaultPartnerCallFiltering("partner.equals=" + DEFAULT_PARTNER, "partner.equals=" + UPDATED_PARTNER);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByPartnerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where partner in
        defaultPartnerCallFiltering("partner.in=" + DEFAULT_PARTNER + "," + UPDATED_PARTNER, "partner.in=" + UPDATED_PARTNER);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByPartnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where partner is not null
        defaultPartnerCallFiltering("partner.specified=true", "partner.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByApiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where api equals to
        defaultPartnerCallFiltering("api.equals=" + DEFAULT_API, "api.equals=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByApiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where api in
        defaultPartnerCallFiltering("api.in=" + DEFAULT_API + "," + UPDATED_API, "api.in=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByApiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where api is not null
        defaultPartnerCallFiltering("api.specified=true", "api.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByApiContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where api contains
        defaultPartnerCallFiltering("api.contains=" + DEFAULT_API, "api.contains=" + UPDATED_API);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByApiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where api does not contain
        defaultPartnerCallFiltering("api.doesNotContain=" + UPDATED_API, "api.doesNotContain=" + DEFAULT_API);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where method equals to
        defaultPartnerCallFiltering("method.equals=" + DEFAULT_METHOD, "method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where method in
        defaultPartnerCallFiltering("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD, "method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where method is not null
        defaultPartnerCallFiltering("method.specified=true", "method.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByRequestTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where requestTime equals to
        defaultPartnerCallFiltering("requestTime.equals=" + DEFAULT_REQUEST_TIME, "requestTime.equals=" + UPDATED_REQUEST_TIME);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByRequestTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where requestTime in
        defaultPartnerCallFiltering(
            "requestTime.in=" + DEFAULT_REQUEST_TIME + "," + UPDATED_REQUEST_TIME,
            "requestTime.in=" + UPDATED_REQUEST_TIME
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByRequestTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where requestTime is not null
        defaultPartnerCallFiltering("requestTime.specified=true", "requestTime.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode equals to
        defaultPartnerCallFiltering(
            "responseStatusCode.equals=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.equals=" + UPDATED_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode in
        defaultPartnerCallFiltering(
            "responseStatusCode.in=" + DEFAULT_RESPONSE_STATUS_CODE + "," + UPDATED_RESPONSE_STATUS_CODE,
            "responseStatusCode.in=" + UPDATED_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode is not null
        defaultPartnerCallFiltering("responseStatusCode.specified=true", "responseStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode is greater than or equal to
        defaultPartnerCallFiltering(
            "responseStatusCode.greaterThanOrEqual=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.greaterThanOrEqual=" + (DEFAULT_RESPONSE_STATUS_CODE + 1)
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode is less than or equal to
        defaultPartnerCallFiltering(
            "responseStatusCode.lessThanOrEqual=" + DEFAULT_RESPONSE_STATUS_CODE,
            "responseStatusCode.lessThanOrEqual=" + SMALLER_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode is less than
        defaultPartnerCallFiltering(
            "responseStatusCode.lessThan=" + (DEFAULT_RESPONSE_STATUS_CODE + 1),
            "responseStatusCode.lessThan=" + DEFAULT_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseStatusCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseStatusCode is greater than
        defaultPartnerCallFiltering(
            "responseStatusCode.greaterThan=" + SMALLER_RESPONSE_STATUS_CODE,
            "responseStatusCode.greaterThan=" + DEFAULT_RESPONSE_STATUS_CODE
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseTime equals to
        defaultPartnerCallFiltering("responseTime.equals=" + DEFAULT_RESPONSE_TIME, "responseTime.equals=" + UPDATED_RESPONSE_TIME);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseTime in
        defaultPartnerCallFiltering(
            "responseTime.in=" + DEFAULT_RESPONSE_TIME + "," + UPDATED_RESPONSE_TIME,
            "responseTime.in=" + UPDATED_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByResponseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where responseTime is not null
        defaultPartnerCallFiltering("responseTime.specified=true", "responseTime.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByCorrelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where correlationId equals to
        defaultPartnerCallFiltering("correlationId.equals=" + DEFAULT_CORRELATION_ID, "correlationId.equals=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByCorrelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where correlationId in
        defaultPartnerCallFiltering(
            "correlationId.in=" + DEFAULT_CORRELATION_ID + "," + UPDATED_CORRELATION_ID,
            "correlationId.in=" + UPDATED_CORRELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByCorrelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where correlationId is not null
        defaultPartnerCallFiltering("correlationId.specified=true", "correlationId.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByCorrelationIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where correlationId contains
        defaultPartnerCallFiltering("correlationId.contains=" + DEFAULT_CORRELATION_ID, "correlationId.contains=" + UPDATED_CORRELATION_ID);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByCorrelationIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where correlationId does not contain
        defaultPartnerCallFiltering(
            "correlationId.doesNotContain=" + UPDATED_CORRELATION_ID,
            "correlationId.doesNotContain=" + DEFAULT_CORRELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByQueryParamIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where queryParam equals to
        defaultPartnerCallFiltering("queryParam.equals=" + DEFAULT_QUERY_PARAM, "queryParam.equals=" + UPDATED_QUERY_PARAM);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByQueryParamIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where queryParam in
        defaultPartnerCallFiltering(
            "queryParam.in=" + DEFAULT_QUERY_PARAM + "," + UPDATED_QUERY_PARAM,
            "queryParam.in=" + UPDATED_QUERY_PARAM
        );
    }

    @Test
    @Transactional
    void getAllPartnerCallsByQueryParamIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where queryParam is not null
        defaultPartnerCallFiltering("queryParam.specified=true", "queryParam.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnerCallsByQueryParamContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where queryParam contains
        defaultPartnerCallFiltering("queryParam.contains=" + DEFAULT_QUERY_PARAM, "queryParam.contains=" + UPDATED_QUERY_PARAM);
    }

    @Test
    @Transactional
    void getAllPartnerCallsByQueryParamNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        // Get all the partnerCallList where queryParam does not contain
        defaultPartnerCallFiltering("queryParam.doesNotContain=" + UPDATED_QUERY_PARAM, "queryParam.doesNotContain=" + DEFAULT_QUERY_PARAM);
    }

    private void defaultPartnerCallFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPartnerCallShouldBeFound(shouldBeFound);
        defaultPartnerCallShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartnerCallShouldBeFound(String filter) throws Exception {
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.toString())))
            .andExpect(jsonPath("$.[*].api").value(hasItem(DEFAULT_API)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].requestHeaders").value(hasItem(DEFAULT_REQUEST_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY.toString())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(DEFAULT_REQUEST_TIME.toString())))
            .andExpect(jsonPath("$.[*].responseStatusCode").value(hasItem(DEFAULT_RESPONSE_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].responseHeaders").value(hasItem(DEFAULT_RESPONSE_HEADERS.toString())))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY.toString())))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID)))
            .andExpect(jsonPath("$.[*].queryParam").value(hasItem(DEFAULT_QUERY_PARAM)));

        // Check, that the count call also returns 1
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartnerCallShouldNotBeFound(String filter) throws Exception {
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartnerCallMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartnerCall() throws Exception {
        // Get the partnerCall
        restPartnerCallMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartnerCall() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerCall
        PartnerCall updatedPartnerCall = partnerCallRepository.findById(partnerCall.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartnerCall are not directly saved in db
        em.detach(updatedPartnerCall);
        updatedPartnerCall
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .requestTime(UPDATED_REQUEST_TIME)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseHeaders(UPDATED_RESPONSE_HEADERS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .correlationId(UPDATED_CORRELATION_ID)
            .queryParam(UPDATED_QUERY_PARAM);

        restPartnerCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartnerCall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPartnerCall))
            )
            .andExpect(status().isOk());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartnerCallToMatchAllProperties(updatedPartnerCall);
    }

    @Test
    @Transactional
    void putNonExistingPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerCall.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partnerCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partnerCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partnerCall)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartnerCallWithPatch() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerCall using partial update
        PartnerCall partialUpdatedPartnerCall = new PartnerCall();
        partialUpdatedPartnerCall.setId(partnerCall.getId());

        partialUpdatedPartnerCall
            .partner(UPDATED_PARTNER)
            .method(UPDATED_METHOD)
            .requestBody(UPDATED_REQUEST_BODY)
            .requestTime(UPDATED_REQUEST_TIME)
            .responseTime(UPDATED_RESPONSE_TIME)
            .correlationId(UPDATED_CORRELATION_ID);

        restPartnerCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartnerCall))
            )
            .andExpect(status().isOk());

        // Validate the PartnerCall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerCallUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartnerCall, partnerCall),
            getPersistedPartnerCall(partnerCall)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartnerCallWithPatch() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partnerCall using partial update
        PartnerCall partialUpdatedPartnerCall = new PartnerCall();
        partialUpdatedPartnerCall.setId(partnerCall.getId());

        partialUpdatedPartnerCall
            .partner(UPDATED_PARTNER)
            .api(UPDATED_API)
            .method(UPDATED_METHOD)
            .requestHeaders(UPDATED_REQUEST_HEADERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .requestTime(UPDATED_REQUEST_TIME)
            .responseStatusCode(UPDATED_RESPONSE_STATUS_CODE)
            .responseHeaders(UPDATED_RESPONSE_HEADERS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .correlationId(UPDATED_CORRELATION_ID)
            .queryParam(UPDATED_QUERY_PARAM);

        restPartnerCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartnerCall))
            )
            .andExpect(status().isOk());

        // Validate the PartnerCall in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerCallUpdatableFieldsEquals(partialUpdatedPartnerCall, getPersistedPartnerCall(partialUpdatedPartnerCall));
    }

    @Test
    @Transactional
    void patchNonExistingPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partnerCall.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partnerCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partnerCall))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartnerCall() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partnerCall.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerCallMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partnerCall)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartnerCall in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartnerCall() throws Exception {
        // Initialize the database
        insertedPartnerCall = partnerCallRepository.saveAndFlush(partnerCall);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partnerCall
        restPartnerCallMockMvc
            .perform(delete(ENTITY_API_URL_ID, partnerCall.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partnerCallRepository.count();
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

    protected PartnerCall getPersistedPartnerCall(PartnerCall partnerCall) {
        return partnerCallRepository.findById(partnerCall.getId()).orElseThrow();
    }

    protected void assertPersistedPartnerCallToMatchAllProperties(PartnerCall expectedPartnerCall) {
        assertPartnerCallAllPropertiesEquals(expectedPartnerCall, getPersistedPartnerCall(expectedPartnerCall));
    }

    protected void assertPersistedPartnerCallToMatchUpdatableProperties(PartnerCall expectedPartnerCall) {
        assertPartnerCallAllUpdatablePropertiesEquals(expectedPartnerCall, getPersistedPartnerCall(expectedPartnerCall));
    }
}
