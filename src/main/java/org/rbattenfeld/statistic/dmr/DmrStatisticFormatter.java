package org.rbattenfeld.statistic.dmr;

public interface DmrStatisticFormatter<C extends DmrStatisticConfigurer, T> {
	public String formatHeader(final C configurer);
	public String formatLine(final C configurer, final T source);
}
