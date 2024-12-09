package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Product;
import com.kotekka.app.repository.ProductRepository;
import com.kotekka.app.service.ProductService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        LOG.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        LOG.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        LOG.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getUuid() != null) {
                    existingProduct.setUuid(product.getUuid());
                }
                if (product.getWalletHolder() != null) {
                    existingProduct.setWalletHolder(product.getWalletHolder());
                }
                if (product.getTitle() != null) {
                    existingProduct.setTitle(product.getTitle());
                }
                if (product.getDescription() != null) {
                    existingProduct.setDescription(product.getDescription());
                }
                if (product.getStatus() != null) {
                    existingProduct.setStatus(product.getStatus());
                }
                if (product.getMedia() != null) {
                    existingProduct.setMedia(product.getMedia());
                }
                if (product.getMediaContentType() != null) {
                    existingProduct.setMediaContentType(product.getMediaContentType());
                }
                if (product.getPrice() != null) {
                    existingProduct.setPrice(product.getPrice());
                }
                if (product.getCompareAtPrice() != null) {
                    existingProduct.setCompareAtPrice(product.getCompareAtPrice());
                }
                if (product.getCostPerItem() != null) {
                    existingProduct.setCostPerItem(product.getCostPerItem());
                }
                if (product.getProfit() != null) {
                    existingProduct.setProfit(product.getProfit());
                }
                if (product.getMargin() != null) {
                    existingProduct.setMargin(product.getMargin());
                }
                if (product.getInventoryQuantity() != null) {
                    existingProduct.setInventoryQuantity(product.getInventoryQuantity());
                }
                if (product.getInventoryLocation() != null) {
                    existingProduct.setInventoryLocation(product.getInventoryLocation());
                }
                if (product.getTrackQuantity() != null) {
                    existingProduct.setTrackQuantity(product.getTrackQuantity());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        LOG.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
