package org.recap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BibRecords implements Serializable {

    @XmlElement(name = "bibRecord")
    private List<BibRecord> bibRecords;

    public List<BibRecord> getBibRecords() {
        return bibRecords;
    }

    public void setBibRecords(List<BibRecord> bibRecords) {
        this.bibRecords = bibRecords;
    }
}
