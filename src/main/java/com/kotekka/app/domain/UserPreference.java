package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.App;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * User Preference
 * Not Audited
 */
@Schema(description = "User Preference\nNot Audited")
@Entity
@Table(name = "user_preference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "app")
    private App app;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Size(max = 100)
    @Column(name = "setting", length = 100)
    private String setting;

    @Column(name = "created_date")
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPreference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public App getApp() {
        return this.app;
    }

    public UserPreference app(App app) {
        this.setApp(app);
        return this;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public String getName() {
        return this.name;
    }

    public UserPreference name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetting() {
        return this.setting;
    }

    public UserPreference setting(String setting) {
        this.setSetting(setting);
        return this;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserPreference createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPreference user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreference)) {
            return false;
        }
        return getId() != null && getId().equals(((UserPreference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreference{" +
            "id=" + getId() +
            ", app='" + getApp() + "'" +
            ", name='" + getName() + "'" +
            ", setting='" + getSetting() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
