package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.CinAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Cin;
import com.kotekka.app.repository.CinRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CinResourceIT {

    private static final String DEFAULT_CIN_ID = "AAAAAAAAAA";
    private static final String UPDATED_CIN_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VALIDITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALIDITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALIDITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_CITY = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUING_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUING_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUING_COUNTRY_CODE = "AAA";
    private static final String UPDATED_ISSUING_COUNTRY_CODE = "BBB";

    private static final String DEFAULT_MOTHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CIVIL_REGISTER = "AAAAAAAAAA";
    private static final String UPDATED_CIVIL_REGISTER = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAA";
    private static final String UPDATED_SEX = "BBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_CITY_CODE = "AAA";
    private static final String UPDATED_BIRTH_CITY_CODE = "BBB";

    private static final UUID DEFAULT_WALLET_HOLDER = UUID.randomUUID();
    private static final UUID UPDATED_WALLET_HOLDER = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CinRepository cinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCinMockMvc;

    private Cin cin;

    private Cin insertedCin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cin createEntity() {
        return new Cin()
            .cinId(DEFAULT_CIN_ID)
            .validityDate(DEFAULT_VALIDITY_DATE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthCity(DEFAULT_BIRTH_CITY)
            .fatherName(DEFAULT_FATHER_NAME)
            .nationality(DEFAULT_NATIONALITY)
            .nationalityCode(DEFAULT_NATIONALITY_CODE)
            .issuingCountry(DEFAULT_ISSUING_COUNTRY)
            .issuingCountryCode(DEFAULT_ISSUING_COUNTRY_CODE)
            .motherName(DEFAULT_MOTHER_NAME)
            .civilRegister(DEFAULT_CIVIL_REGISTER)
            .sex(DEFAULT_SEX)
            .address(DEFAULT_ADDRESS)
            .birthCityCode(DEFAULT_BIRTH_CITY_CODE)
            .walletHolder(DEFAULT_WALLET_HOLDER)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cin createUpdatedEntity() {
        return new Cin()
            .cinId(UPDATED_CIN_ID)
            .validityDate(UPDATED_VALIDITY_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthCity(UPDATED_BIRTH_CITY)
            .fatherName(UPDATED_FATHER_NAME)
            .nationality(UPDATED_NATIONALITY)
            .nationalityCode(UPDATED_NATIONALITY_CODE)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .issuingCountryCode(UPDATED_ISSUING_COUNTRY_CODE)
            .motherName(UPDATED_MOTHER_NAME)
            .civilRegister(UPDATED_CIVIL_REGISTER)
            .sex(UPDATED_SEX)
            .address(UPDATED_ADDRESS)
            .birthCityCode(UPDATED_BIRTH_CITY_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    public void initTest() {
        cin = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCin != null) {
            cinRepository.delete(insertedCin);
            insertedCin = null;
        }
    }

    @Test
    @Transactional
    void createCin() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cin
        var returnedCin = om.readValue(
            restCinMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cin)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Cin.class
        );

        // Validate the Cin in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCinUpdatableFieldsEquals(returnedCin, getPersistedCin(returnedCin));

        insertedCin = returnedCin;
    }

    @Test
    @Transactional
    void createCinWithExistingId() throws Exception {
        // Create the Cin with an existing ID
        cin.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cin)))
            .andExpect(status().isBadRequest());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCins() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList
        restCinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cin.getId().intValue())))
            .andExpect(jsonPath("$.[*].cinId").value(hasItem(DEFAULT_CIN_ID)))
            .andExpect(jsonPath("$.[*].validityDate").value(hasItem(DEFAULT_VALIDITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthCity").value(hasItem(DEFAULT_BIRTH_CITY)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].nationalityCode").value(hasItem(DEFAULT_NATIONALITY_CODE)))
            .andExpect(jsonPath("$.[*].issuingCountry").value(hasItem(DEFAULT_ISSUING_COUNTRY)))
            .andExpect(jsonPath("$.[*].issuingCountryCode").value(hasItem(DEFAULT_ISSUING_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].civilRegister").value(hasItem(DEFAULT_CIVIL_REGISTER)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].birthCityCode").value(hasItem(DEFAULT_BIRTH_CITY_CODE)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCin() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get the cin
        restCinMockMvc
            .perform(get(ENTITY_API_URL_ID, cin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cin.getId().intValue()))
            .andExpect(jsonPath("$.cinId").value(DEFAULT_CIN_ID))
            .andExpect(jsonPath("$.validityDate").value(DEFAULT_VALIDITY_DATE.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthCity").value(DEFAULT_BIRTH_CITY))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.nationalityCode").value(DEFAULT_NATIONALITY_CODE))
            .andExpect(jsonPath("$.issuingCountry").value(DEFAULT_ISSUING_COUNTRY))
            .andExpect(jsonPath("$.issuingCountryCode").value(DEFAULT_ISSUING_COUNTRY_CODE))
            .andExpect(jsonPath("$.motherName").value(DEFAULT_MOTHER_NAME))
            .andExpect(jsonPath("$.civilRegister").value(DEFAULT_CIVIL_REGISTER))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.birthCityCode").value(DEFAULT_BIRTH_CITY_CODE))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCinsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        Long id = cin.getId();

        defaultCinFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCinFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCinFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCinsByCinIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where cinId equals to
        defaultCinFiltering("cinId.equals=" + DEFAULT_CIN_ID, "cinId.equals=" + UPDATED_CIN_ID);
    }

    @Test
    @Transactional
    void getAllCinsByCinIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where cinId in
        defaultCinFiltering("cinId.in=" + DEFAULT_CIN_ID + "," + UPDATED_CIN_ID, "cinId.in=" + UPDATED_CIN_ID);
    }

    @Test
    @Transactional
    void getAllCinsByCinIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where cinId is not null
        defaultCinFiltering("cinId.specified=true", "cinId.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByCinIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where cinId contains
        defaultCinFiltering("cinId.contains=" + DEFAULT_CIN_ID, "cinId.contains=" + UPDATED_CIN_ID);
    }

    @Test
    @Transactional
    void getAllCinsByCinIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where cinId does not contain
        defaultCinFiltering("cinId.doesNotContain=" + UPDATED_CIN_ID, "cinId.doesNotContain=" + DEFAULT_CIN_ID);
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate equals to
        defaultCinFiltering("validityDate.equals=" + DEFAULT_VALIDITY_DATE, "validityDate.equals=" + UPDATED_VALIDITY_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate in
        defaultCinFiltering(
            "validityDate.in=" + DEFAULT_VALIDITY_DATE + "," + UPDATED_VALIDITY_DATE,
            "validityDate.in=" + UPDATED_VALIDITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate is not null
        defaultCinFiltering("validityDate.specified=true", "validityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate is greater than or equal to
        defaultCinFiltering(
            "validityDate.greaterThanOrEqual=" + DEFAULT_VALIDITY_DATE,
            "validityDate.greaterThanOrEqual=" + UPDATED_VALIDITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate is less than or equal to
        defaultCinFiltering(
            "validityDate.lessThanOrEqual=" + DEFAULT_VALIDITY_DATE,
            "validityDate.lessThanOrEqual=" + SMALLER_VALIDITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate is less than
        defaultCinFiltering("validityDate.lessThan=" + UPDATED_VALIDITY_DATE, "validityDate.lessThan=" + DEFAULT_VALIDITY_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByValidityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where validityDate is greater than
        defaultCinFiltering("validityDate.greaterThan=" + SMALLER_VALIDITY_DATE, "validityDate.greaterThan=" + DEFAULT_VALIDITY_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate equals to
        defaultCinFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate in
        defaultCinFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate is not null
        defaultCinFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate is greater than or equal to
        defaultCinFiltering("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate is less than or equal to
        defaultCinFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate is less than
        defaultCinFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthDate is greater than
        defaultCinFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthPlace equals to
        defaultCinFiltering("birthPlace.equals=" + DEFAULT_BIRTH_PLACE, "birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthPlace in
        defaultCinFiltering("birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE, "birthPlace.in=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthPlace is not null
        defaultCinFiltering("birthPlace.specified=true", "birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthPlace contains
        defaultCinFiltering("birthPlace.contains=" + DEFAULT_BIRTH_PLACE, "birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthPlace does not contain
        defaultCinFiltering("birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE, "birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllCinsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where firstName equals to
        defaultCinFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where firstName in
        defaultCinFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where firstName is not null
        defaultCinFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where firstName contains
        defaultCinFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where firstName does not contain
        defaultCinFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where lastName equals to
        defaultCinFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where lastName in
        defaultCinFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where lastName is not null
        defaultCinFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where lastName contains
        defaultCinFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where lastName does not contain
        defaultCinFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCity equals to
        defaultCinFiltering("birthCity.equals=" + DEFAULT_BIRTH_CITY, "birthCity.equals=" + UPDATED_BIRTH_CITY);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCity in
        defaultCinFiltering("birthCity.in=" + DEFAULT_BIRTH_CITY + "," + UPDATED_BIRTH_CITY, "birthCity.in=" + UPDATED_BIRTH_CITY);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCity is not null
        defaultCinFiltering("birthCity.specified=true", "birthCity.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCity contains
        defaultCinFiltering("birthCity.contains=" + DEFAULT_BIRTH_CITY, "birthCity.contains=" + UPDATED_BIRTH_CITY);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCity does not contain
        defaultCinFiltering("birthCity.doesNotContain=" + UPDATED_BIRTH_CITY, "birthCity.doesNotContain=" + DEFAULT_BIRTH_CITY);
    }

    @Test
    @Transactional
    void getAllCinsByFatherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where fatherName equals to
        defaultCinFiltering("fatherName.equals=" + DEFAULT_FATHER_NAME, "fatherName.equals=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFatherNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where fatherName in
        defaultCinFiltering("fatherName.in=" + DEFAULT_FATHER_NAME + "," + UPDATED_FATHER_NAME, "fatherName.in=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFatherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where fatherName is not null
        defaultCinFiltering("fatherName.specified=true", "fatherName.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByFatherNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where fatherName contains
        defaultCinFiltering("fatherName.contains=" + DEFAULT_FATHER_NAME, "fatherName.contains=" + UPDATED_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByFatherNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where fatherName does not contain
        defaultCinFiltering("fatherName.doesNotContain=" + UPDATED_FATHER_NAME, "fatherName.doesNotContain=" + DEFAULT_FATHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationality equals to
        defaultCinFiltering("nationality.equals=" + DEFAULT_NATIONALITY, "nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationality in
        defaultCinFiltering("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY, "nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationality is not null
        defaultCinFiltering("nationality.specified=true", "nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationality contains
        defaultCinFiltering("nationality.contains=" + DEFAULT_NATIONALITY, "nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationality does not contain
        defaultCinFiltering("nationality.doesNotContain=" + UPDATED_NATIONALITY, "nationality.doesNotContain=" + DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationalityCode equals to
        defaultCinFiltering("nationalityCode.equals=" + DEFAULT_NATIONALITY_CODE, "nationalityCode.equals=" + UPDATED_NATIONALITY_CODE);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationalityCode in
        defaultCinFiltering(
            "nationalityCode.in=" + DEFAULT_NATIONALITY_CODE + "," + UPDATED_NATIONALITY_CODE,
            "nationalityCode.in=" + UPDATED_NATIONALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByNationalityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationalityCode is not null
        defaultCinFiltering("nationalityCode.specified=true", "nationalityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByNationalityCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationalityCode contains
        defaultCinFiltering("nationalityCode.contains=" + DEFAULT_NATIONALITY_CODE, "nationalityCode.contains=" + UPDATED_NATIONALITY_CODE);
    }

    @Test
    @Transactional
    void getAllCinsByNationalityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where nationalityCode does not contain
        defaultCinFiltering(
            "nationalityCode.doesNotContain=" + UPDATED_NATIONALITY_CODE,
            "nationalityCode.doesNotContain=" + DEFAULT_NATIONALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountry equals to
        defaultCinFiltering("issuingCountry.equals=" + DEFAULT_ISSUING_COUNTRY, "issuingCountry.equals=" + UPDATED_ISSUING_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountry in
        defaultCinFiltering(
            "issuingCountry.in=" + DEFAULT_ISSUING_COUNTRY + "," + UPDATED_ISSUING_COUNTRY,
            "issuingCountry.in=" + UPDATED_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountry is not null
        defaultCinFiltering("issuingCountry.specified=true", "issuingCountry.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountry contains
        defaultCinFiltering("issuingCountry.contains=" + DEFAULT_ISSUING_COUNTRY, "issuingCountry.contains=" + UPDATED_ISSUING_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountry does not contain
        defaultCinFiltering(
            "issuingCountry.doesNotContain=" + UPDATED_ISSUING_COUNTRY,
            "issuingCountry.doesNotContain=" + DEFAULT_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountryCode equals to
        defaultCinFiltering(
            "issuingCountryCode.equals=" + DEFAULT_ISSUING_COUNTRY_CODE,
            "issuingCountryCode.equals=" + UPDATED_ISSUING_COUNTRY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountryCode in
        defaultCinFiltering(
            "issuingCountryCode.in=" + DEFAULT_ISSUING_COUNTRY_CODE + "," + UPDATED_ISSUING_COUNTRY_CODE,
            "issuingCountryCode.in=" + UPDATED_ISSUING_COUNTRY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountryCode is not null
        defaultCinFiltering("issuingCountryCode.specified=true", "issuingCountryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountryCode contains
        defaultCinFiltering(
            "issuingCountryCode.contains=" + DEFAULT_ISSUING_COUNTRY_CODE,
            "issuingCountryCode.contains=" + UPDATED_ISSUING_COUNTRY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByIssuingCountryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where issuingCountryCode does not contain
        defaultCinFiltering(
            "issuingCountryCode.doesNotContain=" + UPDATED_ISSUING_COUNTRY_CODE,
            "issuingCountryCode.doesNotContain=" + DEFAULT_ISSUING_COUNTRY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByMotherNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where motherName equals to
        defaultCinFiltering("motherName.equals=" + DEFAULT_MOTHER_NAME, "motherName.equals=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByMotherNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where motherName in
        defaultCinFiltering("motherName.in=" + DEFAULT_MOTHER_NAME + "," + UPDATED_MOTHER_NAME, "motherName.in=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByMotherNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where motherName is not null
        defaultCinFiltering("motherName.specified=true", "motherName.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByMotherNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where motherName contains
        defaultCinFiltering("motherName.contains=" + DEFAULT_MOTHER_NAME, "motherName.contains=" + UPDATED_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByMotherNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where motherName does not contain
        defaultCinFiltering("motherName.doesNotContain=" + UPDATED_MOTHER_NAME, "motherName.doesNotContain=" + DEFAULT_MOTHER_NAME);
    }

    @Test
    @Transactional
    void getAllCinsByCivilRegisterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where civilRegister equals to
        defaultCinFiltering("civilRegister.equals=" + DEFAULT_CIVIL_REGISTER, "civilRegister.equals=" + UPDATED_CIVIL_REGISTER);
    }

    @Test
    @Transactional
    void getAllCinsByCivilRegisterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where civilRegister in
        defaultCinFiltering(
            "civilRegister.in=" + DEFAULT_CIVIL_REGISTER + "," + UPDATED_CIVIL_REGISTER,
            "civilRegister.in=" + UPDATED_CIVIL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllCinsByCivilRegisterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where civilRegister is not null
        defaultCinFiltering("civilRegister.specified=true", "civilRegister.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByCivilRegisterContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where civilRegister contains
        defaultCinFiltering("civilRegister.contains=" + DEFAULT_CIVIL_REGISTER, "civilRegister.contains=" + UPDATED_CIVIL_REGISTER);
    }

    @Test
    @Transactional
    void getAllCinsByCivilRegisterNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where civilRegister does not contain
        defaultCinFiltering(
            "civilRegister.doesNotContain=" + UPDATED_CIVIL_REGISTER,
            "civilRegister.doesNotContain=" + DEFAULT_CIVIL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllCinsBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where sex equals to
        defaultCinFiltering("sex.equals=" + DEFAULT_SEX, "sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllCinsBySexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where sex in
        defaultCinFiltering("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX, "sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllCinsBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where sex is not null
        defaultCinFiltering("sex.specified=true", "sex.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsBySexContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where sex contains
        defaultCinFiltering("sex.contains=" + DEFAULT_SEX, "sex.contains=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllCinsBySexNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where sex does not contain
        defaultCinFiltering("sex.doesNotContain=" + UPDATED_SEX, "sex.doesNotContain=" + DEFAULT_SEX);
    }

    @Test
    @Transactional
    void getAllCinsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where address equals to
        defaultCinFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCinsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where address in
        defaultCinFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCinsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where address is not null
        defaultCinFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where address contains
        defaultCinFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCinsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where address does not contain
        defaultCinFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCityCode equals to
        defaultCinFiltering("birthCityCode.equals=" + DEFAULT_BIRTH_CITY_CODE, "birthCityCode.equals=" + UPDATED_BIRTH_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCityCode in
        defaultCinFiltering(
            "birthCityCode.in=" + DEFAULT_BIRTH_CITY_CODE + "," + UPDATED_BIRTH_CITY_CODE,
            "birthCityCode.in=" + UPDATED_BIRTH_CITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCityCode is not null
        defaultCinFiltering("birthCityCode.specified=true", "birthCityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCityCode contains
        defaultCinFiltering("birthCityCode.contains=" + DEFAULT_BIRTH_CITY_CODE, "birthCityCode.contains=" + UPDATED_BIRTH_CITY_CODE);
    }

    @Test
    @Transactional
    void getAllCinsByBirthCityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where birthCityCode does not contain
        defaultCinFiltering(
            "birthCityCode.doesNotContain=" + UPDATED_BIRTH_CITY_CODE,
            "birthCityCode.doesNotContain=" + DEFAULT_BIRTH_CITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCinsByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where walletHolder equals to
        defaultCinFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllCinsByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where walletHolder in
        defaultCinFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllCinsByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where walletHolder is not null
        defaultCinFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllCinsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where createdDate equals to
        defaultCinFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCinsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where createdDate in
        defaultCinFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCinsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        // Get all the cinList where createdDate is not null
        defaultCinFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultCinFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCinShouldBeFound(shouldBeFound);
        defaultCinShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCinShouldBeFound(String filter) throws Exception {
        restCinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cin.getId().intValue())))
            .andExpect(jsonPath("$.[*].cinId").value(hasItem(DEFAULT_CIN_ID)))
            .andExpect(jsonPath("$.[*].validityDate").value(hasItem(DEFAULT_VALIDITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthCity").value(hasItem(DEFAULT_BIRTH_CITY)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].nationalityCode").value(hasItem(DEFAULT_NATIONALITY_CODE)))
            .andExpect(jsonPath("$.[*].issuingCountry").value(hasItem(DEFAULT_ISSUING_COUNTRY)))
            .andExpect(jsonPath("$.[*].issuingCountryCode").value(hasItem(DEFAULT_ISSUING_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].civilRegister").value(hasItem(DEFAULT_CIVIL_REGISTER)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].birthCityCode").value(hasItem(DEFAULT_BIRTH_CITY_CODE)))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restCinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCinShouldNotBeFound(String filter) throws Exception {
        restCinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCinMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCin() throws Exception {
        // Get the cin
        restCinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCin() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cin
        Cin updatedCin = cinRepository.findById(cin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCin are not directly saved in db
        em.detach(updatedCin);
        updatedCin
            .cinId(UPDATED_CIN_ID)
            .validityDate(UPDATED_VALIDITY_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthCity(UPDATED_BIRTH_CITY)
            .fatherName(UPDATED_FATHER_NAME)
            .nationality(UPDATED_NATIONALITY)
            .nationalityCode(UPDATED_NATIONALITY_CODE)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .issuingCountryCode(UPDATED_ISSUING_COUNTRY_CODE)
            .motherName(UPDATED_MOTHER_NAME)
            .civilRegister(UPDATED_CIVIL_REGISTER)
            .sex(UPDATED_SEX)
            .address(UPDATED_ADDRESS)
            .birthCityCode(UPDATED_BIRTH_CITY_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdDate(UPDATED_CREATED_DATE);

        restCinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCin.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedCin))
            )
            .andExpect(status().isOk());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCinToMatchAllProperties(updatedCin);
    }

    @Test
    @Transactional
    void putNonExistingCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(put(ENTITY_API_URL_ID, cin.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cin)))
            .andExpect(status().isBadRequest());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCinWithPatch() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cin using partial update
        Cin partialUpdatedCin = new Cin();
        partialUpdatedCin.setId(cin.getId());

        partialUpdatedCin
            .birthPlace(UPDATED_BIRTH_PLACE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthCity(UPDATED_BIRTH_CITY)
            .nationalityCode(UPDATED_NATIONALITY_CODE)
            .issuingCountryCode(UPDATED_ISSUING_COUNTRY_CODE)
            .motherName(UPDATED_MOTHER_NAME)
            .civilRegister(UPDATED_CIVIL_REGISTER)
            .sex(UPDATED_SEX)
            .address(UPDATED_ADDRESS)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdDate(UPDATED_CREATED_DATE);

        restCinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCin))
            )
            .andExpect(status().isOk());

        // Validate the Cin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCinUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCin, cin), getPersistedCin(cin));
    }

    @Test
    @Transactional
    void fullUpdateCinWithPatch() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cin using partial update
        Cin partialUpdatedCin = new Cin();
        partialUpdatedCin.setId(cin.getId());

        partialUpdatedCin
            .cinId(UPDATED_CIN_ID)
            .validityDate(UPDATED_VALIDITY_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthCity(UPDATED_BIRTH_CITY)
            .fatherName(UPDATED_FATHER_NAME)
            .nationality(UPDATED_NATIONALITY)
            .nationalityCode(UPDATED_NATIONALITY_CODE)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .issuingCountryCode(UPDATED_ISSUING_COUNTRY_CODE)
            .motherName(UPDATED_MOTHER_NAME)
            .civilRegister(UPDATED_CIVIL_REGISTER)
            .sex(UPDATED_SEX)
            .address(UPDATED_ADDRESS)
            .birthCityCode(UPDATED_BIRTH_CITY_CODE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdDate(UPDATED_CREATED_DATE);

        restCinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCin.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCin))
            )
            .andExpect(status().isOk());

        // Validate the Cin in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCinUpdatableFieldsEquals(partialUpdatedCin, getPersistedCin(partialUpdatedCin));
    }

    @Test
    @Transactional
    void patchNonExistingCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(patch(ENTITY_API_URL_ID, cin.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cin)))
            .andExpect(status().isBadRequest());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cin))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCin() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cin in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCin() throws Exception {
        // Initialize the database
        insertedCin = cinRepository.saveAndFlush(cin);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cin
        restCinMockMvc.perform(delete(ENTITY_API_URL_ID, cin.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cinRepository.count();
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

    protected Cin getPersistedCin(Cin cin) {
        return cinRepository.findById(cin.getId()).orElseThrow();
    }

    protected void assertPersistedCinToMatchAllProperties(Cin expectedCin) {
        assertCinAllPropertiesEquals(expectedCin, getPersistedCin(expectedCin));
    }

    protected void assertPersistedCinToMatchUpdatableProperties(Cin expectedCin) {
        assertCinAllUpdatablePropertiesEquals(expectedCin, getPersistedCin(expectedCin));
    }
}
