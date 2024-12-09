package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.AccountType;
import com.kotekka.app.domain.enumeration.DefaultStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Service Client
 */
@Schema(description = "Service Client")
@Entity
@Table(name = "service_client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * uuid of wallet holder if client type is Customer
     */
    @Schema(description = "uuid of wallet holder if client type is Customer")
    @Column(name = "client_id")
    private UUID clientId;

    /**
     * type of client initially only Customer
     */
    @Schema(description = "type of client initially only Customer")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountType type;

    @Column(name = "api_key", unique = true)
    private UUID apiKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DefaultStatus status;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceClient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public ServiceClient clientId(UUID clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public AccountType getType() {
        return this.type;
    }

    public ServiceClient type(AccountType type) {
        this.setType(type);
        return this;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public UUID getApiKey() {
        return this.apiKey;
    }

    public ServiceClient apiKey(UUID apiKey) {
        this.setApiKey(apiKey);
        return this;
    }

    public void setApiKey(UUID apiKey) {
        this.apiKey = apiKey;
    }

    public DefaultStatus getStatus() {
        return this.status;
    }

    public ServiceClient status(DefaultStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DefaultStatus status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public ServiceClient note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ServiceClient createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ServiceClient lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceClient)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceClient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceClient{" +
            "id=" + getId() +
            ", clientId='" + getClientId() + "'" +
            ", type='" + getType() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
