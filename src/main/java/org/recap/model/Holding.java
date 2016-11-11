package org.recap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Holding {

    @XmlElement(name = "owningInstitutionHoldingsId")
    private String owningInstitutionHoldingsId;

    @XmlElement(name = "content")
    private Content holdingContent;

    @XmlElement(name = "items")
    private List<Items> itemsList;


    public String getOwningInstitutionHoldingsId() {
        return owningInstitutionHoldingsId;
    }

    public void setOwningInstitutionHoldingsId(String owningInstitutionHoldingsId) {
        this.owningInstitutionHoldingsId = owningInstitutionHoldingsId;
    }

    public Content getHoldingContent() {
        return holdingContent;
    }

    public void setHoldingContent(Content holdingContent) {
        this.holdingContent = holdingContent;
    }

    public List<Items> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

}
