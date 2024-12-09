package com.kotekka.app.web.rest;

import static com.kotekka.app.domain.ImageAsserts.*;
import static com.kotekka.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kotekka.app.IntegrationTest;
import com.kotekka.app.domain.Image;
import com.kotekka.app.repository.ImageRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link ImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

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

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageMockMvc;

    private Image image;

    private Image insertedImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Image createEntity() {
        return new Image()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
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
    public static Image createUpdatedEntity() {
        return new Image()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        image = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedImage != null) {
            imageRepository.delete(insertedImage);
            insertedImage = null;
        }
    }

    @Test
    @Transactional
    void createImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Image
        var returnedImage = om.readValue(
            restImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Image.class
        );

        // Validate the Image in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertImageUpdatableFieldsEquals(returnedImage, getPersistedImage(returnedImage));

        insertedImage = returnedImage;
    }

    @Test
    @Transactional
    void createImageWithExistingId() throws Exception {
        // Create the Image with an existing ID
        image.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        image.setUuid(null);

        // Create the Image, which fails.

        restImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc
            .perform(get(ENTITY_API_URL_ID, image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.walletHolder").value(DEFAULT_WALLET_HOLDER.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        Long id = image.getId();

        defaultImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where uuid equals to
        defaultImageFiltering("uuid.equals=" + DEFAULT_UUID, "uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllImagesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where uuid in
        defaultImageFiltering("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID, "uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllImagesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where uuid is not null
        defaultImageFiltering("uuid.specified=true", "uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where name equals to
        defaultImageFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where name in
        defaultImageFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where name is not null
        defaultImageFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where name contains
        defaultImageFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where name does not contain
        defaultImageFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByWalletHolderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where walletHolder equals to
        defaultImageFiltering("walletHolder.equals=" + DEFAULT_WALLET_HOLDER, "walletHolder.equals=" + UPDATED_WALLET_HOLDER);
    }

    @Test
    @Transactional
    void getAllImagesByWalletHolderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where walletHolder in
        defaultImageFiltering(
            "walletHolder.in=" + DEFAULT_WALLET_HOLDER + "," + UPDATED_WALLET_HOLDER,
            "walletHolder.in=" + UPDATED_WALLET_HOLDER
        );
    }

    @Test
    @Transactional
    void getAllImagesByWalletHolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where walletHolder is not null
        defaultImageFiltering("walletHolder.specified=true", "walletHolder.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdBy equals to
        defaultImageFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdBy in
        defaultImageFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdBy is not null
        defaultImageFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdBy contains
        defaultImageFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdBy does not contain
        defaultImageFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdDate equals to
        defaultImageFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdDate in
        defaultImageFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImagesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where createdDate is not null
        defaultImageFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedBy equals to
        defaultImageFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedBy in
        defaultImageFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedBy is not null
        defaultImageFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedBy contains
        defaultImageFiltering("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedBy does not contain
        defaultImageFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedDate equals to
        defaultImageFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedDate in
        defaultImageFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImagesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        // Get all the imageList where lastModifiedDate is not null
        defaultImageFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageShouldBeFound(shouldBeFound);
        defaultImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].walletHolder").value(hasItem(DEFAULT_WALLET_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageToMatchAllProperties(updatedImage);
    }

    @Test
    @Transactional
    void putNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL_ID, image.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(image)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageWithPatch() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage.uuid(UPDATED_UUID).name(UPDATED_NAME).createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedImage, image), getPersistedImage(image));
    }

    @Test
    @Transactional
    void fullUpdateImageWithPatch() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the image using partial update
        Image partialUpdatedImage = new Image();
        partialUpdatedImage.setId(image.getId());

        partialUpdatedImage
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .walletHolder(UPDATED_WALLET_HOLDER)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImage))
            )
            .andExpect(status().isOk());

        // Validate the Image in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageUpdatableFieldsEquals(partialUpdatedImage, getPersistedImage(partialUpdatedImage));
    }

    @Test
    @Transactional
    void patchNonExistingImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, image.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(image))
            )
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        image.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(image)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Image in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImage() throws Exception {
        // Initialize the database
        insertedImage = imageRepository.saveAndFlush(image);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the image
        restImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, image.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imageRepository.count();
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

    protected Image getPersistedImage(Image image) {
        return imageRepository.findById(image.getId()).orElseThrow();
    }

    protected void assertPersistedImageToMatchAllProperties(Image expectedImage) {
        assertImageAllPropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }

    protected void assertPersistedImageToMatchUpdatableProperties(Image expectedImage) {
        assertImageAllUpdatablePropertiesEquals(expectedImage, getPersistedImage(expectedImage));
    }
}
