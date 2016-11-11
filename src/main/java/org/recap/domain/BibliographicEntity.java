package org.recap.domain;

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
 * A BibliographicEntity.
 */
@Entity
@Table(name = "bibliographic_t")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bibliographicentity")
public class BibliographicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "bibliographic_id", nullable = false)
    private Integer bibliographicId;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "owning_institution_id", nullable = false)
    private Integer owningInstitutionId;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_date")
    private LocalDate lastUpdatedDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @NotNull
    @Column(name = "owning_institution_bib_id", nullable = false)
    private String owningInstitutionBibId;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "bibliographic_entity_holdings_entity",
               joinColumns = @JoinColumn(name="bibliographic_entities_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="holdings_entities_id", referencedColumnName="ID"))
    private Set<HoldingsEntity> holdingsEntities = new HashSet<>();

    @ManyToOne
    private InstitutionEntity institutionEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBibliographicId() {
        return bibliographicId;
    }

    public BibliographicEntity bibliographicId(Integer bibliographicId) {
        this.bibliographicId = bibliographicId;
        return this;
    }

    public void setBibliographicId(Integer bibliographicId) {
        this.bibliographicId = bibliographicId;
    }

    public String getContent() {
        return content;
    }

    public BibliographicEntity content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOwningInstitutionId() {
        return owningInstitutionId;
    }

    public BibliographicEntity owningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
        return this;
    }

    public void setOwningInstitutionId(Integer owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public BibliographicEntity createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public BibliographicEntity createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public BibliographicEntity lastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public BibliographicEntity lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getOwningInstitutionBibId() {
        return owningInstitutionBibId;
    }

    public BibliographicEntity owningInstitutionBibId(String owningInstitutionBibId) {
        this.owningInstitutionBibId = owningInstitutionBibId;
        return this;
    }

    public void setOwningInstitutionBibId(String owningInstitutionBibId) {
        this.owningInstitutionBibId = owningInstitutionBibId;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public BibliographicEntity isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<HoldingsEntity> getHoldingsEntities() {
        return holdingsEntities;
    }

    public BibliographicEntity holdingsEntities(Set<HoldingsEntity> holdingsEntities) {
        this.holdingsEntities = holdingsEntities;
        return this;
    }

    public BibliographicEntity addHoldingsEntity(HoldingsEntity holdingsEntity) {
        holdingsEntities.add(holdingsEntity);
        holdingsEntity.getBibliographicEntities().add(this);
        return this;
    }

    public BibliographicEntity removeHoldingsEntity(HoldingsEntity holdingsEntity) {
        holdingsEntities.remove(holdingsEntity);
        holdingsEntity.getBibliographicEntities().remove(this);
        return this;
    }

    public void setHoldingsEntities(Set<HoldingsEntity> holdingsEntities) {
        this.holdingsEntities = holdingsEntities;
    }

    public InstitutionEntity getInstitutionEntity() {
        return institutionEntity;
    }

    public BibliographicEntity institutionEntity(InstitutionEntity institutionEntity) {
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
        BibliographicEntity bibliographicEntity = (BibliographicEntity) o;
        if(bibliographicEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bibliographicEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BibliographicEntity{" +
            "id=" + id +
            ", bibliographicId='" + bibliographicId + "'" +
            ", content='" + content + "'" +
            ", owningInstitutionId='" + owningInstitutionId + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            ", lastUpdatedDate='" + lastUpdatedDate + "'" +
            ", lastUpdatedBy='" + lastUpdatedBy + "'" +
            ", owningInstitutionBibId='" + owningInstitutionBibId + "'" +
            ", isDeleted='" + isDeleted + "'" +
            '}';
    }
}
