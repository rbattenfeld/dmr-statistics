package org.rbattenfeld.statistic.dmr.ejb3;

public enum EjbType {
	StatelessBean("stateless-session-bean"),
	StatefulBean("stateful-session-bean"),
	SingletonBean("singleton-bean"),
	MessageDriven("message-driven-bean"),
	EntityBean("entity-bean");
	
	String _name;
	
	private EjbType(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
}
