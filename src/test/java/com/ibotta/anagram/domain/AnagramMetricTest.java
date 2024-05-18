package com.ibotta.anagram.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class AnagramMetricTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(AnagramMetric.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}