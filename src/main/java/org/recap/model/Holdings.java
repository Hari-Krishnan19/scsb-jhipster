package org.recap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Holdings {

    @XmlElement(name="holding")
    private List<Holding> holdingList;

    public List<Holding> getHoldingList() {
        return holdingList;
    }

    public void setHoldingList(List<Holding> holdingList) {
        this.holdingList = holdingList;
    }
}
