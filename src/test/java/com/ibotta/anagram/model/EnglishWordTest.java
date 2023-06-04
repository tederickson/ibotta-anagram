package com.ibotta.anagram.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EnglishWordTest {
    @Test
    void defaultConstructor() {
        // JPA needs a default constructor
        assertNotNull(new EnglishWord());
    }
}