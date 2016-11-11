package org.recap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OwningInstitutionBibId {

    @XmlValue
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

