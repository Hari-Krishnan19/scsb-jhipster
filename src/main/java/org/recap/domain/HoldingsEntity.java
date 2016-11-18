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
 * A HoldingsEntity.
 */
@Entity
@Table(name = "holdings_t")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "holdingsentity")
public class HoldingsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "holdings_id", nullable = false)
    private Integer holdingsId;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_updated_date")
    private LocalDate lastUpdatedDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @NotNull
    @Column(name = "owning_institution_id", nullable = false)
    private Integer owningInstitutionId;

    @NotNull
    @Column(name = "owning_institution_holdings_id", nullable = false)
    private String owningInstitutionHoldingsId;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private String createdBy;

    @ManyToMany(mappedBy = "holdingsEntities")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BibliographicEntity> bibliographicEntities = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "holdings_entity_item_entity",
               joinColumns = @JoinColumn(name="holdings_entities_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="item_entities_id", referencedColumnName="ID"))
    private Set<ItemEntity> itemEntities = new HashSet<>();

    @ManyToOne
    private InstitutionEntity institutionEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHoldingsId() {
        return holdingsId;
    }

    public HoldingsEntity holdingsId(Integer holdingsId) {
        this.holdingsId = holdingsId;
        return this;
    }

    public void setHoldingsId(Integer holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getContent() {
        return content;
    }

    public HoldingsEntity content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public HoldingsEntity createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public HoldingsEntity lastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public HoldingsEntity lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getOwningInstitutionId() {
        return owningInstitutionId;
    }

    public HoldingsEntity owningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
        return this;
    }

    public void setOwningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
    }

    public String getOwningInstitutionHoldingsId() {
        return owningInstitutionHoldingsId;
    }

    public HoldingsEntity owningInstitutionHoldingsId(String owningInstitutionHoldingsId) {
        this.owningInstitutionHoldingsId = owningInstitutionHoldingsId;
        return this;
    }

    public void setOwningInstitutionHoldingsId(String owningInstitutionHoldingsId) {
        this.owningInstitutionHoldingsId = owningInstitutionHoldingsId;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public HoldingsEntity isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public HoldingsEntity createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<BibliographicEntity> getBibliographicEntities() {
        return bibliographicEntities;
    }

    public HoldingsEntity bibliographicEntities(Set<BibliographicEntity> bibliographicEntities) {
        this.bibliographicEntities = bibliographicEntities;
        return this;
    }

    public HoldingsEntity addBibliographicEntity(BibliographicEntity bibliographicEntity) {
        bibliographicEntities.add(bibliographicEntity);
        bibliographicEntity.getHoldingsEntities().add(this);
        return this;
    }

    public HoldingsEntity removeBibliographicEntity(BibliographicEntity bibliographicEntity) {
        bibliographicEntities.remove(bibliographicEntity);
        bibliographicEntity.getHoldingsEntities().remove(this);
        return this;
    }

    public void setBibliographicEntities(Set<BibliographicEntity> bibliographicEntities) {
        this.bibliographicEntities = bibliographicEntities;
    }

    public Set<ItemEntity> getItemEntities() {
        return itemEntities;
    }

    public HoldingsEntity itemEntities(Set<ItemEntity> itemEntities) {
        this.itemEntities = itemEntities;
        return this;
    }

    public HoldingsEntity addItemEntity(ItemEntity itemEntity) {
        itemEntities.add(itemEntity);
        itemEntity.getHoldingsEntities().add(this);
        return this;
    }

    public HoldingsEntity removeItemEntity(ItemEntity itemEntity) {
        itemEntities.remove(itemEntity);
        itemEntity.getHoldingsEntities().remove(this);
        return this;
    }

    public void setItemEntities(Set<ItemEntity> itemEntities) {
        this.itemEntities = itemEntities;
    }

    public InstitutionEntity getInstitutionEntity() {
        return institutionEntity;
    }

    public HoldingsEntity institutionEntity(InstitutionEntity institutionEntity) {
        this.institutionEntity = institutionEntity;
        return this;
    }

    public void setInstitutionEntity(InstitutionEntity institutionEntity) {
        this.institutionEntity = institutionEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HoldingsEntity holdingsEntity = (HoldingsEntity) o;
        if(holdingsEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, holdingsEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HoldingsEntity{" +
            "id=" + id +
            ", holdingsId='" + holdingsId + "'" +
            ", content='" + content + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", owningInstitutionId='" + owningInstitutionId + "'" +
            ", owningInstitutionHoldingsId='" + owningInstitutionHoldingsId + "'" +
            ", isDeleted='" + isDeleted + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
