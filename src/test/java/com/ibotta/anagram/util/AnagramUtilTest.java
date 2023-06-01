package com.ibotta.anagram.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.exception.InvalidWordException;
import org.junit.jupiter.api.Test;

public class AnagramUtilTest {

    @Test
    public void testCreateKeyCaseIndifferent() throws AnagramException {
        final var mixedCase = AnagramUtil.createKey("Apple");
        final var lowerCase = AnagramUtil.createKey("apple");
        final var upperCase = AnagramUtil.createKey("APPLE");

        assertEquals(mixedCase, lowerCase);
        assertEquals(upperCase, lowerCase);
    }

    @Test
    public void testCreateKeyWithSpaces() throws AnagramException {
        final var spaces = AnagramUtil.createKey("  ");
        assertEquals("00000000000000000000000000", spaces);
    }

    @Test
    public void testCreateKeyWithDuplicateLetters() throws AnagramException {
        final var spaces = AnagramUtil.createKey("Mississippi");
        assertEquals("00000000400010020040000000", spaces);
    }

    @Test
    public void testCreateKeyWithNull() {
        assertThrows(InvalidWordException.class,
                () -> AnagramUtil.createKey(null));
    }

    @Test
    public void testCreateKeyWithTooManyDuplicates() {
        assertThrows(AnagramException.class,
                () -> AnagramUtil.createKey("aaaaaaaaaaaaaaaaaaaaaaaaa"));
    }
}
