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
@XmlAccessorType(XmlAccessType.FIELD)
public class BibRecord implements Serializable {

    @XmlElement(name="bib")
    private Bib bib;
    @XmlElement(name="holdings")
    private List<Holdings> holdings;

    public Bib getBib() {
        return bib;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
    }

    public List<Holdings> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holdings> holdings) {
        this.holdings = holdings;
    }

}
