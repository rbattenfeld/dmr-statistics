package org.jrbsoft.statistic.junit;

import static org.junit.Assert.assertTrue;

import org.jrbsoft.statistic.model.AbstractMinMaxType;
import org.junit.Test;

public class AbstractMinMaxTypeTest {

	@Test
	public void testInteger() {
		final AbstractMinMaxType<?> minMaxType = AbstractMinMaxType.asMinMaxType(99L);
		minMaxType.set(22L);
		minMaxType.set(11L);
		minMaxType.set(22L);
		minMaxType.set(44L);
		minMaxType.set("");
		assertTrue(minMaxType.getMin().equals(11L));
		assertTrue(minMaxType.getMax().equals(44L));
	}

}
