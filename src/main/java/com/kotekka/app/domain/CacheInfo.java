package com.kotekka.app.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * temporary Cache entity
 * (key, value) pairs for given wallet holders
 * includes createdDate for cleanup
 */
@Schema(description = "temporary Cache entity\n(key, value) pairs for given wallet holders\nincludes createdDate for cleanup")
@Entity
@Table(name = "cache_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CacheInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "wallet_holder")
    private UUID walletHolder;

    @Size(max = 150)
    @Column(name = "key", length = 150)
    private String key;

    @Lob
    @Column(name = "value")
    private String value;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CacheInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public CacheInfo walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getKey() {
        return this.key;
    }

    public CacheInfo key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public CacheInfo value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CacheInfo createdDate(Instant createdDate) {
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
        if (!(o instanceof CacheInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((CacheInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CacheInfo{" +
            "id=" + getId() +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
