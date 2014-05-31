package org.jrbsoft.statistic.model;

import java.io.IOException;


public interface IModelUpdater {
    void updateModel(final IRootModel model) throws IOException;
}
