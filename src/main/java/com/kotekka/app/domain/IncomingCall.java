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
 * Incoming Call from partner
 * Either webhook callback, or normal api call
 * Not Audited
 */
@Schema(description = "Incoming Call from partner\nEither webhook callback, or normal api call\nNot Audited")
@Entity
@Table(name = "incoming_call")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomingCall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner")
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

    @Column(name = "created_date")
    private Instant createdDate;

    @Max(value = 999)
    @Column(name = "response_status_code")
    private Integer responseStatusCode;

    @Column(name = "response_time")
    private Instant responseTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncomingCall id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public IncomingCall partner(Partner partner) {
        this.setPartner(partner);
        return this;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getApi() {
        return this.api;
    }

    public IncomingCall api(String api) {
        this.setApi(api);
        return this;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public IncomingCall method(HttpMethod method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getRequestHeaders() {
        return this.requestHeaders;
    }

    public IncomingCall requestHeaders(String requestHeaders) {
        this.setRequestHeaders(requestHeaders);
        return this;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    public IncomingCall requestBody(String requestBody) {
        this.setRequestBody(requestBody);
        return this;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public IncomingCall createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getResponseStatusCode() {
        return this.responseStatusCode;
    }

    public IncomingCall responseStatusCode(Integer responseStatusCode) {
        this.setResponseStatusCode(responseStatusCode);
        return this;
    }

    public void setResponseStatusCode(Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public Instant getResponseTime() {
        return this.responseTime;
    }

    public IncomingCall responseTime(Instant responseTime) {
        this.setResponseTime(responseTime);
        return this;
    }

    public void setResponseTime(Instant responseTime) {
        this.responseTime = responseTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomingCall)) {
            return false;
        }
        return getId() != null && getId().equals(((IncomingCall) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomingCall{" +
            "id=" + getId() +
            ", partner='" + getPartner() + "'" +
            ", api='" + getApi() + "'" +
            ", method='" + getMethod() + "'" +
            ", requestHeaders='" + getRequestHeaders() + "'" +
            ", requestBody='" + getRequestBody() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", responseStatusCode=" + getResponseStatusCode() +
            ", responseTime='" + getResponseTime() + "'" +
            "}";
    }
}
