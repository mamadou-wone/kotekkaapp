package com.kotekka.app.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * CIN (Carte d'Identité Nationale)
 * Not Audited
 */
@Schema(description = "CIN (Carte d'Identité Nationale)\nNot Audited")
@Entity
@Table(name = "cin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "cin_id", length = 50)
    private String cinId;

    @Column(name = "validity_date")
    private LocalDate validityDate;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Size(max = 255)
    @Column(name = "birth_place", length = 255)
    private String birthPlace;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 255)
    @Column(name = "birth_city", length = 255)
    private String birthCity;

    @Size(max = 255)
    @Column(name = "father_name", length = 255)
    private String fatherName;

    @Size(max = 255)
    @Column(name = "nationality", length = 255)
    private String nationality;

    @Size(max = 255)
    @Column(name = "nationality_code", length = 255)
    private String nationalityCode;

    @Size(max = 255)
    @Column(name = "issuing_country", length = 255)
    private String issuingCountry;

    @Size(max = 3)
    @Column(name = "issuing_country_code", length = 3)
    private String issuingCountryCode;

    @Size(max = 255)
    @Column(name = "mother_name", length = 255)
    private String motherName;

    @Size(max = 255)
    @Column(name = "civil_register", length = 255)
    private String civilRegister;

    @Size(max = 3)
    @Column(name = "sex", length = 3)
    private String sex;

    @Size(max = 255)
    @Column(name = "address", length = 255)
    private String address;

    @Size(max = 3)
    @Column(name = "birth_city_code", length = 3)
    private String birthCityCode;

    @Column(name = "wallet_holder")
    private UUID walletHolder;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCinId() {
        return this.cinId;
    }

    public Cin cinId(String cinId) {
        this.setCinId(cinId);
        return this;
    }

    public void setCinId(String cinId) {
        this.cinId = cinId;
    }

    public LocalDate getValidityDate() {
        return this.validityDate;
    }

    public Cin validityDate(LocalDate validityDate) {
        this.setValidityDate(validityDate);
        return this;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Cin birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Cin birthPlace(String birthPlace) {
        this.setBirthPlace(birthPlace);
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Cin firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Cin lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthCity() {
        return this.birthCity;
    }

    public Cin birthCity(String birthCity) {
        this.setBirthCity(birthCity);
        return this;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public Cin fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Cin nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationalityCode() {
        return this.nationalityCode;
    }

    public Cin nationalityCode(String nationalityCode) {
        this.setNationalityCode(nationalityCode);
        return this;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getIssuingCountry() {
        return this.issuingCountry;
    }

    public Cin issuingCountry(String issuingCountry) {
        this.setIssuingCountry(issuingCountry);
        return this;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getIssuingCountryCode() {
        return this.issuingCountryCode;
    }

    public Cin issuingCountryCode(String issuingCountryCode) {
        this.setIssuingCountryCode(issuingCountryCode);
        return this;
    }

    public void setIssuingCountryCode(String issuingCountryCode) {
        this.issuingCountryCode = issuingCountryCode;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public Cin motherName(String motherName) {
        this.setMotherName(motherName);
        return this;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getCivilRegister() {
        return this.civilRegister;
    }

    public Cin civilRegister(String civilRegister) {
        this.setCivilRegister(civilRegister);
        return this;
    }

    public void setCivilRegister(String civilRegister) {
        this.civilRegister = civilRegister;
    }

    public String getSex() {
        return this.sex;
    }

    public Cin sex(String sex) {
        this.setSex(sex);
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return this.address;
    }

    public Cin address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthCityCode() {
        return this.birthCityCode;
    }

    public Cin birthCityCode(String birthCityCode) {
        this.setBirthCityCode(birthCityCode);
        return this;
    }

    public void setBirthCityCode(String birthCityCode) {
        this.birthCityCode = birthCityCode;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public Cin walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Cin createdDate(Instant createdDate) {
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
        if (!(o instanceof Cin)) {
            return false;
        }
        return getId() != null && getId().equals(((Cin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cin{" +
            "id=" + getId() +
            ", cinId='" + getCinId() + "'" +
            ", validityDate='" + getValidityDate() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthCity='" + getBirthCity() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", nationalityCode='" + getNationalityCode() + "'" +
            ", issuingCountry='" + getIssuingCountry() + "'" +
            ", issuingCountryCode='" + getIssuingCountryCode() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", civilRegister='" + getCivilRegister() + "'" +
            ", sex='" + getSex() + "'" +
            ", address='" + getAddress() + "'" +
            ", birthCityCode='" + getBirthCityCode() + "'" +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
