package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Notification
 */
@Schema(description = "Notification")
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "wallet_holder")
    private UUID walletHolder;

    @Size(max = 100)
    @Column(name = "heading", length = 100)
    private String heading;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus status;

    @Size(max = 254)
    @Column(name = "content", length = 254)
    private String content;

    @Size(max = 254)
    @Column(name = "data", length = 254)
    private String data;

    @Size(max = 2)
    @Column(name = "language", length = 2)
    private String language;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Notification uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public Notification walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getHeading() {
        return this.heading;
    }

    public Notification heading(String heading) {
        this.setHeading(heading);
        return this;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public Notification status(NotificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getContent() {
        return this.content;
    }

    public Notification content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return this.data;
    }

    public Notification data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLanguage() {
        return this.language;
    }

    public Notification language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Notification createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", heading='" + getHeading() + "'" +
            ", status='" + getStatus() + "'" +
            ", content='" + getContent() + "'" +
            ", data='" + getData() + "'" +
            ", language='" + getLanguage() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
