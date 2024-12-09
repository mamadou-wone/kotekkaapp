package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Collection;
import com.kotekka.app.repository.CollectionRepository;
import com.kotekka.app.service.CollectionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Collection}.
 */
@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public Collection save(Collection collection) {
        LOG.debug("Request to save Collection : {}", collection);
        return collectionRepository.save(collection);
    }

    @Override
    public Collection update(Collection collection) {
        LOG.debug("Request to update Collection : {}", collection);
        return collectionRepository.save(collection);
    }

    @Override
    public Optional<Collection> partialUpdate(Collection collection) {
        LOG.debug("Request to partially update Collection : {}", collection);

        return collectionRepository
            .findById(collection.getId())
            .map(existingCollection -> {
                if (collection.getName() != null) {
                    existingCollection.setName(collection.getName());
                }

                return existingCollection;
            })
            .map(collectionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Collection> findOne(Long id) {
        LOG.debug("Request to get Collection : {}", id);
        return collectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Collection : {}", id);
        collectionRepository.deleteById(id);
    }
}
