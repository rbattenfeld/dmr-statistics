package org.jrbsoft.statistic.test.incontainer;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class TestSingleton {

	@EJB
	private TestBean _testBean;

	public String callMe() {
		return "Hi";
	}
	
	public String callMeAgain() {
		return "Hi again";
	}
	

	public void callTestBean(final int count) {
		for (int i = 0; i < count; i++) {
		    _testBean.testCall();
		}
	}
}
