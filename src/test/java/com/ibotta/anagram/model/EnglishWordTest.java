package com.ibotta.anagram.model;

import com.ibotta.anagram.exception.AnagramException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnglishWordTest {
    @Test
    public void shouldHaveNoArgsConstructor() {
        assertThat(EnglishWord.class, hasValidBeanConstructor());
    }

    @Test
    public void gettersAndSettersShouldWorkForEachProperty() {
        assertThat(EnglishWord.class, hasValidGettersAndSetters());
    }

    @Test
    public void allPropertiesShouldBeRepresentedInToStringOutput() {
        assertThat(EnglishWord.class, hasValidBeanToString());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(EnglishWord.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testInvalidWord() {
        assertThrows(AnagramException.class,
                () -> new EnglishWord("aaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void testCreateKeyWithDuplicateLetters() throws AnagramException {
        final var mississippi = "Mississippi";
        final var word = new EnglishWord(mississippi);

        assertEquals("00000000400010020040000000", word.getAnagramKey());
        assertEquals(mississippi, word.getWord());
    }
}