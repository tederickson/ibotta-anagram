package com.ibotta.anagram.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ibotta.anagram.exception.AnagramException;
import org.junit.jupiter.api.Test;

class EnglishWordTest {
    @Test
    public void shouldHaveANoArgsConstructor() {
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