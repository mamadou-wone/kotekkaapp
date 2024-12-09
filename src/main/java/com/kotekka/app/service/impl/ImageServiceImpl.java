package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Image;
import com.kotekka.app.repository.ImageRepository;
import com.kotekka.app.service.ImageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Image}.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image save(Image image) {
        LOG.debug("Request to save Image : {}", image);
        return imageRepository.save(image);
    }

    @Override
    public Image update(Image image) {
        LOG.debug("Request to update Image : {}", image);
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> partialUpdate(Image image) {
        LOG.debug("Request to partially update Image : {}", image);

        return imageRepository
            .findById(image.getId())
            .map(existingImage -> {
                if (image.getUuid() != null) {
                    existingImage.setUuid(image.getUuid());
                }
                if (image.getName() != null) {
                    existingImage.setName(image.getName());
                }
                if (image.getFile() != null) {
                    existingImage.setFile(image.getFile());
                }
                if (image.getFileContentType() != null) {
                    existingImage.setFileContentType(image.getFileContentType());
                }
                if (image.getWalletHolder() != null) {
                    existingImage.setWalletHolder(image.getWalletHolder());
                }
                if (image.getCreatedBy() != null) {
                    existingImage.setCreatedBy(image.getCreatedBy());
                }
                if (image.getCreatedDate() != null) {
                    existingImage.setCreatedDate(image.getCreatedDate());
                }
                if (image.getLastModifiedBy() != null) {
                    existingImage.setLastModifiedBy(image.getLastModifiedBy());
                }
                if (image.getLastModifiedDate() != null) {
                    existingImage.setLastModifiedDate(image.getLastModifiedDate());
                }

                return existingImage;
            })
            .map(imageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Image> findOne(Long id) {
        LOG.debug("Request to get Image : {}", id);
        return imageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
    }
}
