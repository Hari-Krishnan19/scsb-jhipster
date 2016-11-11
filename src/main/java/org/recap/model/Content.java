package org.recap.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Content implements Serializable {
    @XmlElement(required = true, nillable = true)
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
