package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.OtpStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.OneTimePassword} entity. This class is used
 * in {@link com.kotekka.app.web.rest.OneTimePasswordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /one-time-passwords?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OneTimePasswordCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OtpStatus
     */
    public static class OtpStatusFilter extends Filter<OtpStatus> {

        public OtpStatusFilter() {}

        public OtpStatusFilter(OtpStatusFilter filter) {
            super(filter);
        }

        @Override
        public OtpStatusFilter copy() {
            return new OtpStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter user;

    private StringFilter code;

    private OtpStatusFilter status;

    private InstantFilter expiry;

    private InstantFilter createdDate;

    private Boolean distinct;

    public OneTimePasswordCriteria() {}

    public OneTimePasswordCriteria(OneTimePasswordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.user = other.optionalUser().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OtpStatusFilter::copy).orElse(null);
        this.expiry = other.optionalExpiry().map(InstantFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OneTimePasswordCriteria copy() {
        return new OneTimePasswordCriteria(this);
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

    public StringFilter getUser() {
        return user;
    }

    public Optional<StringFilter> optionalUser() {
        return Optional.ofNullable(user);
    }

    public StringFilter user() {
        if (user == null) {
            setUser(new StringFilter());
        }
        return user;
    }

    public void setUser(StringFilter user) {
        this.user = user;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public OtpStatusFilter getStatus() {
        return status;
    }

    public Optional<OtpStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OtpStatusFilter status() {
        if (status == null) {
            setStatus(new OtpStatusFilter());
        }
        return status;
    }

    public void setStatus(OtpStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getExpiry() {
        return expiry;
    }

    public Optional<InstantFilter> optionalExpiry() {
        return Optional.ofNullable(expiry);
    }

    public InstantFilter expiry() {
        if (expiry == null) {
            setExpiry(new InstantFilter());
        }
        return expiry;
    }

    public void setExpiry(InstantFilter expiry) {
        this.expiry = expiry;
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
        final OneTimePasswordCriteria that = (OneTimePasswordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(user, that.user) &&
            Objects.equals(code, that.code) &&
            Objects.equals(status, that.status) &&
            Objects.equals(expiry, that.expiry) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, user, code, status, expiry, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OneTimePasswordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalUser().map(f -> "user=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalExpiry().map(f -> "expiry=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
