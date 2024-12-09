package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.OtpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity for tracking One Time Passwords
 * Should be usable by all flows/applications
 * Not Audited
 */
@Schema(description = "Entity for tracking One Time Passwords\nShould be usable by all flows/applications\nNot Audited")
@Entity
@Table(name = "one_time_password")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OneTimePassword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Size(max = 50)
    @Column(name = "jhi_user", length = 50)
    private String user;

    @Size(max = 12)
    @Column(name = "code", length = 12)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OtpStatus status;

    @Column(name = "expiry")
    private Instant expiry;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OneTimePassword id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public OneTimePassword uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUser() {
        return this.user;
    }

    public OneTimePassword user(String user) {
        this.setUser(user);
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return this.code;
    }

    public OneTimePassword code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OtpStatus getStatus() {
        return this.status;
    }

    public OneTimePassword status(OtpStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OtpStatus status) {
        this.status = status;
    }

    public Instant getExpiry() {
        return this.expiry;
    }

    public OneTimePassword expiry(Instant expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public OneTimePassword createdDate(Instant createdDate) {
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
        if (!(o instanceof OneTimePassword)) {
            return false;
        }
        return getId() != null && getId().equals(((OneTimePassword) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OneTimePassword{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", user='" + getUser() + "'" +
            ", code='" + getCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
