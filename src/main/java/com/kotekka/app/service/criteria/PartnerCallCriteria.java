package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.HttpMethod;
import com.kotekka.app.domain.enumeration.Partner;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.PartnerCall} entity. This class is used
 * in {@link com.kotekka.app.web.rest.PartnerCallResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /partner-calls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerCallCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Partner
     */
    public static class PartnerFilter extends Filter<Partner> {

        public PartnerFilter() {}

        public PartnerFilter(PartnerFilter filter) {
            super(filter);
        }

        @Override
        public PartnerFilter copy() {
            return new PartnerFilter(this);
        }
    }

    /**
     * Class for filtering HttpMethod
     */
    public static class HttpMethodFilter extends Filter<HttpMethod> {

        public HttpMethodFilter() {}

        public HttpMethodFilter(HttpMethodFilter filter) {
            super(filter);
        }

        @Override
        public HttpMethodFilter copy() {
            return new HttpMethodFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PartnerFilter partner;

    private StringFilter api;

    private HttpMethodFilter method;

    private InstantFilter requestTime;

    private IntegerFilter responseStatusCode;

    private InstantFilter responseTime;

    private StringFilter correlationId;

    private StringFilter queryParam;

    private Boolean distinct;

    public PartnerCallCriteria() {}

    public PartnerCallCriteria(PartnerCallCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.partner = other.optionalPartner().map(PartnerFilter::copy).orElse(null);
        this.api = other.optionalApi().map(StringFilter::copy).orElse(null);
        this.method = other.optionalMethod().map(HttpMethodFilter::copy).orElse(null);
        this.requestTime = other.optionalRequestTime().map(InstantFilter::copy).orElse(null);
        this.responseStatusCode = other.optionalResponseStatusCode().map(IntegerFilter::copy).orElse(null);
        this.responseTime = other.optionalResponseTime().map(InstantFilter::copy).orElse(null);
        this.correlationId = other.optionalCorrelationId().map(StringFilter::copy).orElse(null);
        this.queryParam = other.optionalQueryParam().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PartnerCallCriteria copy() {
        return new PartnerCallCriteria(this);
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

    public PartnerFilter getPartner() {
        return partner;
    }

    public Optional<PartnerFilter> optionalPartner() {
        return Optional.ofNullable(partner);
    }

    public PartnerFilter partner() {
        if (partner == null) {
            setPartner(new PartnerFilter());
        }
        return partner;
    }

    public void setPartner(PartnerFilter partner) {
        this.partner = partner;
    }

    public StringFilter getApi() {
        return api;
    }

    public Optional<StringFilter> optionalApi() {
        return Optional.ofNullable(api);
    }

    public StringFilter api() {
        if (api == null) {
            setApi(new StringFilter());
        }
        return api;
    }

    public void setApi(StringFilter api) {
        this.api = api;
    }

    public HttpMethodFilter getMethod() {
        return method;
    }

    public Optional<HttpMethodFilter> optionalMethod() {
        return Optional.ofNullable(method);
    }

    public HttpMethodFilter method() {
        if (method == null) {
            setMethod(new HttpMethodFilter());
        }
        return method;
    }

    public void setMethod(HttpMethodFilter method) {
        this.method = method;
    }

    public InstantFilter getRequestTime() {
        return requestTime;
    }

    public Optional<InstantFilter> optionalRequestTime() {
        return Optional.ofNullable(requestTime);
    }

    public InstantFilter requestTime() {
        if (requestTime == null) {
            setRequestTime(new InstantFilter());
        }
        return requestTime;
    }

    public void setRequestTime(InstantFilter requestTime) {
        this.requestTime = requestTime;
    }

    public IntegerFilter getResponseStatusCode() {
        return responseStatusCode;
    }

    public Optional<IntegerFilter> optionalResponseStatusCode() {
        return Optional.ofNullable(responseStatusCode);
    }

    public IntegerFilter responseStatusCode() {
        if (responseStatusCode == null) {
            setResponseStatusCode(new IntegerFilter());
        }
        return responseStatusCode;
    }

    public void setResponseStatusCode(IntegerFilter responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public InstantFilter getResponseTime() {
        return responseTime;
    }

    public Optional<InstantFilter> optionalResponseTime() {
        return Optional.ofNullable(responseTime);
    }

    public InstantFilter responseTime() {
        if (responseTime == null) {
            setResponseTime(new InstantFilter());
        }
        return responseTime;
    }

    public void setResponseTime(InstantFilter responseTime) {
        this.responseTime = responseTime;
    }

    public StringFilter getCorrelationId() {
        return correlationId;
    }

    public Optional<StringFilter> optionalCorrelationId() {
        return Optional.ofNullable(correlationId);
    }

    public StringFilter correlationId() {
        if (correlationId == null) {
            setCorrelationId(new StringFilter());
        }
        return correlationId;
    }

    public void setCorrelationId(StringFilter correlationId) {
        this.correlationId = correlationId;
    }

    public StringFilter getQueryParam() {
        return queryParam;
    }

    public Optional<StringFilter> optionalQueryParam() {
        return Optional.ofNullable(queryParam);
    }

    public StringFilter queryParam() {
        if (queryParam == null) {
            setQueryParam(new StringFilter());
        }
        return queryParam;
    }

    public void setQueryParam(StringFilter queryParam) {
        this.queryParam = queryParam;
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
        final PartnerCallCriteria that = (PartnerCallCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(partner, that.partner) &&
            Objects.equals(api, that.api) &&
            Objects.equals(method, that.method) &&
            Objects.equals(requestTime, that.requestTime) &&
            Objects.equals(responseStatusCode, that.responseStatusCode) &&
            Objects.equals(responseTime, that.responseTime) &&
            Objects.equals(correlationId, that.correlationId) &&
            Objects.equals(queryParam, that.queryParam) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partner, api, method, requestTime, responseStatusCode, responseTime, correlationId, queryParam, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerCallCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPartner().map(f -> "partner=" + f + ", ").orElse("") +
            optionalApi().map(f -> "api=" + f + ", ").orElse("") +
            optionalMethod().map(f -> "method=" + f + ", ").orElse("") +
            optionalRequestTime().map(f -> "requestTime=" + f + ", ").orElse("") +
            optionalResponseStatusCode().map(f -> "responseStatusCode=" + f + ", ").orElse("") +
            optionalResponseTime().map(f -> "responseTime=" + f + ", ").orElse("") +
            optionalCorrelationId().map(f -> "correlationId=" + f + ", ").orElse("") +
            optionalQueryParam().map(f -> "queryParam=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
