package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.Action;
import com.kotekka.app.domain.enumeration.App;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Failed verification Attempts History
 * Not Audited
 */
@Schema(description = "Failed verification Attempts History\nNot Audited")
@Entity
@Table(name = "failed_attempt_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedAttemptHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "login", length = 50)
    private String login;

    @Size(max = 50)
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "is_after_lock")
    private Boolean isAfterLock;

    @Enumerated(EnumType.STRING)
    @Column(name = "app")
    private App app;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private Action action;

    @Column(name = "device")
    private UUID device;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 100)
    @Column(name = "reason", length = 100)
    private String reason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FailedAttemptHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public FailedAttemptHistory login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public FailedAttemptHistory ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getIsAfterLock() {
        return this.isAfterLock;
    }

    public FailedAttemptHistory isAfterLock(Boolean isAfterLock) {
        this.setIsAfterLock(isAfterLock);
        return this;
    }

    public void setIsAfterLock(Boolean isAfterLock) {
        this.isAfterLock = isAfterLock;
    }

    public App getApp() {
        return this.app;
    }

    public FailedAttemptHistory app(App app) {
        this.setApp(app);
        return this;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Action getAction() {
        return this.action;
    }

    public FailedAttemptHistory action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public UUID getDevice() {
        return this.device;
    }

    public FailedAttemptHistory device(UUID device) {
        this.setDevice(device);
        return this;
    }

    public void setDevice(UUID device) {
        this.device = device;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public FailedAttemptHistory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getReason() {
        return this.reason;
    }

    public FailedAttemptHistory reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FailedAttemptHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((FailedAttemptHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedAttemptHistory{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", isAfterLock='" + getIsAfterLock() + "'" +
            ", app='" + getApp() + "'" +
            ", action='" + getAction() + "'" +
            ", device='" + getDevice() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
