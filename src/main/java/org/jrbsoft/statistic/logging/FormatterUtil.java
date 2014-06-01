package org.jrbsoft.statistic.logging;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;


public class FormatterUtil {

    /**
     * Adds the value of the given object to the buffer.
     * @param buffer
     * @param value
     * @param separator
     */
    public static void add(final StringBuffer buffer, final Object value, final char separator) {
        if (buffer.length() > 0 || value == null) {
            buffer.append(separator);
        } else if (value instanceof String && ((String)value).isEmpty()) {
            buffer.append(separator);
        }
        buffer.append (asString(value));
    }

    /**
     * Returns the string representation of the given object.
     * @param obj
     * @return
     */
    public static String asString(final Object obj) {
        if (obj instanceof ModelNode) {
            final ModelNode node = (ModelNode)obj;
            if (node.getType() == ModelType.UNDEFINED) {
                return "undefined";
            } else if (node.getType() == ModelType.BIG_DECIMAL) {
                return node.asBigDecimal().toString();
            } else if (node.getType() == ModelType.BIG_INTEGER) {
                return node.asBigInteger().toString();
            } else if (node.getType() == ModelType.INT) {
                return String.valueOf(node.asInt());
            } else if (node.getType() == ModelType.DOUBLE) {
                return String.valueOf(node.asDouble());
            } else if (node.getType() == ModelType.LONG) {
                return String.valueOf(node.asLong());
            } else {
                return node.asString();
            }
        } else {
            return obj.toString();
        }
    }
}
