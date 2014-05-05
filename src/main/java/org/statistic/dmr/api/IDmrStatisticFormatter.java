package org.statistic.dmr.api;

public interface IDmrStatisticFormatter<R> {
	public R formatHeader(final IDmrModel Model);
	public R formatLine(final IDmrModel Model);
}
