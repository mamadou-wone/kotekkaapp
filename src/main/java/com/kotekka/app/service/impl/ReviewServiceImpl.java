package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Review;
import com.kotekka.app.repository.ReviewRepository;
import com.kotekka.app.service.ReviewService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Review}.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review save(Review review) {
        LOG.debug("Request to save Review : {}", review);
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        LOG.debug("Request to update Review : {}", review);
        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> partialUpdate(Review review) {
        LOG.debug("Request to partially update Review : {}", review);

        return reviewRepository
            .findById(review.getId())
            .map(existingReview -> {
                if (review.getUuid() != null) {
                    existingReview.setUuid(review.getUuid());
                }
                if (review.getProduct() != null) {
                    existingReview.setProduct(review.getProduct());
                }
                if (review.getWalletHolder() != null) {
                    existingReview.setWalletHolder(review.getWalletHolder());
                }
                if (review.getRating() != null) {
                    existingReview.setRating(review.getRating());
                }
                if (review.getComment() != null) {
                    existingReview.setComment(review.getComment());
                }
                if (review.getCreatedDate() != null) {
                    existingReview.setCreatedDate(review.getCreatedDate());
                }

                return existingReview;
            })
            .map(reviewRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findOne(Long id) {
        LOG.debug("Request to get Review : {}", id);
        return reviewRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
    }
}
