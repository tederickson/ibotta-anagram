package com.ibotta.anagram.service;

import java.util.List;

import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;

public interface AnagramService {
    void addWords(List<String> words) throws AnagramException;

    List<String> findAnagrams(String word, boolean allowProperNoun) throws AnagramException;

    void remove(String word);

    void removeAll();

    void removeAllAnagramsOf(String word) throws AnagramException;

    boolean areSameAnagram(List<String> words) throws AnagramException;

    WordMetric retrieveWordMetrics();

    List<AnagramMetric> mostAnagrams();

    List<AnagramMetric> anagramsWithAtLeast(int count);
}
