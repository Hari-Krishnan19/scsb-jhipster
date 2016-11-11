package org.recap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ItemEntity.
 */
@Entity
@Table(name = "item_t")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itementity")
public class ItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "call_number")
    private String callNumber;

    @Column(name = "call_number_type")
    private String callNumberType;

    @NotNull
    @Column(name = "item_availability_status_id", nullable = false)
    private Integer itemAvailabilityStatusId;

    @Column(name = "copy_number")
    private Integer copyNumber;

    @NotNull
    @Column(name = "owning_institution_id", nullable = false)
    private Integer owningInstitutionId;

    @Column(name = "collection_group_id")
    private Integer collectionGroupId;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_date")
    private LocalDate lastUpdatedDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "use_restrictions")
    private String useRestrictions;

    @Column(name = "volume_part_year")
    private String volumePartYear;

    @NotNull
    @Column(name = "owning_institution_item_id", nullable = false)
    private String owningInstitutionItemId;

    @ManyToMany(mappedBy = "itemEntities")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<HoldingsEntity> holdingsEntities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public ItemEntity itemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public ItemEntity barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public ItemEntity customerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public ItemEntity callNumber(String callNumber) {
        this.callNumber = callNumber;
        return this;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallNumberType() {
        return callNumberType;
    }

    public ItemEntity callNumberType(String callNumberType) {
        this.callNumberType = callNumberType;
        return this;
    }

    public void setCallNumberType(String callNumberType) {
        this.callNumberType = callNumberType;
    }

    public Integer getItemAvailabilityStatusId() {
        return itemAvailabilityStatusId;
    }

    public ItemEntity itemAvailabilityStatusId(Integer itemAvailabilityStatusId) {
        this.itemAvailabilityStatusId = itemAvailabilityStatusId;
        return this;
    }

    public void setItemAvailabilityStatusId(Integer itemAvailabilityStatusId) {
        this.itemAvailabilityStatusId = itemAvailabilityStatusId;
    }

    public Integer getCopyNumber() {
        return copyNumber;
    }

    public ItemEntity copyNumber(Integer copyNumber) {
        this.copyNumber = copyNumber;
        return this;
    }

    public void setCopyNumber(Integer copyNumber) {
        this.copyNumber = copyNumber;
    }

    public Integer getOwningInstitutionId() {
        return owningInstitutionId;
    }

    public ItemEntity owningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
        return this;
    }

    public void setOwningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
    }

    public Integer getCollectionGroupId() {
        return collectionGroupId;
    }

    public ItemEntity collectionGroupId(Integer collectionGroupId) {
        this.collectionGroupId = collectionGroupId;
        return this;
    }

    public void setCollectionGroupId(Integer collectionGroupId) {
        this.collectionGroupId = collectionGroupId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public ItemEntity createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ItemEntity createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public ItemEntity lastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public ItemEntity lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getUseRestrictions() {
        return useRestrictions;
    }

    public ItemEntity useRestrictions(String useRestrictions) {
        this.useRestrictions = useRestrictions;
        return this;
    }

    public void setUseRestrictions(String useRestrictions) {
        this.useRestrictions = useRestrictions;
    }

    public String getVolumePartYear() {
        return volumePartYear;
    }

    public ItemEntity volumePartYear(String volumePartYear) {
        this.volumePartYear = volumePartYear;
        return this;
    }

    public void setVolumePartYear(String volumePartYear) {
        this.volumePartYear = volumePartYear;
    }

    public String getOwningInstitutionItemId() {
        return owningInstitutionItemId;
    }

    public ItemEntity owningInstitutionItemId(String owningInstitutionItemId) {
        this.owningInstitutionItemId = owningInstitutionItemId;
        return this;
    }

    public void setOwningInstitutionItemId(String owningInstitutionItemId) {
        this.owningInstitutionItemId = owningInstitutionItemId;
    }

    public Set<HoldingsEntity> getHoldingsEntities() {
        return holdingsEntities;
    }

    public ItemEntity holdingsEntities(Set<HoldingsEntity> holdingsEntities) {
        this.holdingsEntities = holdingsEntities;
        return this;
    }

    public ItemEntity addHoldingsEntity(HoldingsEntity holdingsEntity) {
        holdingsEntities.add(holdingsEntity);
        holdingsEntity.getItemEntities().add(this);
        return this;
    }

    public ItemEntity removeHoldingsEntity(HoldingsEntity holdingsEntity) {
        holdingsEntities.remove(holdingsEntity);
        holdingsEntity.getItemEntities().remove(this);
        return this;
    }

    public void setHoldingsEntities(Set<HoldingsEntity> holdingsEntities) {
        this.holdingsEntities = holdingsEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemEntity itemEntity = (ItemEntity) o;
        if(itemEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, itemEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
            "id=" + id +
            ", itemId='" + itemId + "'" +
            ", barcode='" + barcode + "'" +
            ", customerCode='" + customerCode + "'" +
            ", callNumber='" + callNumber + "'" +
            ", callNumberType='" + callNumberType + "'" +
            ", itemAvailabilityStatusId='" + itemAvailabilityStatusId + "'" +
            ", copyNumber='" + copyNumber + "'" +
            ", owningInstitutionId='" + owningInstitutionId + "'" +
            ", collectionGroupId='" + collectionGroupId + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", useRestrictions='" + useRestrictions + "'" +
            ", volumePartYear='" + volumePartYear + "'" +
            ", owningInstitutionItemId='" + owningInstitutionItemId + "'" +
            '}';
    }
}
