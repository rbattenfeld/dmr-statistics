package org.statistic.models;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DmrUtilTest {
	
//
//	@Test
//	public void testUpdateStatistics() {
//		fail("Not yet implemented");
//	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgIsNull() throws Exception {
		DmrUtil.createPathAddress(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgIsEmtpy() throws Exception {
		DmrUtil.createPathAddress("");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgIsOnlySeperator() throws Exception {
		DmrUtil.createPathAddress("/");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgOnlyOneChar() throws Exception {
		DmrUtil.createPathAddress("c");
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgLessThan3Chars() throws Exception {
		DmrUtil.createPathAddress("c=");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgOnlyKey() throws Exception {
		DmrUtil.createPathAddress("core-service");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePathAddressArgOnlyKeyAndEqual() throws Exception {
		DmrUtil.createPathAddress("core-service=");
	}
	
	@Test
	public void testCreatePathAddressesArgsAreValid() throws Exception {
		assertNotNull(DmrUtil.createPathAddress("core-service=platform-mbean"));
		assertNotNull(DmrUtil.createPathAddress("core-service=platform-mbean/type=garbage-collector/name=PS_MarkSweep"));
		assertNotNull(DmrUtil.createPathAddress("/core-service=platform-mbean"));
		assertNotNull(DmrUtil.createPathAddress("/core-service=platform-mbean/type=garbage-collector/name=PS_MarkSweep"));
	}

	@Test
	public void testCreatePathAddress() {
		assertNotNull(DmrUtil.createOperation(DmrUtil.createPathAddress("core-service=platform-mbean")));
		assertNotNull(DmrUtil.createOperation(DmrUtil.createPathAddress("core-service=platform-mbean/type=garbage-collector/name=PS_MarkSweep")));
		assertNotNull(DmrUtil.createOperation(DmrUtil.createPathAddress("/core-service=platform-mbean")));
		assertNotNull(DmrUtil.createOperation(DmrUtil.createPathAddress("/core-service=platform-mbean/type=garbage-collector/name=PS_MarkSweep")));
	}

}
