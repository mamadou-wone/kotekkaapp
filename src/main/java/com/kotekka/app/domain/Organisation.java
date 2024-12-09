package com.kotekka.app.domain;

import com.kotekka.app.domain.enumeration.OrgType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organisation.
 */
@Entity
@Table(name = "organisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrgType type;

    @Size(max = 50)
    @Column(name = "parent", length = 50)
    private String parent;

    @Size(max = 50)
    @Column(name = "location", length = 50)
    private String location;

    @Column(name = "headcount")
    private Integer headcount;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Organisation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrgType getType() {
        return this.type;
    }

    public Organisation type(OrgType type) {
        this.setType(type);
        return this;
    }

    public void setType(OrgType type) {
        this.type = type;
    }

    public String getParent() {
        return this.parent;
    }

    public Organisation parent(String parent) {
        this.setParent(parent);
        return this;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLocation() {
        return this.location;
    }

    public Organisation location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHeadcount() {
        return this.headcount;
    }

    public Organisation headcount(Integer headcount) {
        this.setHeadcount(headcount);
        return this;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Organisation createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Organisation createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organisation)) {
            return false;
        }
        return getId() != null && getId().equals(((Organisation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", parent='" + getParent() + "'" +
            ", location='" + getLocation() + "'" +
            ", headcount=" + getHeadcount() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
