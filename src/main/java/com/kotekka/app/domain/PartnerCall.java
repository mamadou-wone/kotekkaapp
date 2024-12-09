package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.HttpMethod;
import com.kotekka.app.domain.enumeration.Partner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Call to partners by back-end
 * Not Audited
 */
@Schema(description = "Call to partners by back-end\nNot Audited")
@Entity
@Table(name = "partner_call")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerCall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "partner", nullable = false)
    private Partner partner;

    @Size(max = 255)
    @Column(name = "api", length = 255)
    private String api;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private HttpMethod method;

    @Lob
    @Column(name = "request_headers")
    private String requestHeaders;

    @Lob
    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "request_time")
    private Instant requestTime;

    @Max(value = 999)
    @Column(name = "response_status_code")
    private Integer responseStatusCode;

    @Lob
    @Column(name = "response_headers")
    private String responseHeaders;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "response_time")
    private Instant responseTime;

    @Size(max = 50)
    @Column(name = "correlation_id", length = 50)
    private String correlationId;

    @Size(max = 255)
    @Column(name = "query_param", length = 255)
    private String queryParam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartnerCall id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public PartnerCall partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getApi() {
        return this.api;
    }

    public PartnerCall api(String api) {
        this.setApi(api);
        return this;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public PartnerCall method(HttpMethod method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getRequestHeaders() {
        return this.requestHeaders;
    }

    public PartnerCall requestHeaders(String requestHeaders) {
        this.setRequestHeaders(requestHeaders);
        return this;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    public PartnerCall requestBody(String requestBody) {
        this.setRequestBody(requestBody);
        return this;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Instant getRequestTime() {
        return this.requestTime;
    }

    public PartnerCall requestTime(Instant requestTime) {
        this.setRequestTime(requestTime);
        return this;
    }

    public void setRequestTime(Instant requestTime) {
        this.requestTime = requestTime;
    }

    public Integer getResponseStatusCode() {
        return this.responseStatusCode;
    }

    public PartnerCall responseStatusCode(Integer responseStatusCode) {
        this.setResponseStatusCode(responseStatusCode);
        return this;
    }

    public void setResponseStatusCode(Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public String getResponseHeaders() {
        return this.responseHeaders;
    }

    public PartnerCall responseHeaders(String responseHeaders) {
        this.setResponseHeaders(responseHeaders);
        return this;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public PartnerCall responseBody(String responseBody) {
        this.setResponseBody(responseBody);
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Instant getResponseTime() {
        return this.responseTime;
    }

    public PartnerCall responseTime(Instant responseTime) {
        this.setResponseTime(responseTime);
        return this;
    }

    public void setResponseTime(Instant responseTime) {
        this.responseTime = responseTime;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public PartnerCall correlationId(String correlationId) {
        this.setCorrelationId(correlationId);
        return this;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getQueryParam() {
        return this.queryParam;
    }

    public PartnerCall queryParam(String queryParam) {
        this.setQueryParam(queryParam);
        return this;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerCall)) {
            return false;
        }
        return getId() != null && getId().equals(((PartnerCall) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerCall{" +
            "id=" + getId() +
            ", partner='" + getPartner() + "'" +
            ", api='" + getApi() + "'" +
            ", method='" + getMethod() + "'" +
            ", requestHeaders='" + getRequestHeaders() + "'" +
            ", requestBody='" + getRequestBody() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            ", responseStatusCode=" + getResponseStatusCode() +
            ", responseHeaders='" + getResponseHeaders() + "'" +
            ", responseBody='" + getResponseBody() + "'" +
            ", responseTime='" + getResponseTime() + "'" +
            ", correlationId='" + getCorrelationId() + "'" +
            ", queryParam='" + getQueryParam() + "'" +
            "}";
    }
}
