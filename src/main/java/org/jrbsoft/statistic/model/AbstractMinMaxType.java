package org.jrbsoft.statistic.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;


public abstract class AbstractMinMaxType<T extends Comparable<T>> {
	private int _updateCount = 0;
	private T _current;
	private T _min = getMaxTypeValue();
	private T _max = getMinTypeValue();
	    
	public abstract T getMinTypeValue();
	public abstract T getMaxTypeValue();
	public abstract Double getAvg();
	public abstract boolean isType(Object obj);
	
	public void set(Object obj) {
		if (isType(obj)) {
			set((T)obj);
		}
	}
	
	public void set(T t) {
		_updateCount++;
		_current = t;
		checkMinValue(t);
		checkMaxValue(t);
	}
	
	public void setMin(T t) {
		_min = t;
	}

	public void setMax(T t) {
		_max = t;
	}

	public T getCurrent() {
		return _current;
	}
	
	public T getMin() {
		return _min;
	}
	
	public T getMax() {
		return _max;
	}
	
	public static AbstractMinMaxType<?> asMinMaxType(final Object obj) {
        if (obj instanceof ModelNode) {
            final ModelNode node = (ModelNode)obj;
            if (node.getType() == ModelType.UNDEFINED) {
            	return new StringMinMax(node.asString());
            } else if (node.getType() == ModelType.BIG_DECIMAL) {
                return new BigDecimalMinMax(node.asBigDecimal());
            } else if (node.getType() == ModelType.BIG_INTEGER) {
            	return new BigIntegerMinMax(node.asBigInteger());
            } else if (node.getType() == ModelType.INT) {
            	return new IntegerMinMax(node.asInt());
            } else if (node.getType() == ModelType.DOUBLE) {
            	return new DoubleMinMax(node.asDouble());
            } else if (node.getType() == ModelType.LONG) {
            	return new LongMinMax(node.asLong());
            } else {
            	return new StringMinMax(node.asString());
            }
        } else if (obj instanceof Integer) {
            return new IntegerMinMax((Integer)obj);
        } else if (obj instanceof Long) {
            return new LongMinMax((Long)obj);
        } else if (obj instanceof Double) {
            return new DoubleMinMax((Double)obj);
        } else if (obj instanceof BigInteger) {
            return new BigIntegerMinMax((BigInteger)obj);
        } else if (obj instanceof BigDecimal) {
            return new BigDecimalMinMax((BigDecimal)obj);
        } else if (obj instanceof String) {
            return new StringMinMax((String)obj);
        } else if (obj != null) {
        	return new StringMinMax(obj.toString());
        } else {
        	return null;
        }
    }
	
	public String toString() {
		return _current.toString();
	}
	
	//-----------------------------------------------------------------------||
	//-- Private Methods ----------------------------------------------------||
	//-----------------------------------------------------------------------||
	
	private boolean isNewMin(T t) {
		if (t != null && t.compareTo(getMin()) == -1) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isNewMax(T t) {
		if (t != null && t.compareTo(getMin()) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	private void checkMinValue(T t) {
		if (isNewMin(t)) {
			_min = t;
		}
	}
	
	private void checkMaxValue(T t) {
		if (isNewMax(t)) {
			_max = t;
		}
	}

}
