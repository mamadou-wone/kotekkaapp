package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.App;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.UserPreference} entity. This class is used
 * in {@link com.kotekka.app.web.rest.UserPreferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreferenceCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private AppFilter app;

    private StringFilter name;

    private StringFilter setting;

    private InstantFilter createdDate;

    private LongFilter userId;

    private Boolean distinct;

    public UserPreferenceCriteria() {}

    public UserPreferenceCriteria(UserPreferenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.app = other.optionalApp().map(AppFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.setting = other.optionalSetting().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserPreferenceCriteria copy() {
        return new UserPreferenceCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSetting() {
        return setting;
    }

    public Optional<StringFilter> optionalSetting() {
        return Optional.ofNullable(setting);
    }

    public StringFilter setting() {
        if (setting == null) {
            setSetting(new StringFilter());
        }
        return setting;
    }

    public void setSetting(StringFilter setting) {
        this.setting = setting;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final UserPreferenceCriteria that = (UserPreferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(app, that.app) &&
            Objects.equals(name, that.name) &&
            Objects.equals(setting, that.setting) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, name, setting, createdDate, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalApp().map(f -> "app=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSetting().map(f -> "setting=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
