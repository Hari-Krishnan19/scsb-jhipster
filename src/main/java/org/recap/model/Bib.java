package org.recap.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Bib implements Serializable {
    @XmlElement(name="owningInstitutionId")
    private OwningInstitutionId owningInstitutionId;
    @XmlElement(name="owningInstitutionBibId")
    private OwningInstitutionBibId owningInstitutionBibId;
    @XmlElement(name="content")
    private Content content;

    public OwningInstitutionId getOwningInstitutionId() {
        return owningInstitutionId;
    }

    public void setOwningInstitutionId(OwningInstitutionId owningInstitutionId) {
        this.owningInstitutionId = owningInstitutionId;
    }

    public OwningInstitutionBibId getOwningInstitutionBibId() {
        return owningInstitutionBibId;
    }

    public void setOwningInstitutionBibId(OwningInstitutionBibId owningInstitutionBibId) {
        this.owningInstitutionBibId = owningInstitutionBibId;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


}
