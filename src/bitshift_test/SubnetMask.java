package bitshift_test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubnetMask {

	

	@Test
	void test() {
		int mask = 0b1111_1111_1111_0000;
		int wrong_mask = 0b1111_1111_1111_0010;
		assertTrue(isSubnetMask(mask));
		assertFalse(isSubnetMask(wrong_mask));
	}

	private boolean isSubnetMask(int mask) {		
		return ((mask - 1 | mask) == 0xffff);
	}

	

	

}
