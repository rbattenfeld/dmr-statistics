package org.rbattenfeld.statistic.dmr.test.incontainer;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Stateless
public class TestBean {
	private static final Log _Logger = LogFactory.getLog(TestBean.class);
	
	public void testMe() {
		_Logger.info("testMe(");
	}
	
}
