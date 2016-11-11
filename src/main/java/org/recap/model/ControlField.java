package org.recap.model;

import javax.xml.bind.annotation.*;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ControlField {
    @XmlAttribute(name="tag")
    private Integer tag;
    @XmlValue
    private String description;

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
