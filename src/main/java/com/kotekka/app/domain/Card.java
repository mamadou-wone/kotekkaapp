package com.kotekka.app.domain;

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
 * Payment Card
 * Tokenized card details provided by the payment processor
 * Optionally stored by the customer
 */
@Schema(description = "Payment Card\nTokenized card details provided by the payment processor\nOptionally stored by the customer")
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DefaultStatus status;

    @Size(max = 50)
    @Column(name = "label", length = 50)
    private String label;

    @Size(max = 30)
    @Column(name = "masked_pan", length = 30)
    private String maskedPan;

    @Size(max = 30)
    @Column(name = "card_holder_name", length = 30)
    private String cardHolderName;

    @Size(max = 36)
    @Column(name = "token", length = 36)
    private String token;

    @Size(max = 2)
    @Column(name = "expiry_year", length = 2)
    private String expiryYear;

    @Size(max = 2)
    @Column(name = "expiry_month", length = 2)
    private String expiryMonth;

    @Size(max = 20)
    @Column(name = "rnd", length = 20)
    private String rnd;

    @Size(max = 100)
    @Column(name = "hash", length = 100)
    private String hash;

    @Column(name = "wallet_holder")
    private UUID walletHolder;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Card uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public DefaultStatus getStatus() {
        return this.status;
    }

    public Card status(DefaultStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DefaultStatus status) {
        this.status = status;
    }

    public String getLabel() {
        return this.label;
    }

    public Card label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaskedPan() {
        return this.maskedPan;
    }

    public Card maskedPan(String maskedPan) {
        this.setMaskedPan(maskedPan);
        return this;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public Card cardHolderName(String cardHolderName) {
        this.setCardHolderName(cardHolderName);
        return this;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getToken() {
        return this.token;
    }

    public Card token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiryYear() {
        return this.expiryYear;
    }

    public Card expiryYear(String expiryYear) {
        this.setExpiryYear(expiryYear);
        return this;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return this.expiryMonth;
    }

    public Card expiryMonth(String expiryMonth) {
        this.setExpiryMonth(expiryMonth);
        return this;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getRnd() {
        return this.rnd;
    }

    public Card rnd(String rnd) {
        this.setRnd(rnd);
        return this;
    }

    public void setRnd(String rnd) {
        this.rnd = rnd;
    }

    public String getHash() {
        return this.hash;
    }

    public Card hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public Card walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Card createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Card createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Card lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Card lastModifiedDate(Instant lastModifiedDate) {
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
        if (!(o instanceof Card)) {
            return false;
        }
        return getId() != null && getId().equals(((Card) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", status='" + getStatus() + "'" +
            ", label='" + getLabel() + "'" +
            ", maskedPan='" + getMaskedPan() + "'" +
            ", cardHolderName='" + getCardHolderName() + "'" +
            ", token='" + getToken() + "'" +
            ", expiryYear='" + getExpiryYear() + "'" +
            ", expiryMonth='" + getExpiryMonth() + "'" +
            ", rnd='" + getRnd() + "'" +
            ", hash='" + getHash() + "'" +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
