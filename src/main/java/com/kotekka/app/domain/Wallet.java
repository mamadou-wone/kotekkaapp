package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.AccountLevel;
import com.kotekka.app.domain.enumeration.WalletStatus;
import com.kotekka.app.domain.enumeration.WalletType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Wallet account
 * Collects balances and transactions
 * Current balance should match externel provider balance
 */
@Schema(description = "Wallet account\nCollects balances and transactions\nCurrent balance should match externel provider balance")
@Entity
@Table(name = "wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wallet implements Serializable {

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
    @Column(name = "type")
    private WalletType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WalletStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private AccountLevel level;

    @Size(max = 32)
    @Column(name = "iban", length = 32, unique = true)
    private String iban;

    @Size(max = 3)
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @Column(name = "balances_as_of")
    private Instant balancesAsOf;

    @Size(max = 36)
    @Column(name = "external_id", length = 36)
    private String externalId;

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

    public Wallet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Wallet uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public WalletType getType() {
        return this.type;
    }

    public Wallet type(WalletType type) {
        this.setType(type);
        return this;
    }

    public void setType(WalletType type) {
        this.type = type;
    }

    public WalletStatus getStatus() {
        return this.status;
    }

    public Wallet status(WalletStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public AccountLevel getLevel() {
        return this.level;
    }

    public Wallet level(AccountLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(AccountLevel level) {
        this.level = level;
    }

    public String getIban() {
        return this.iban;
    }

    public Wallet iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Wallet currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Wallet balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getBalancesAsOf() {
        return this.balancesAsOf;
    }

    public Wallet balancesAsOf(Instant balancesAsOf) {
        this.setBalancesAsOf(balancesAsOf);
        return this;
    }

    public void setBalancesAsOf(Instant balancesAsOf) {
        this.balancesAsOf = balancesAsOf;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public Wallet externalId(String externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public Wallet walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Wallet createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Wallet createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Wallet lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Wallet lastModifiedDate(Instant lastModifiedDate) {
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
        if (!(o instanceof Wallet)) {
            return false;
        }
        return getId() != null && getId().equals(((Wallet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", level='" + getLevel() + "'" +
            ", iban='" + getIban() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", balance=" + getBalance() +
            ", balancesAsOf='" + getBalancesAsOf() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
