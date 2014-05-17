package org.statistic.test.incontainer;

import javax.ejb.Singleton;

@Singleton
public class TestSingleton {

	public String callMe() {
		return "Hi";
	}
	
	public String callMeAgain() {
		return "Hi again";
	}
}
