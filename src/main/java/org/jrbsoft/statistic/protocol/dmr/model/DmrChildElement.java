package org.jrbsoft.statistic.protocol.dmr.model;

import org.jrbsoft.statistic.model.AbstractElement;
import org.simpleframework.xml.Element;

public class DmrChildElement extends AbstractElement {

    @Element(name = "childs")
    private String[] _childs;

    public String[] getChilds() {
        return _childs;
    }

    public void setChilds(String[] childs) {
        _childs = childs;
    }

}
