package com.kotekka.app.service.criteria;

import com.kotekka.app.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kotekka.app.domain.Product} entity. This class is used
 * in {@link com.kotekka.app.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProductStatus
     */
    public static class ProductStatusFilter extends Filter<ProductStatus> {

        public ProductStatusFilter() {}

        public ProductStatusFilter(ProductStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProductStatusFilter copy() {
            return new ProductStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private UUIDFilter walletHolder;

    private StringFilter title;

    private StringFilter description;

    private ProductStatusFilter status;

    private BigDecimalFilter price;

    private BigDecimalFilter compareAtPrice;

    private BigDecimalFilter costPerItem;

    private BigDecimalFilter profit;

    private BigDecimalFilter margin;

    private IntegerFilter inventoryQuantity;

    private StringFilter inventoryLocation;

    private BooleanFilter trackQuantity;

    private LongFilter categoryId;

    private LongFilter collectionsId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.walletHolder = other.optionalWalletHolder().map(UUIDFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ProductStatusFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.compareAtPrice = other.optionalCompareAtPrice().map(BigDecimalFilter::copy).orElse(null);
        this.costPerItem = other.optionalCostPerItem().map(BigDecimalFilter::copy).orElse(null);
        this.profit = other.optionalProfit().map(BigDecimalFilter::copy).orElse(null);
        this.margin = other.optionalMargin().map(BigDecimalFilter::copy).orElse(null);
        this.inventoryQuantity = other.optionalInventoryQuantity().map(IntegerFilter::copy).orElse(null);
        this.inventoryLocation = other.optionalInventoryLocation().map(StringFilter::copy).orElse(null);
        this.trackQuantity = other.optionalTrackQuantity().map(BooleanFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.collectionsId = other.optionalCollectionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ProductStatusFilter getStatus() {
        return status;
    }

    public Optional<ProductStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ProductStatusFilter status() {
        if (status == null) {
            setStatus(new ProductStatusFilter());
        }
        return status;
    }

    public void setStatus(ProductStatusFilter status) {
        this.status = status;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getCompareAtPrice() {
        return compareAtPrice;
    }

    public Optional<BigDecimalFilter> optionalCompareAtPrice() {
        return Optional.ofNullable(compareAtPrice);
    }

    public BigDecimalFilter compareAtPrice() {
        if (compareAtPrice == null) {
            setCompareAtPrice(new BigDecimalFilter());
        }
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimalFilter compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public BigDecimalFilter getCostPerItem() {
        return costPerItem;
    }

    public Optional<BigDecimalFilter> optionalCostPerItem() {
        return Optional.ofNullable(costPerItem);
    }

    public BigDecimalFilter costPerItem() {
        if (costPerItem == null) {
            setCostPerItem(new BigDecimalFilter());
        }
        return costPerItem;
    }

    public void setCostPerItem(BigDecimalFilter costPerItem) {
        this.costPerItem = costPerItem;
    }

    public BigDecimalFilter getProfit() {
        return profit;
    }

    public Optional<BigDecimalFilter> optionalProfit() {
        return Optional.ofNullable(profit);
    }

    public BigDecimalFilter profit() {
        if (profit == null) {
            setProfit(new BigDecimalFilter());
        }
        return profit;
    }

    public void setProfit(BigDecimalFilter profit) {
        this.profit = profit;
    }

    public BigDecimalFilter getMargin() {
        return margin;
    }

    public Optional<BigDecimalFilter> optionalMargin() {
        return Optional.ofNullable(margin);
    }

    public BigDecimalFilter margin() {
        if (margin == null) {
            setMargin(new BigDecimalFilter());
        }
        return margin;
    }

    public void setMargin(BigDecimalFilter margin) {
        this.margin = margin;
    }

    public IntegerFilter getInventoryQuantity() {
        return inventoryQuantity;
    }

    public Optional<IntegerFilter> optionalInventoryQuantity() {
        return Optional.ofNullable(inventoryQuantity);
    }

    public IntegerFilter inventoryQuantity() {
        if (inventoryQuantity == null) {
            setInventoryQuantity(new IntegerFilter());
        }
        return inventoryQuantity;
    }

    public void setInventoryQuantity(IntegerFilter inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public StringFilter getInventoryLocation() {
        return inventoryLocation;
    }

    public Optional<StringFilter> optionalInventoryLocation() {
        return Optional.ofNullable(inventoryLocation);
    }

    public StringFilter inventoryLocation() {
        if (inventoryLocation == null) {
            setInventoryLocation(new StringFilter());
        }
        return inventoryLocation;
    }

    public void setInventoryLocation(StringFilter inventoryLocation) {
        this.inventoryLocation = inventoryLocation;
    }

    public BooleanFilter getTrackQuantity() {
        return trackQuantity;
    }

    public Optional<BooleanFilter> optionalTrackQuantity() {
        return Optional.ofNullable(trackQuantity);
    }

    public BooleanFilter trackQuantity() {
        if (trackQuantity == null) {
            setTrackQuantity(new BooleanFilter());
        }
        return trackQuantity;
    }

    public void setTrackQuantity(BooleanFilter trackQuantity) {
        this.trackQuantity = trackQuantity;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getCollectionsId() {
        return collectionsId;
    }

    public Optional<LongFilter> optionalCollectionsId() {
        return Optional.ofNullable(collectionsId);
    }

    public LongFilter collectionsId() {
        if (collectionsId == null) {
            setCollectionsId(new LongFilter());
        }
        return collectionsId;
    }

    public void setCollectionsId(LongFilter collectionsId) {
        this.collectionsId = collectionsId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(walletHolder, that.walletHolder) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(price, that.price) &&
            Objects.equals(compareAtPrice, that.compareAtPrice) &&
            Objects.equals(costPerItem, that.costPerItem) &&
            Objects.equals(profit, that.profit) &&
            Objects.equals(margin, that.margin) &&
            Objects.equals(inventoryQuantity, that.inventoryQuantity) &&
            Objects.equals(inventoryLocation, that.inventoryLocation) &&
            Objects.equals(trackQuantity, that.trackQuantity) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(collectionsId, that.collectionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            walletHolder,
            title,
            description,
            status,
            price,
            compareAtPrice,
            costPerItem,
            profit,
            margin,
            inventoryQuantity,
            inventoryLocation,
            trackQuantity,
            categoryId,
            collectionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalWalletHolder().map(f -> "walletHolder=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalCompareAtPrice().map(f -> "compareAtPrice=" + f + ", ").orElse("") +
            optionalCostPerItem().map(f -> "costPerItem=" + f + ", ").orElse("") +
            optionalProfit().map(f -> "profit=" + f + ", ").orElse("") +
            optionalMargin().map(f -> "margin=" + f + ", ").orElse("") +
            optionalInventoryQuantity().map(f -> "inventoryQuantity=" + f + ", ").orElse("") +
            optionalInventoryLocation().map(f -> "inventoryLocation=" + f + ", ").orElse("") +
            optionalTrackQuantity().map(f -> "trackQuantity=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalCollectionsId().map(f -> "collectionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
