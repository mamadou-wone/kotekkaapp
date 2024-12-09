package com.kotekka.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.UserAffiliation} entity. This class is used
 * in {@link com.kotekka.app.web.rest.UserAffiliationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-affiliations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAffiliationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter walletHolder;

    private StringFilter affiliation;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public UserAffiliationCriteria() {}

    public UserAffiliationCriteria(UserAffiliationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.walletHolder = other.optionalWalletHolder().map(UUIDFilter::copy).orElse(null);
        this.affiliation = other.optionalAffiliation().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserAffiliationCriteria copy() {
        return new UserAffiliationCriteria(this);
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

    public StringFilter getAffiliation() {
        return affiliation;
    }

    public Optional<StringFilter> optionalAffiliation() {
        return Optional.ofNullable(affiliation);
    }

    public StringFilter affiliation() {
        if (affiliation == null) {
            setAffiliation(new StringFilter());
        }
        return affiliation;
    }

    public void setAffiliation(StringFilter affiliation) {
        this.affiliation = affiliation;
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
        final UserAffiliationCriteria that = (UserAffiliationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(walletHolder, that.walletHolder) &&
            Objects.equals(affiliation, that.affiliation) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, walletHolder, affiliation, createdBy, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAffiliationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalWalletHolder().map(f -> "walletHolder=" + f + ", ").orElse("") +
            optionalAffiliation().map(f -> "affiliation=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
