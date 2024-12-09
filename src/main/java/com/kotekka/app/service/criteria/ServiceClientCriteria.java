package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.AccountType;
import com.kotekka.app.domain.enumeration.DefaultStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.ServiceClient} entity. This class is used
 * in {@link com.kotekka.app.web.rest.ServiceClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /service-clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceClientCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountType
     */
    public static class AccountTypeFilter extends Filter<AccountType> {

        public AccountTypeFilter() {}

        public AccountTypeFilter(AccountTypeFilter filter) {
            super(filter);
        }

        @Override
        public AccountTypeFilter copy() {
            return new AccountTypeFilter(this);
        }
    }

    /**
     * Class for filtering DefaultStatus
     */
    public static class DefaultStatusFilter extends Filter<DefaultStatus> {

        public DefaultStatusFilter() {}

        public DefaultStatusFilter(DefaultStatusFilter filter) {
            super(filter);
        }

        @Override
        public DefaultStatusFilter copy() {
            return new DefaultStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter clientId;

    private AccountTypeFilter type;

    private UUIDFilter apiKey;

    private DefaultStatusFilter status;

    private StringFilter note;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public ServiceClientCriteria() {}

    public ServiceClientCriteria(ServiceClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.type = other.optionalType().map(AccountTypeFilter::copy).orElse(null);
        this.apiKey = other.optionalApiKey().map(UUIDFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DefaultStatusFilter::copy).orElse(null);
        this.note = other.optionalNote().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServiceClientCriteria copy() {
        return new ServiceClientCriteria(this);
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

    public UUIDFilter getClientId() {
        return clientId;
    }

    public Optional<UUIDFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public UUIDFilter clientId() {
        if (clientId == null) {
            setClientId(new UUIDFilter());
        }
        return clientId;
    }

    public void setClientId(UUIDFilter clientId) {
        this.clientId = clientId;
    }

    public AccountTypeFilter getType() {
        return type;
    }

    public Optional<AccountTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public AccountTypeFilter type() {
        if (type == null) {
            setType(new AccountTypeFilter());
        }
        return type;
    }

    public void setType(AccountTypeFilter type) {
        this.type = type;
    }

    public UUIDFilter getApiKey() {
        return apiKey;
    }

    public Optional<UUIDFilter> optionalApiKey() {
        return Optional.ofNullable(apiKey);
    }

    public UUIDFilter apiKey() {
        if (apiKey == null) {
            setApiKey(new UUIDFilter());
        }
        return apiKey;
    }

    public void setApiKey(UUIDFilter apiKey) {
        this.apiKey = apiKey;
    }

    public DefaultStatusFilter getStatus() {
        return status;
    }

    public Optional<DefaultStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DefaultStatusFilter status() {
        if (status == null) {
            setStatus(new DefaultStatusFilter());
        }
        return status;
    }

    public void setStatus(DefaultStatusFilter status) {
        this.status = status;
    }

    public StringFilter getNote() {
        return note;
    }

    public Optional<StringFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public StringFilter note() {
        if (note == null) {
            setNote(new StringFilter());
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
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
        final ServiceClientCriteria that = (ServiceClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(type, that.type) &&
            Objects.equals(apiKey, that.apiKey) &&
            Objects.equals(status, that.status) &&
            Objects.equals(note, that.note) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, type, apiKey, status, note, createdDate, lastModifiedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalApiKey().map(f -> "apiKey=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
