package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.AccountStatus;
import com.kotekka.app.domain.enumeration.AccountType;
import com.kotekka.app.domain.enumeration.LoginStatus;
import com.kotekka.app.domain.enumeration.Network;
import com.kotekka.app.domain.enumeration.OnboardingStatus;
import com.kotekka.app.domain.enumeration.Sex;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.WalletHolder} entity. This class is used
 * in {@link com.kotekka.app.web.rest.WalletHolderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wallet-holders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WalletHolderCriteria implements Serializable, Criteria {

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
     * Class for filtering AccountStatus
     */
    public static class AccountStatusFilter extends Filter<AccountStatus> {

        public AccountStatusFilter() {}

        public AccountStatusFilter(AccountStatusFilter filter) {
            super(filter);
        }

        @Override
        public AccountStatusFilter copy() {
            return new AccountStatusFilter(this);
        }
    }

    /**
     * Class for filtering Network
     */
    public static class NetworkFilter extends Filter<Network> {

        public NetworkFilter() {}

        public NetworkFilter(NetworkFilter filter) {
            super(filter);
        }

        @Override
        public NetworkFilter copy() {
            return new NetworkFilter(this);
        }
    }

    /**
     * Class for filtering OnboardingStatus
     */
    public static class OnboardingStatusFilter extends Filter<OnboardingStatus> {

        public OnboardingStatusFilter() {}

        public OnboardingStatusFilter(OnboardingStatusFilter filter) {
            super(filter);
        }

        @Override
        public OnboardingStatusFilter copy() {
            return new OnboardingStatusFilter(this);
        }
    }

    /**
     * Class for filtering Sex
     */
    public static class SexFilter extends Filter<Sex> {

        public SexFilter() {}

        public SexFilter(SexFilter filter) {
            super(filter);
        }

        @Override
        public SexFilter copy() {
            return new SexFilter(this);
        }
    }

    /**
     * Class for filtering LoginStatus
     */
    public static class LoginStatusFilter extends Filter<LoginStatus> {

        public LoginStatusFilter() {}

        public LoginStatusFilter(LoginStatusFilter filter) {
            super(filter);
        }

        @Override
        public LoginStatusFilter copy() {
            return new LoginStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private AccountTypeFilter type;

    private AccountStatusFilter status;

    private StringFilter phoneNumber;

    private NetworkFilter network;

    private StringFilter tag;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter address;

    private StringFilter city;

    private StringFilter country;

    private StringFilter postalCode;

    private OnboardingStatusFilter onboarding;

    private StringFilter externalId;

    private StringFilter email;

    private LocalDateFilter dateOfBirth;

    private SexFilter sex;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LoginStatusFilter loginStatus;

    private LongFilter userId;

    private Boolean distinct;

    public WalletHolderCriteria() {}

    public WalletHolderCriteria(WalletHolderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.type = other.optionalType().map(AccountTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AccountStatusFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.network = other.optionalNetwork().map(NetworkFilter::copy).orElse(null);
        this.tag = other.optionalTag().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(StringFilter::copy).orElse(null);
        this.onboarding = other.optionalOnboarding().map(OnboardingStatusFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.dateOfBirth = other.optionalDateOfBirth().map(LocalDateFilter::copy).orElse(null);
        this.sex = other.optionalSex().map(SexFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.loginStatus = other.optionalLoginStatus().map(LoginStatusFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WalletHolderCriteria copy() {
        return new WalletHolderCriteria(this);
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

    public UUIDFilter getUuid() {
        return uuid;
    }

    public Optional<UUIDFilter> optionalUuid() {
        return Optional.ofNullable(uuid);
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            setUuid(new UUIDFilter());
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
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

    public AccountStatusFilter getStatus() {
        return status;
    }

    public Optional<AccountStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AccountStatusFilter status() {
        if (status == null) {
            setStatus(new AccountStatusFilter());
        }
        return status;
    }

    public void setStatus(AccountStatusFilter status) {
        this.status = status;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public NetworkFilter getNetwork() {
        return network;
    }

    public Optional<NetworkFilter> optionalNetwork() {
        return Optional.ofNullable(network);
    }

    public NetworkFilter network() {
        if (network == null) {
            setNetwork(new NetworkFilter());
        }
        return network;
    }

    public void setNetwork(NetworkFilter network) {
        this.network = network;
    }

    public StringFilter getTag() {
        return tag;
    }

    public Optional<StringFilter> optionalTag() {
        return Optional.ofNullable(tag);
    }

    public StringFilter tag() {
        if (tag == null) {
            setTag(new StringFilter());
        }
        return tag;
    }

    public void setTag(StringFilter tag) {
        this.tag = tag;
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

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public Optional<StringFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new StringFilter());
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public OnboardingStatusFilter getOnboarding() {
        return onboarding;
    }

    public Optional<OnboardingStatusFilter> optionalOnboarding() {
        return Optional.ofNullable(onboarding);
    }

    public OnboardingStatusFilter onboarding() {
        if (onboarding == null) {
            setOnboarding(new OnboardingStatusFilter());
        }
        return onboarding;
    }

    public void setOnboarding(OnboardingStatusFilter onboarding) {
        this.onboarding = onboarding;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public Optional<StringFilter> optionalExternalId() {
        return Optional.ofNullable(externalId);
    }

    public StringFilter externalId() {
        if (externalId == null) {
            setExternalId(new StringFilter());
        }
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<LocalDateFilter> optionalDateOfBirth() {
        return Optional.ofNullable(dateOfBirth);
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            setDateOfBirth(new LocalDateFilter());
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public SexFilter getSex() {
        return sex;
    }

    public Optional<SexFilter> optionalSex() {
        return Optional.ofNullable(sex);
    }

    public SexFilter sex() {
        if (sex == null) {
            setSex(new SexFilter());
        }
        return sex;
    }

    public void setSex(SexFilter sex) {
        this.sex = sex;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
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

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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

    public LoginStatusFilter getLoginStatus() {
        return loginStatus;
    }

    public Optional<LoginStatusFilter> optionalLoginStatus() {
        return Optional.ofNullable(loginStatus);
    }

    public LoginStatusFilter loginStatus() {
        if (loginStatus == null) {
            setLoginStatus(new LoginStatusFilter());
        }
        return loginStatus;
    }

    public void setLoginStatus(LoginStatusFilter loginStatus) {
        this.loginStatus = loginStatus;
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
        final WalletHolderCriteria that = (WalletHolderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(network, that.network) &&
            Objects.equals(tag, that.tag) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(onboarding, that.onboarding) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(loginStatus, that.loginStatus) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            type,
            status,
            phoneNumber,
            network,
            tag,
            firstName,
            lastName,
            address,
            city,
            country,
            postalCode,
            onboarding,
            externalId,
            email,
            dateOfBirth,
            sex,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            loginStatus,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WalletHolderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalNetwork().map(f -> "network=" + f + ", ").orElse("") +
            optionalTag().map(f -> "tag=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalOnboarding().map(f -> "onboarding=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalDateOfBirth().map(f -> "dateOfBirth=" + f + ", ").orElse("") +
            optionalSex().map(f -> "sex=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalLoginStatus().map(f -> "loginStatus=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
