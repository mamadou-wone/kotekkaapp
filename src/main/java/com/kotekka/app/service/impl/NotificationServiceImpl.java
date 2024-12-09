package com.kotekka.app.service.impl;

import com.kotekka.app.domain.Notification;
import com.kotekka.app.repository.NotificationRepository;
import com.kotekka.app.service.NotificationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kotekka.app.domain.Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        LOG.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        LOG.debug("Request to update Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> partialUpdate(Notification notification) {
        LOG.debug("Request to partially update Notification : {}", notification);

        return notificationRepository
            .findById(notification.getId())
            .map(existingNotification -> {
                if (notification.getUuid() != null) {
                    existingNotification.setUuid(notification.getUuid());
                }
                if (notification.getWalletHolder() != null) {
                    existingNotification.setWalletHolder(notification.getWalletHolder());
                }
                if (notification.getHeading() != null) {
                    existingNotification.setHeading(notification.getHeading());
                }
                if (notification.getStatus() != null) {
                    existingNotification.setStatus(notification.getStatus());
                }
                if (notification.getContent() != null) {
                    existingNotification.setContent(notification.getContent());
                }
                if (notification.getData() != null) {
                    existingNotification.setData(notification.getData());
                }
                if (notification.getLanguage() != null) {
                    existingNotification.setLanguage(notification.getLanguage());
                }
                if (notification.getCreatedDate() != null) {
                    existingNotification.setCreatedDate(notification.getCreatedDate());
                }

                return existingNotification;
            })
            .map(notificationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
