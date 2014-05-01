package org.rbattenfeld.statistic.dmr.ejb3;


/**
 * This interface allows to define EJB 3 statistical values to be extracted from the specified bean.
 */
public interface IEjb3StatisticDetails {
	
	/**
	 * Returns the bean name.
	 * @return
	 */
	public String getBeanName();
	
	/**
	 * Returns the bean name abbreviation.
	 * @return
	 */
	public String getBeanNameAbbr();
	
	/**
	 * Returns the keys.
	 *  *  <p>
	 * The possible keys (class level statistics) are: 
	 * <ul>
	 * <li> component-class-name
	 * <li> declared-roles
	 * <li> execution-time
	 * <li> invocations
	 * <li> peak-concurrent-invocations
	 * <li> pool-available-count
	 * <li> pool-create-count
	 * <li> pool-current-size
	 * <li> pool-max-size
	 * <li> pool-name  
	 * <li> pool-remove-count
	 * <li> run-as-role
	 * <li> security-domain
	 * <li> timers
	 * <li> wait-time
	 * <li> service
	 * </ul>
	 * @return
	 */
	public String[] getKeys();
	
	/**
	 * Returns the methods for which the statistical values have to be extracted.
	 * <p>
	 * For each defined method, the following values are extracted: 
	 * <ul>
	 * <li> execution-time
	 * <li> invocations
	 * <li> wait-time
	 * </ul>
	 * @return
	 */
	public String[] getMethods();
	
	/**
	 * Returns the Bean type.
	 * @return
	 */
	public EjbType getEjbType();
}
