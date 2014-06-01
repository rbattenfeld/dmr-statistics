package org.jrbsoft.statistic.logging;

import org.jrbsoft.statistic.model.IRootModel;

public interface IFormatter<R> {
    R formatHeader(final IRootModel model);
    R formatLine(final IRootModel model);
}
