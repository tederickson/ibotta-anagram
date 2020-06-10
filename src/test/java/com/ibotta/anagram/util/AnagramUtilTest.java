package com.ibotta.anagram.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class AnagramUtilTest {

	@Test
	public void testCreateKeyCaseIndifferent() {
		String mixedCase = AnagramUtil.createKey("Apple");
		String lowerCase = AnagramUtil.createKey("apple");
		String upperCase = AnagramUtil.createKey("APPLE");

		assertEquals(mixedCase, lowerCase);
		assertEquals(upperCase, lowerCase);
	}

	@Test
	public void testCreateKeyWithSpaces() {
		String spaces = AnagramUtil.createKey("  ");
		assertEquals(spaces, "00000000000000000000000000");
	}

	@Test
	public void testCreateKeyWithNull() {
		try {
			AnagramUtil.createKey(null);
			fail("expected a null pointer exception");
		} catch (NullPointerException e) {
			assertTrue(e != null);
		}
	}
}
