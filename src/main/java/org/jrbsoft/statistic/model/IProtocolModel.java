package org.jrbsoft.statistic.model;

import java.util.List;


public interface IProtocolModel {
    String getName();
    void setName(final String name);
    List<AbstractElement> getStatisticElements();
}
