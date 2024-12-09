package com.kotekka.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAffiliation.
 */
@Entity
@Table(name = "user_affiliation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAffiliation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "wallet_holder", nullable = false)
    private UUID walletHolder;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "affiliation", length = 100, nullable = false)
    private String affiliation;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAffiliation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public UserAffiliation walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getAffiliation() {
        return this.affiliation;
    }

    public UserAffiliation affiliation(String affiliation) {
        this.setAffiliation(affiliation);
        return this;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserAffiliation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserAffiliation createdDate(Instant createdDate) {
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
        if (!(o instanceof UserAffiliation)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAffiliation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAffiliation{" +
            "id=" + getId() +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", affiliation='" + getAffiliation() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
