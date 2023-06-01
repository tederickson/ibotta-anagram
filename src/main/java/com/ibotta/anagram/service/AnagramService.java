package com.ibotta.anagram.service;

import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;

import java.util.List;

public interface AnagramService {
    void addWords(List<String> words) throws AnagramException;

    List<String> findAnagrams(String word, boolean allowProperNoun) throws AnagramException;

    void remove(String word);

    void removeAll();

    void removeAllAnagramsOf(String word);

    boolean areSameAnagram(List<String> words) throws AnagramException;

    WordMetric retrieveWordMetrics();

    List<AnagramMetric> mostAnagrams();

    List<AnagramMetric> anagramsWithAtLeast(int count);
}
