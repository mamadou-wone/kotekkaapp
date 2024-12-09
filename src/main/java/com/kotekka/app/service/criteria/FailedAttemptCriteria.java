package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.Action;
import com.kotekka.app.domain.enumeration.App;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.FailedAttempt} entity. This class is used
 * in {@link com.kotekka.app.web.rest.FailedAttemptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /failed-attempts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedAttemptCriteria implements Serializable, Criteria {

    /**
     * Class for filtering App
     */
    public static class AppFilter extends Filter<App> {

        public AppFilter() {}

        public AppFilter(AppFilter filter) {
            super(filter);
        }

        @Override
        public AppFilter copy() {
            return new AppFilter(this);
        }
    }

    /**
     * Class for filtering Action
     */
    public static class ActionFilter extends Filter<Action> {

        public ActionFilter() {}

        public ActionFilter(ActionFilter filter) {
            super(filter);
        }

        @Override
        public ActionFilter copy() {
            return new ActionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter login;

    private StringFilter ipAddress;

    private BooleanFilter isAfterLock;

    private AppFilter app;

    private ActionFilter action;

    private UUIDFilter device;

    private InstantFilter createdDate;

    private StringFilter reason;

    private Boolean distinct;

    public FailedAttemptCriteria() {}

    public FailedAttemptCriteria(FailedAttemptCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.login = other.optionalLogin().map(StringFilter::copy).orElse(null);
        this.ipAddress = other.optionalIpAddress().map(StringFilter::copy).orElse(null);
        this.isAfterLock = other.optionalIsAfterLock().map(BooleanFilter::copy).orElse(null);
        this.app = other.optionalApp().map(AppFilter::copy).orElse(null);
        this.action = other.optionalAction().map(ActionFilter::copy).orElse(null);
        this.device = other.optionalDevice().map(UUIDFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.reason = other.optionalReason().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FailedAttemptCriteria copy() {
        return new FailedAttemptCriteria(this);
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

    public StringFilter getLogin() {
        return login;
    }

    public Optional<StringFilter> optionalLogin() {
        return Optional.ofNullable(login);
    }

    public StringFilter login() {
        if (login == null) {
            setLogin(new StringFilter());
        }
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getIpAddress() {
        return ipAddress;
    }

    public Optional<StringFilter> optionalIpAddress() {
        return Optional.ofNullable(ipAddress);
    }

    public StringFilter ipAddress() {
        if (ipAddress == null) {
            setIpAddress(new StringFilter());
        }
        return ipAddress;
    }

    public void setIpAddress(StringFilter ipAddress) {
        this.ipAddress = ipAddress;
    }

    public BooleanFilter getIsAfterLock() {
        return isAfterLock;
    }

    public Optional<BooleanFilter> optionalIsAfterLock() {
        return Optional.ofNullable(isAfterLock);
    }

    public BooleanFilter isAfterLock() {
        if (isAfterLock == null) {
            setIsAfterLock(new BooleanFilter());
        }
        return isAfterLock;
    }

    public void setIsAfterLock(BooleanFilter isAfterLock) {
        this.isAfterLock = isAfterLock;
    }

    public AppFilter getApp() {
        return app;
    }

    public Optional<AppFilter> optionalApp() {
        return Optional.ofNullable(app);
    }

    public AppFilter app() {
        if (app == null) {
            setApp(new AppFilter());
        }
        return app;
    }

    public void setApp(AppFilter app) {
        this.app = app;
    }

    public ActionFilter getAction() {
        return action;
    }

    public Optional<ActionFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public ActionFilter action() {
        if (action == null) {
            setAction(new ActionFilter());
        }
        return action;
    }

    public void setAction(ActionFilter action) {
        this.action = action;
    }

    public UUIDFilter getDevice() {
        return device;
    }

    public Optional<UUIDFilter> optionalDevice() {
        return Optional.ofNullable(device);
    }

    public UUIDFilter device() {
        if (device == null) {
            setDevice(new UUIDFilter());
        }
        return device;
    }

    public void setDevice(UUIDFilter device) {
        this.device = device;
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

    public StringFilter getReason() {
        return reason;
    }

    public Optional<StringFilter> optionalReason() {
        return Optional.ofNullable(reason);
    }

    public StringFilter reason() {
        if (reason == null) {
            setReason(new StringFilter());
        }
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
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
        final FailedAttemptCriteria that = (FailedAttemptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(isAfterLock, that.isAfterLock) &&
            Objects.equals(app, that.app) &&
            Objects.equals(action, that.action) &&
            Objects.equals(device, that.device) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, ipAddress, isAfterLock, app, action, device, createdDate, reason, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedAttemptCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLogin().map(f -> "login=" + f + ", ").orElse("") +
            optionalIpAddress().map(f -> "ipAddress=" + f + ", ").orElse("") +
            optionalIsAfterLock().map(f -> "isAfterLock=" + f + ", ").orElse("") +
            optionalApp().map(f -> "app=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalDevice().map(f -> "device=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalReason().map(f -> "reason=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
