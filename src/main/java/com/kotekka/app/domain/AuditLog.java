package com.kotekka.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AuditLog.
 */
@Entity
@Table(name = "audit_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @NotNull
    @Size(max = 100)
    @Column(name = "entity_name", length = 100, nullable = false)
    private String entityName;

    @NotNull
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @NotNull
    @Size(max = 50)
    @Column(name = "action", length = 50, nullable = false)
    private String action;

    @Size(max = 50)
    @Column(name = "performed_by", length = 50)
    private String performedBy;

    @NotNull
    @Column(name = "performed_date", nullable = false)
    private Instant performedDate;

    @Lob
    @Column(name = "details")
    private String details;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public AuditLog uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public AuditLog entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public UUID getEntityId() {
        return this.entityId;
    }

    public AuditLog entityId(UUID entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return this.action;
    }

    public AuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return this.performedBy;
    }

    public AuditLog performedBy(String performedBy) {
        this.setPerformedBy(performedBy);
        return this;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public Instant getPerformedDate() {
        return this.performedDate;
    }

    public AuditLog performedDate(Instant performedDate) {
        this.setPerformedDate(performedDate);
        return this;
    }

    public void setPerformedDate(Instant performedDate) {
        this.performedDate = performedDate;
    }

    public String getDetails() {
        return this.details;
    }

    public AuditLog details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((AuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLog{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", action='" + getAction() + "'" +
            ", performedBy='" + getPerformedBy() + "'" +
            ", performedDate='" + getPerformedDate() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
