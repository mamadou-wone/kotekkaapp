package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Card;
import com.kotekka.app.repository.CardRepository;
import com.kotekka.app.service.CardService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger LOG = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card save(Card card) {
        LOG.debug("Request to save Card : {}", card);
        return cardRepository.save(card);
    }

    @Override
    public Card update(Card card) {
        LOG.debug("Request to update Card : {}", card);
        return cardRepository.save(card);
    }

    @Override
    public Optional<Card> partialUpdate(Card card) {
        LOG.debug("Request to partially update Card : {}", card);

        return cardRepository
            .findById(card.getId())
            .map(existingCard -> {
                if (card.getUuid() != null) {
                    existingCard.setUuid(card.getUuid());
                }
                if (card.getStatus() != null) {
                    existingCard.setStatus(card.getStatus());
                }
                if (card.getLabel() != null) {
                    existingCard.setLabel(card.getLabel());
                }
                if (card.getMaskedPan() != null) {
                    existingCard.setMaskedPan(card.getMaskedPan());
                }
                if (card.getCardHolderName() != null) {
                    existingCard.setCardHolderName(card.getCardHolderName());
                }
                if (card.getToken() != null) {
                    existingCard.setToken(card.getToken());
                }
                if (card.getExpiryYear() != null) {
                    existingCard.setExpiryYear(card.getExpiryYear());
                }
                if (card.getExpiryMonth() != null) {
                    existingCard.setExpiryMonth(card.getExpiryMonth());
                }
                if (card.getRnd() != null) {
                    existingCard.setRnd(card.getRnd());
                }
                if (card.getHash() != null) {
                    existingCard.setHash(card.getHash());
                }
                if (card.getWalletHolder() != null) {
                    existingCard.setWalletHolder(card.getWalletHolder());
                }
                if (card.getCreatedBy() != null) {
                    existingCard.setCreatedBy(card.getCreatedBy());
                }
                if (card.getCreatedDate() != null) {
                    existingCard.setCreatedDate(card.getCreatedDate());
                }
                if (card.getLastModifiedBy() != null) {
                    existingCard.setLastModifiedBy(card.getLastModifiedBy());
                }
                if (card.getLastModifiedDate() != null) {
                    existingCard.setLastModifiedDate(card.getLastModifiedDate());
                }

                return existingCard;
            })
            .map(cardRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Card> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cards");
        return cardRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findOne(Long id) {
        LOG.debug("Request to get Card : {}", id);
        return cardRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
