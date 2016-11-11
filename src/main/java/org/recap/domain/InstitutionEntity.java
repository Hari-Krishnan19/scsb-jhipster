package org.recap.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InstitutionEntity.
 */
@Entity
@Table(name = "institution_t")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "institutionentity")
public class InstitutionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "institution_code")
    private String institutionCode;

    @Column(name = "institution_name")
    private String institutionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public InstitutionEntity institutionId(Integer institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public InstitutionEntity institutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
        return this;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public InstitutionEntity institutionName(String institutionName) {
        this.institutionName = institutionName;
        return this;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstitutionEntity institutionEntity = (InstitutionEntity) o;
        if(institutionEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, institutionEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstitutionEntity{" +
            "id=" + id +
            ", institutionId='" + institutionId + "'" +
            ", institutionCode='" + institutionCode + "'" +
            ", institutionName='" + institutionName + "'" +
            '}';
    }
}
