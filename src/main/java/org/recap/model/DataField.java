package org.recap.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harikrishnanv on 11/11/16.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class DataField {

    @XmlAttribute(name="ind1")
    private Integer ind1;
    @XmlAttribute(name="ind2")
    private Integer ind2;
    @XmlAttribute(name="tag")
    private Integer tag;

    @XmlElement(name="subfield")
    private List<SubField> subFieldList;

    public Integer getInd1() {
        return ind1;
    }

    public void setInd1(Integer ind1) {
        this.ind1 = ind1;
    }

    public Integer getInd2() {
        return ind2;
    }

    public void setInd2(Integer ind2) {
        this.ind2 = ind2;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public List<SubField> getSubFieldList() {
        return subFieldList;
    }

    public void setSubFieldList(List<SubField> subFieldList) {
        this.subFieldList = subFieldList;
    }
}
