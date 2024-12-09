package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.CounterpartyType;
import com.kotekka.app.domain.enumeration.Direction;
import com.kotekka.app.domain.enumeration.TransactionStatus;
import com.kotekka.app.domain.enumeration.TransactionType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.Transaction} entity. This class is used
 * in {@link com.kotekka.app.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }
    }

    /**
     * Class for filtering TransactionStatus
     */
    public static class TransactionStatusFilter extends Filter<TransactionStatus> {

        public TransactionStatusFilter() {}

        public TransactionStatusFilter(TransactionStatusFilter filter) {
            super(filter);
        }

        @Override
        public TransactionStatusFilter copy() {
            return new TransactionStatusFilter(this);
        }
    }

    /**
     * Class for filtering Direction
     */
    public static class DirectionFilter extends Filter<Direction> {

        public DirectionFilter() {}

        public DirectionFilter(DirectionFilter filter) {
            super(filter);
        }

        @Override
        public DirectionFilter copy() {
            return new DirectionFilter(this);
        }
    }

    /**
     * Class for filtering CounterpartyType
     */
    public static class CounterpartyTypeFilter extends Filter<CounterpartyType> {

        public CounterpartyTypeFilter() {}

        public CounterpartyTypeFilter(CounterpartyTypeFilter filter) {
            super(filter);
        }

        @Override
        public CounterpartyTypeFilter copy() {
            return new CounterpartyTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private TransactionTypeFilter type;

    private TransactionStatusFilter status;

    private DirectionFilter direction;

    private BigDecimalFilter amount;

    private StringFilter description;

    private BigDecimalFilter fee;

    private BigDecimalFilter commission;

    private StringFilter currency;

    private CounterpartyTypeFilter counterpartyType;

    private StringFilter counterpartyId;

    private LocalDateFilter entryDate;

    private LocalDateFilter effectiveDate;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private UUIDFilter parent;

    private StringFilter reference;

    private StringFilter externalId;

    private StringFilter partnerToken;

    private UUIDFilter wallet;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public TransactionCriteria() {}

    public TransactionCriteria(TransactionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.type = other.optionalType().map(TransactionTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(TransactionStatusFilter::copy).orElse(null);
        this.direction = other.optionalDirection().map(DirectionFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.fee = other.optionalFee().map(BigDecimalFilter::copy).orElse(null);
        this.commission = other.optionalCommission().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.counterpartyType = other.optionalCounterpartyType().map(CounterpartyTypeFilter::copy).orElse(null);
        this.counterpartyId = other.optionalCounterpartyId().map(StringFilter::copy).orElse(null);
        this.entryDate = other.optionalEntryDate().map(LocalDateFilter::copy).orElse(null);
        this.effectiveDate = other.optionalEffectiveDate().map(LocalDateFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(InstantFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(InstantFilter::copy).orElse(null);
        this.parent = other.optionalParent().map(UUIDFilter::copy).orElse(null);
        this.reference = other.optionalReference().map(StringFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.partnerToken = other.optionalPartnerToken().map(StringFilter::copy).orElse(null);
        this.wallet = other.optionalWallet().map(UUIDFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
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

    public TransactionTypeFilter getType() {
        return type;
    }

    public Optional<TransactionTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TransactionTypeFilter type() {
        if (type == null) {
            setType(new TransactionTypeFilter());
        }
        return type;
    }

    public void setType(TransactionTypeFilter type) {
        this.type = type;
    }

    public TransactionStatusFilter getStatus() {
        return status;
    }

    public Optional<TransactionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public TransactionStatusFilter status() {
        if (status == null) {
            setStatus(new TransactionStatusFilter());
        }
        return status;
    }

    public void setStatus(TransactionStatusFilter status) {
        this.status = status;
    }

    public DirectionFilter getDirection() {
        return direction;
    }

    public Optional<DirectionFilter> optionalDirection() {
        return Optional.ofNullable(direction);
    }

    public DirectionFilter direction() {
        if (direction == null) {
            setDirection(new DirectionFilter());
        }
        return direction;
    }

    public void setDirection(DirectionFilter direction) {
        this.direction = direction;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getFee() {
        return fee;
    }

    public Optional<BigDecimalFilter> optionalFee() {
        return Optional.ofNullable(fee);
    }

    public BigDecimalFilter fee() {
        if (fee == null) {
            setFee(new BigDecimalFilter());
        }
        return fee;
    }

    public void setFee(BigDecimalFilter fee) {
        this.fee = fee;
    }

    public BigDecimalFilter getCommission() {
        return commission;
    }

    public Optional<BigDecimalFilter> optionalCommission() {
        return Optional.ofNullable(commission);
    }

    public BigDecimalFilter commission() {
        if (commission == null) {
            setCommission(new BigDecimalFilter());
        }
        return commission;
    }

    public void setCommission(BigDecimalFilter commission) {
        this.commission = commission;
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

    public CounterpartyTypeFilter getCounterpartyType() {
        return counterpartyType;
    }

    public Optional<CounterpartyTypeFilter> optionalCounterpartyType() {
        return Optional.ofNullable(counterpartyType);
    }

    public CounterpartyTypeFilter counterpartyType() {
        if (counterpartyType == null) {
            setCounterpartyType(new CounterpartyTypeFilter());
        }
        return counterpartyType;
    }

    public void setCounterpartyType(CounterpartyTypeFilter counterpartyType) {
        this.counterpartyType = counterpartyType;
    }

    public StringFilter getCounterpartyId() {
        return counterpartyId;
    }

    public Optional<StringFilter> optionalCounterpartyId() {
        return Optional.ofNullable(counterpartyId);
    }

    public StringFilter counterpartyId() {
        if (counterpartyId == null) {
            setCounterpartyId(new StringFilter());
        }
        return counterpartyId;
    }

    public void setCounterpartyId(StringFilter counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public LocalDateFilter getEntryDate() {
        return entryDate;
    }

    public Optional<LocalDateFilter> optionalEntryDate() {
        return Optional.ofNullable(entryDate);
    }

    public LocalDateFilter entryDate() {
        if (entryDate == null) {
            setEntryDate(new LocalDateFilter());
        }
        return entryDate;
    }

    public void setEntryDate(LocalDateFilter entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateFilter getEffectiveDate() {
        return effectiveDate;
    }

    public Optional<LocalDateFilter> optionalEffectiveDate() {
        return Optional.ofNullable(effectiveDate);
    }

    public LocalDateFilter effectiveDate() {
        if (effectiveDate == null) {
            setEffectiveDate(new LocalDateFilter());
        }
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateFilter effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public Optional<InstantFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            setStartTime(new InstantFilter());
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public Optional<InstantFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            setEndTime(new InstantFilter());
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public UUIDFilter getParent() {
        return parent;
    }

    public Optional<UUIDFilter> optionalParent() {
        return Optional.ofNullable(parent);
    }

    public UUIDFilter parent() {
        if (parent == null) {
            setParent(new UUIDFilter());
        }
        return parent;
    }

    public void setParent(UUIDFilter parent) {
        this.parent = parent;
    }

    public StringFilter getReference() {
        return reference;
    }

    public Optional<StringFilter> optionalReference() {
        return Optional.ofNullable(reference);
    }

    public StringFilter reference() {
        if (reference == null) {
            setReference(new StringFilter());
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
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

    public StringFilter getPartnerToken() {
        return partnerToken;
    }

    public Optional<StringFilter> optionalPartnerToken() {
        return Optional.ofNullable(partnerToken);
    }

    public StringFilter partnerToken() {
        if (partnerToken == null) {
            setPartnerToken(new StringFilter());
        }
        return partnerToken;
    }

    public void setPartnerToken(StringFilter partnerToken) {
        this.partnerToken = partnerToken;
    }

    public UUIDFilter getWallet() {
        return wallet;
    }

    public Optional<UUIDFilter> optionalWallet() {
        return Optional.ofNullable(wallet);
    }

    public UUIDFilter wallet() {
        if (wallet == null) {
            setWallet(new UUIDFilter());
        }
        return wallet;
    }

    public void setWallet(UUIDFilter wallet) {
        this.wallet = wallet;
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
        final TransactionCriteria that = (TransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fee, that.fee) &&
            Objects.equals(commission, that.commission) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(counterpartyType, that.counterpartyType) &&
            Objects.equals(counterpartyId, that.counterpartyId) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(effectiveDate, that.effectiveDate) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(parent, that.parent) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(partnerToken, that.partnerToken) &&
            Objects.equals(wallet, that.wallet) &&
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
            direction,
            amount,
            description,
            fee,
            commission,
            currency,
            counterpartyType,
            counterpartyId,
            entryDate,
            effectiveDate,
            startTime,
            endTime,
            parent,
            reference,
            externalId,
            partnerToken,
            wallet,
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
        return "TransactionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDirection().map(f -> "direction=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFee().map(f -> "fee=" + f + ", ").orElse("") +
            optionalCommission().map(f -> "commission=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalCounterpartyType().map(f -> "counterpartyType=" + f + ", ").orElse("") +
            optionalCounterpartyId().map(f -> "counterpartyId=" + f + ", ").orElse("") +
            optionalEntryDate().map(f -> "entryDate=" + f + ", ").orElse("") +
            optionalEffectiveDate().map(f -> "effectiveDate=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalParent().map(f -> "parent=" + f + ", ").orElse("") +
            optionalReference().map(f -> "reference=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalPartnerToken().map(f -> "partnerToken=" + f + ", ").orElse("") +
            optionalWallet().map(f -> "wallet=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
