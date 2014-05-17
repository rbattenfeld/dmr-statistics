package org.statistic.logging;

import org.statistic.models.IRootModel;

public interface ICsvFormatter<R> {
	public R formatHeader(final IRootModel Model);
	public R formatLine(final IRootModel Model);
}
