package com.kotekka.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kotekka.app.domain.enumeration.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "wallet_holder")
    private UUID walletHolder;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Size(max = 5000)
    @Column(name = "description", length = 5000, nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Lob
    @Column(name = "media")
    private byte[] media;

    @Column(name = "media_content_type")
    private String mediaContentType;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "compare_at_price", precision = 21, scale = 2)
    private BigDecimal compareAtPrice;

    @Column(name = "cost_per_item", precision = 21, scale = 2)
    private BigDecimal costPerItem;

    @Column(name = "profit", precision = 21, scale = 2)
    private BigDecimal profit;

    @Column(name = "margin", precision = 21, scale = 2)
    private BigDecimal margin;

    @Column(name = "inventory_quantity")
    private Integer inventoryQuantity;

    @Column(name = "inventory_location")
    private String inventoryLocation;

    @Column(name = "track_quantity")
    private Boolean trackQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_product__collections",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "collections_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Collection> collections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Product uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getWalletHolder() {
        return this.walletHolder;
    }

    public Product walletHolder(UUID walletHolder) {
        this.setWalletHolder(walletHolder);
        return this;
    }

    public void setWalletHolder(UUID walletHolder) {
        this.walletHolder = walletHolder;
    }

    public String getTitle() {
        return this.title;
    }

    public Product title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public byte[] getMedia() {
        return this.media;
    }

    public Product media(byte[] media) {
        this.setMedia(media);
        return this;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }

    public String getMediaContentType() {
        return this.mediaContentType;
    }

    public Product mediaContentType(String mediaContentType) {
        this.mediaContentType = mediaContentType;
        return this;
    }

    public void setMediaContentType(String mediaContentType) {
        this.mediaContentType = mediaContentType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCompareAtPrice() {
        return this.compareAtPrice;
    }

    public Product compareAtPrice(BigDecimal compareAtPrice) {
        this.setCompareAtPrice(compareAtPrice);
        return this;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public BigDecimal getCostPerItem() {
        return this.costPerItem;
    }

    public Product costPerItem(BigDecimal costPerItem) {
        this.setCostPerItem(costPerItem);
        return this;
    }

    public void setCostPerItem(BigDecimal costPerItem) {
        this.costPerItem = costPerItem;
    }

    public BigDecimal getProfit() {
        return this.profit;
    }

    public Product profit(BigDecimal profit) {
        this.setProfit(profit);
        return this;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getMargin() {
        return this.margin;
    }

    public Product margin(BigDecimal margin) {
        this.setMargin(margin);
        return this;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public Integer getInventoryQuantity() {
        return this.inventoryQuantity;
    }

    public Product inventoryQuantity(Integer inventoryQuantity) {
        this.setInventoryQuantity(inventoryQuantity);
        return this;
    }

    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public String getInventoryLocation() {
        return this.inventoryLocation;
    }

    public Product inventoryLocation(String inventoryLocation) {
        this.setInventoryLocation(inventoryLocation);
        return this;
    }

    public void setInventoryLocation(String inventoryLocation) {
        this.inventoryLocation = inventoryLocation;
    }

    public Boolean getTrackQuantity() {
        return this.trackQuantity;
    }

    public Product trackQuantity(Boolean trackQuantity) {
        this.setTrackQuantity(trackQuantity);
        return this;
    }

    public void setTrackQuantity(Boolean trackQuantity) {
        this.trackQuantity = trackQuantity;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Collection> getCollections() {
        return this.collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }

    public Product collections(Set<Collection> collections) {
        this.setCollections(collections);
        return this;
    }

    public Product addCollections(Collection collection) {
        this.collections.add(collection);
        return this;
    }

    public Product removeCollections(Collection collection) {
        this.collections.remove(collection);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", walletHolder='" + getWalletHolder() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", media='" + getMedia() + "'" +
            ", mediaContentType='" + getMediaContentType() + "'" +
            ", price=" + getPrice() +
            ", compareAtPrice=" + getCompareAtPrice() +
            ", costPerItem=" + getCostPerItem() +
            ", profit=" + getProfit() +
            ", margin=" + getMargin() +
            ", inventoryQuantity=" + getInventoryQuantity() +
            ", inventoryLocation='" + getInventoryLocation() + "'" +
            ", trackQuantity='" + getTrackQuantity() + "'" +
            "}";
    }
}
