package org.statistic.csvloggers;


public class FormatterUtil {

	public static void add(final StringBuffer buffer, final Object value, final char separator) {
		if (buffer.length() > 0 || value == null) {
			buffer.append(separator);
		} else if (value instanceof String && ((String)value).isEmpty()) {
			buffer.append(separator);
		}
		buffer.append(value);
	}

}
