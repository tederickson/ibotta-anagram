package com.ibotta.anagram.domain;

import lombok.Data;

@Data
public final class WordMetric {
    private int count;
    private int min;
    private int max;
    private float average;
    private float median;
}
