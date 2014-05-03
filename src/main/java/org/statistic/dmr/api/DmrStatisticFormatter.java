package org.statistic.dmr.api;

public interface DmrStatisticFormatter<T, R> {
	public R formatHeader(final T source);
	public R formatLine(final T source);
}
