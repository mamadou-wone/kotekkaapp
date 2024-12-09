package com.kotekka.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.Cin} entity. This class is used
 * in {@link com.kotekka.app.web.rest.CinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CinCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cinId;

    private LocalDateFilter validityDate;

    private LocalDateFilter birthDate;

    private StringFilter birthPlace;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter birthCity;

    private StringFilter fatherName;

    private StringFilter nationality;

    private StringFilter nationalityCode;

    private StringFilter issuingCountry;

    private StringFilter issuingCountryCode;

    private StringFilter motherName;

    private StringFilter civilRegister;

    private StringFilter sex;

    private StringFilter address;

    private StringFilter birthCityCode;

    private UUIDFilter walletHolder;

    private InstantFilter createdDate;

    private Boolean distinct;

    public CinCriteria() {}

    public CinCriteria(CinCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cinId = other.optionalCinId().map(StringFilter::copy).orElse(null);
        this.validityDate = other.optionalValidityDate().map(LocalDateFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.birthPlace = other.optionalBirthPlace().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.birthCity = other.optionalBirthCity().map(StringFilter::copy).orElse(null);
        this.fatherName = other.optionalFatherName().map(StringFilter::copy).orElse(null);
        this.nationality = other.optionalNationality().map(StringFilter::copy).orElse(null);
        this.nationalityCode = other.optionalNationalityCode().map(StringFilter::copy).orElse(null);
        this.issuingCountry = other.optionalIssuingCountry().map(StringFilter::copy).orElse(null);
        this.issuingCountryCode = other.optionalIssuingCountryCode().map(StringFilter::copy).orElse(null);
        this.motherName = other.optionalMotherName().map(StringFilter::copy).orElse(null);
        this.civilRegister = other.optionalCivilRegister().map(StringFilter::copy).orElse(null);
        this.sex = other.optionalSex().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.birthCityCode = other.optionalBirthCityCode().map(StringFilter::copy).orElse(null);
        this.walletHolder = other.optionalWalletHolder().map(UUIDFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CinCriteria copy() {
        return new CinCriteria(this);
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

    public StringFilter getCinId() {
        return cinId;
    }

    public Optional<StringFilter> optionalCinId() {
        return Optional.ofNullable(cinId);
    }

    public StringFilter cinId() {
        if (cinId == null) {
            setCinId(new StringFilter());
        }
        return cinId;
    }

    public void setCinId(StringFilter cinId) {
        this.cinId = cinId;
    }

    public LocalDateFilter getValidityDate() {
        return validityDate;
    }

    public Optional<LocalDateFilter> optionalValidityDate() {
        return Optional.ofNullable(validityDate);
    }

    public LocalDateFilter validityDate() {
        if (validityDate == null) {
            setValidityDate(new LocalDateFilter());
        }
        return validityDate;
    }

    public void setValidityDate(LocalDateFilter validityDate) {
        this.validityDate = validityDate;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getBirthPlace() {
        return birthPlace;
    }

    public Optional<StringFilter> optionalBirthPlace() {
        return Optional.ofNullable(birthPlace);
    }

    public StringFilter birthPlace() {
        if (birthPlace == null) {
            setBirthPlace(new StringFilter());
        }
        return birthPlace;
    }

    public void setBirthPlace(StringFilter birthPlace) {
        this.birthPlace = birthPlace;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getBirthCity() {
        return birthCity;
    }

    public Optional<StringFilter> optionalBirthCity() {
        return Optional.ofNullable(birthCity);
    }

    public StringFilter birthCity() {
        if (birthCity == null) {
            setBirthCity(new StringFilter());
        }
        return birthCity;
    }

    public void setBirthCity(StringFilter birthCity) {
        this.birthCity = birthCity;
    }

    public StringFilter getFatherName() {
        return fatherName;
    }

    public Optional<StringFilter> optionalFatherName() {
        return Optional.ofNullable(fatherName);
    }

    public StringFilter fatherName() {
        if (fatherName == null) {
            setFatherName(new StringFilter());
        }
        return fatherName;
    }

    public void setFatherName(StringFilter fatherName) {
        this.fatherName = fatherName;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public Optional<StringFilter> optionalNationality() {
        return Optional.ofNullable(nationality);
    }

    public StringFilter nationality() {
        if (nationality == null) {
            setNationality(new StringFilter());
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getNationalityCode() {
        return nationalityCode;
    }

    public Optional<StringFilter> optionalNationalityCode() {
        return Optional.ofNullable(nationalityCode);
    }

    public StringFilter nationalityCode() {
        if (nationalityCode == null) {
            setNationalityCode(new StringFilter());
        }
        return nationalityCode;
    }

    public void setNationalityCode(StringFilter nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public StringFilter getIssuingCountry() {
        return issuingCountry;
    }

    public Optional<StringFilter> optionalIssuingCountry() {
        return Optional.ofNullable(issuingCountry);
    }

    public StringFilter issuingCountry() {
        if (issuingCountry == null) {
            setIssuingCountry(new StringFilter());
        }
        return issuingCountry;
    }

    public void setIssuingCountry(StringFilter issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public StringFilter getIssuingCountryCode() {
        return issuingCountryCode;
    }

    public Optional<StringFilter> optionalIssuingCountryCode() {
        return Optional.ofNullable(issuingCountryCode);
    }

    public StringFilter issuingCountryCode() {
        if (issuingCountryCode == null) {
            setIssuingCountryCode(new StringFilter());
        }
        return issuingCountryCode;
    }

    public void setIssuingCountryCode(StringFilter issuingCountryCode) {
        this.issuingCountryCode = issuingCountryCode;
    }

    public StringFilter getMotherName() {
        return motherName;
    }

    public Optional<StringFilter> optionalMotherName() {
        return Optional.ofNullable(motherName);
    }

    public StringFilter motherName() {
        if (motherName == null) {
            setMotherName(new StringFilter());
        }
        return motherName;
    }

    public void setMotherName(StringFilter motherName) {
        this.motherName = motherName;
    }

    public StringFilter getCivilRegister() {
        return civilRegister;
    }

    public Optional<StringFilter> optionalCivilRegister() {
        return Optional.ofNullable(civilRegister);
    }

    public StringFilter civilRegister() {
        if (civilRegister == null) {
            setCivilRegister(new StringFilter());
        }
        return civilRegister;
    }

    public void setCivilRegister(StringFilter civilRegister) {
        this.civilRegister = civilRegister;
    }

    public StringFilter getSex() {
        return sex;
    }

    public Optional<StringFilter> optionalSex() {
        return Optional.ofNullable(sex);
    }

    public StringFilter sex() {
        if (sex == null) {
            setSex(new StringFilter());
        }
        return sex;
    }

    public void setSex(StringFilter sex) {
        this.sex = sex;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getBirthCityCode() {
        return birthCityCode;
    }

    public Optional<StringFilter> optionalBirthCityCode() {
        return Optional.ofNullable(birthCityCode);
    }

    public StringFilter birthCityCode() {
        if (birthCityCode == null) {
            setBirthCityCode(new StringFilter());
        }
        return birthCityCode;
    }

    public void setBirthCityCode(StringFilter birthCityCode) {
        this.birthCityCode = birthCityCode;
    }

    public UUIDFilter getWalletHolder() {
        return walletHolder;
    }

    public Optional<UUIDFilter> optionalWalletHolder() {
        return Optional.ofNullable(walletHolder);
    }

    public UUIDFilter walletHolder() {
        if (walletHolder == null) {
            setWalletHolder(new UUIDFilter());
        }
        return walletHolder;
    }

    public void setWalletHolder(UUIDFilter walletHolder) {
        this.walletHolder = walletHolder;
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
        final CinCriteria that = (CinCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cinId, that.cinId) &&
            Objects.equals(validityDate, that.validityDate) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(birthPlace, that.birthPlace) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthCity, that.birthCity) &&
            Objects.equals(fatherName, that.fatherName) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(nationalityCode, that.nationalityCode) &&
            Objects.equals(issuingCountry, that.issuingCountry) &&
            Objects.equals(issuingCountryCode, that.issuingCountryCode) &&
            Objects.equals(motherName, that.motherName) &&
            Objects.equals(civilRegister, that.civilRegister) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(address, that.address) &&
            Objects.equals(birthCityCode, that.birthCityCode) &&
            Objects.equals(walletHolder, that.walletHolder) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cinId,
            validityDate,
            birthDate,
            birthPlace,
            firstName,
            lastName,
            birthCity,
            fatherName,
            nationality,
            nationalityCode,
            issuingCountry,
            issuingCountryCode,
            motherName,
            civilRegister,
            sex,
            address,
            birthCityCode,
            walletHolder,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CinCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCinId().map(f -> "cinId=" + f + ", ").orElse("") +
            optionalValidityDate().map(f -> "validityDate=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalBirthPlace().map(f -> "birthPlace=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalBirthCity().map(f -> "birthCity=" + f + ", ").orElse("") +
            optionalFatherName().map(f -> "fatherName=" + f + ", ").orElse("") +
            optionalNationality().map(f -> "nationality=" + f + ", ").orElse("") +
            optionalNationalityCode().map(f -> "nationalityCode=" + f + ", ").orElse("") +
            optionalIssuingCountry().map(f -> "issuingCountry=" + f + ", ").orElse("") +
            optionalIssuingCountryCode().map(f -> "issuingCountryCode=" + f + ", ").orElse("") +
            optionalMotherName().map(f -> "motherName=" + f + ", ").orElse("") +
            optionalCivilRegister().map(f -> "civilRegister=" + f + ", ").orElse("") +
            optionalSex().map(f -> "sex=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalBirthCityCode().map(f -> "birthCityCode=" + f + ", ").orElse("") +
            optionalWalletHolder().map(f -> "walletHolder=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
