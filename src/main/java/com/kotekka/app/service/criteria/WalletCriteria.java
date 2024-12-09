package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.AccountLevel;
import com.kotekka.app.domain.enumeration.WalletStatus;
import com.kotekka.app.domain.enumeration.WalletType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.Wallet} entity. This class is used
 * in {@link com.kotekka.app.web.rest.WalletResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wallets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WalletType
     */
    public static class WalletTypeFilter extends Filter<WalletType> {

        public WalletTypeFilter() {}

        public WalletTypeFilter(WalletTypeFilter filter) {
            super(filter);
        }

        @Override
        public WalletTypeFilter copy() {
            return new WalletTypeFilter(this);
        }
    }

    /**
     * Class for filtering WalletStatus
     */
    public static class WalletStatusFilter extends Filter<WalletStatus> {

        public WalletStatusFilter() {}

        public WalletStatusFilter(WalletStatusFilter filter) {
            super(filter);
        }

        @Override
        public WalletStatusFilter copy() {
            return new WalletStatusFilter(this);
        }
    }

    /**
     * Class for filtering AccountLevel
     */
    public static class AccountLevelFilter extends Filter<AccountLevel> {

        public AccountLevelFilter() {}

        public AccountLevelFilter(AccountLevelFilter filter) {
            super(filter);
        }

        @Override
        public AccountLevelFilter copy() {
            return new AccountLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private WalletTypeFilter type;

    private WalletStatusFilter status;

    private AccountLevelFilter level;

    private StringFilter iban;

    private StringFilter currency;

    private BigDecimalFilter balance;

    private InstantFilter balancesAsOf;

    private StringFilter externalId;

    private UUIDFilter walletHolder;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public WalletCriteria() {}

    public WalletCriteria(WalletCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.type = other.optionalType().map(WalletTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(WalletStatusFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(AccountLevelFilter::copy).orElse(null);
        this.iban = other.optionalIban().map(StringFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.balance = other.optionalBalance().map(BigDecimalFilter::copy).orElse(null);
        this.balancesAsOf = other.optionalBalancesAsOf().map(InstantFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.walletHolder = other.optionalWalletHolder().map(UUIDFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WalletCriteria copy() {
        return new WalletCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public Optional<UUIDFilter> optionalUuid() {
        return Optional.ofNullable(uuid);
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            setUuid(new UUIDFilter());
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public WalletTypeFilter getType() {
        return type;
    }

    public Optional<WalletTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public WalletTypeFilter type() {
        if (type == null) {
            setType(new WalletTypeFilter());
        }
        return type;
    }

    public void setType(WalletTypeFilter type) {
        this.type = type;
    }

    public WalletStatusFilter getStatus() {
        return status;
    }

    public Optional<WalletStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public WalletStatusFilter status() {
        if (status == null) {
            setStatus(new WalletStatusFilter());
        }
        return status;
    }

    public void setStatus(WalletStatusFilter status) {
        this.status = status;
    }

    public AccountLevelFilter getLevel() {
        return level;
    }

    public Optional<AccountLevelFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public AccountLevelFilter level() {
        if (level == null) {
            setLevel(new AccountLevelFilter());
        }
        return level;
    }

    public void setLevel(AccountLevelFilter level) {
        this.level = level;
    }

    public StringFilter getIban() {
        return iban;
    }

    public Optional<StringFilter> optionalIban() {
        return Optional.ofNullable(iban);
    }

    public StringFilter iban() {
        if (iban == null) {
            setIban(new StringFilter());
        }
        return iban;
    }

    public void setIban(StringFilter iban) {
        this.iban = iban;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public Optional<BigDecimalFilter> optionalBalance() {
        return Optional.ofNullable(balance);
    }

    public BigDecimalFilter balance() {
        if (balance == null) {
            setBalance(new BigDecimalFilter());
        }
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public InstantFilter getBalancesAsOf() {
        return balancesAsOf;
    }

    public Optional<InstantFilter> optionalBalancesAsOf() {
        return Optional.ofNullable(balancesAsOf);
    }

    public InstantFilter balancesAsOf() {
        if (balancesAsOf == null) {
            setBalancesAsOf(new InstantFilter());
        }
        return balancesAsOf;
    }

    public void setBalancesAsOf(InstantFilter balancesAsOf) {
        this.balancesAsOf = balancesAsOf;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public Optional<StringFilter> optionalExternalId() {
        return Optional.ofNullable(externalId);
    }

    public StringFilter externalId() {
        if (externalId == null) {
            setExternalId(new StringFilter());
        }
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public UUIDFilter getWalletHolder() {
        return walletHolder;
    }

    public Optional<UUIDFilter> optionalWalletHolder() {
        return Optional.ofNullable(walletHolder);
    }

    public UUIDFilter walletHolder() {
        if (walletHolder == null) {
            setWalletHolder(new UUIDFilter());
        }
        return walletHolder;
    }

    public void setWalletHolder(UUIDFilter walletHolder) {
        this.walletHolder = walletHolder;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WalletCriteria that = (WalletCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(level, that.level) &&
            Objects.equals(iban, that.iban) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(balancesAsOf, that.balancesAsOf) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(walletHolder, that.walletHolder) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            type,
            status,
            level,
            iban,
            currency,
            balance,
            balancesAsOf,
            externalId,
            walletHolder,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalIban().map(f -> "iban=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalBalance().map(f -> "balance=" + f + ", ").orElse("") +
            optionalBalancesAsOf().map(f -> "balancesAsOf=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalWalletHolder().map(f -> "walletHolder=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
