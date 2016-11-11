package org.recap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by harikrishnanv on 11/11/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {

    @XmlElement(name = "leader")
    private Leader leader;
    @XmlElement(name = "controlfield")
    private List<ControlField> controlfield;
    @XmlElement(name = "datafield")
    private List<DataField> datafield;

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public List<DataField> getDatafield() {
        return datafield;
    }

    public void setDatafield(List<DataField> datafield) {
        this.datafield = datafield;
    }

    public List<ControlField> getControlfield() {

        return controlfield;
    }

    public void setControlfield(List<ControlField> controlfield) {
        this.controlfield = controlfield;
    }
}
