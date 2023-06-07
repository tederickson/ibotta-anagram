package com.ibotta.anagram.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.model.EnglishWord;
import com.ibotta.anagram.repository.EnglishWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
class AnagramServiceTest {
    private static final float DELTA = 0.001f;
    private static final String[] words = {"finite", "infinity", "dear", "dare", "read", "rare", "rear",
            "unicorn", "to", "thirteen", "twelve"};

    @Value("classpath:static/dictionary.txt")
    Resource dictionary;

    @Autowired
    private EnglishWordRepository englishWordRepository;

    @Autowired
    private AnagramService anagramService;

    @BeforeEach
    public void setUp() {
        englishWordRepository.deleteAll();
    }

    @Test
    public void testReadDictionary() throws IOException {
        assertTrue(dictionary.getFile().canRead());

        List<String> wordList = new ArrayList<>();

        long start = System.currentTimeMillis();
        try (BufferedReader in = new BufferedReader(new FileReader(dictionary.getFile()))) {
            String text = in.readLine();

            while (text != null) {
                wordList.add(text);
                text = in.readLine();
            }
        }
        displayMetric("Read dictionary", start);
        System.out.println("There are " + wordList.size() + " words");

        start = System.currentTimeMillis();
        anagramService.addWords(wordList);
        displayMetric("Add words", start);

        start = System.currentTimeMillis();
        final var metric = anagramService.retrieveWordMetrics();
        displayMetric(metric.toString(), start);

        final var metrics = anagramService.mostAnagrams();
        assertFalse(metrics.isEmpty());
        metrics.forEach(System.out::println);

        final var anagrams = anagramService.findAnagrams(metrics.get(0).getWord(), true);
        System.out.println(anagrams);
    }

    private void displayMetric(String message, long start) {
        long finish = System.currentTimeMillis();
        long duration = finish - start;

        System.out.println(message + " in " + duration + " milliseconds");
    }

    @Test
    public void testAddWords() throws AnagramException {
        final var wordList = List.of(words);

        anagramService.addWords(wordList);
        assertEquals(words.length, englishWordRepository.count());

        anagramService.addWords(wordList);
        assertEquals(words.length, englishWordRepository.count());
    }

    @Test
    public void testAddInvalidWords() {
        List<String> wordList = new ArrayList<>();

        assertThrows(AnagramException.class, () -> anagramService.addWords(wordList));

        wordList.add("mxyalfj");
        assertThrows(AnagramException.class, () -> anagramService.addWords(wordList));
    }

    @Test
    public void testAddProperNoun() throws AnagramException {
        List<String> wordList = new ArrayList<>();
        wordList.add("a");
        wordList.add("A");
        anagramService.addWords(wordList);
        assertEquals(2, englishWordRepository.count());

        var anagrams = anagramService.findAnagrams("a", false);
        assertTrue(anagrams.isEmpty());

        anagrams = anagramService.findAnagrams("a", true);
        assertFalse(anagrams.isEmpty());
    }

    @Test
    public void testFindAnagrams() throws AnagramException {
        addAllWords();

        final var allowProperNoun = false;
        var anagrams = anagramService.findAnagrams("fish", allowProperNoun);
        assertTrue(anagrams.isEmpty());

        anagrams = anagramService.findAnagrams("read", allowProperNoun);
        assertEquals(2, anagrams.size());

        assertTrue(anagrams.contains("dear"));
        assertTrue(anagrams.contains("dare"));
    }

    @Test
    public void testRemove() throws AnagramException {
        assertEquals(0, englishWordRepository.count());
        anagramService.remove("fish");
        anagramService.remove("read");

        addAllWords();
        String word = "fish";
        assertFalse(englishWordRepository.findById(word).isPresent());
        anagramService.remove(word);

        word = words[3];
        assertTrue(englishWordRepository.findById(word).isPresent());
        anagramService.remove(word);
        assertFalse(englishWordRepository.findById(word).isPresent());
    }

    @Test
    public void testRemoveAll() throws AnagramException {
        addAllWords();
        assertEquals(words.length, englishWordRepository.count());

        anagramService.removeAll();
        assertEquals(0, englishWordRepository.count());
    }

    @Test
    public void testRemoveAllAnagramsOf() throws AnagramException {
        addAllWords();

        final var anagrams = List.of("dear", "dare", "read");
        for (String word : anagrams) {
            assertTrue(englishWordRepository.findById(word).isPresent());
        }

        anagramService.removeAllAnagramsOf("dear");
        for (String word : anagrams) {
            assertFalse(englishWordRepository.findById(word).isPresent());
        }
    }

    @Test
    public void testAreSameAnagram() throws AnagramException {
        assertEquals(0, englishWordRepository.count());

        List<String> anagrams = new ArrayList<>();

        anagrams.add("ape");
        anagrams.add("pea");
        assertTrue(anagramService.areSameAnagram(anagrams));

        anagrams.add("aper");
        assertFalse(anagramService.areSameAnagram(anagrams));
    }

    @Test
    public void testRetrieveWordMetrics() throws AnagramException {
        assertEquals(0, englishWordRepository.count());

        var metric = anagramService.retrieveWordMetrics();

        assertNotNull(metric);
        assertEquals(metric, new com.ibotta.anagram.domain.WordMetric());

        addAllWords();

        metric = anagramService.retrieveWordMetrics();

        assertEquals(metric.getCount(), words.length);

        List<Integer> lengths = new ArrayList<>();

        for (String word : words) {
            lengths.add(word.length());
        }
        int min = lengths.stream().reduce(Integer::min).orElseThrow();
        assertEquals(metric.getMin(), min);

        int max = lengths.stream().reduce(Integer::max).orElseThrow();
        assertEquals(metric.getMax(), max);

        float total = lengths.stream().reduce(0, Integer::sum);
        float average = total / words.length;
        assertEquals(metric.getAverage(), average, DELTA);

        Collections.sort(lengths);
        float median;
        if (lengths.size() % 2 == 1) {
            // Odd number of entries, use the middle entry
            median = lengths.get(lengths.size() / 2);
        } else {
            // Average the two middle entries
            int left = lengths.size() / 2;
            int right = left + 1;
            total = lengths.get(left) + lengths.get(right);

            median = total / 2f;
        }

        assertEquals(metric.getMedian(), median, DELTA);
    }

    @Test
    public void testMostAnagrams() throws AnagramException {
        assertTrue(anagramService.mostAnagrams().isEmpty());

        addAllWords();
        final var metrics = anagramService.mostAnagrams();
        assertEquals(1, metrics.size());

        final var metric = metrics.get(0);
        assertEquals(3, metric.getCount());

        switch (metric.getWord()) {
            case "dear":
            case "dare":
            case "read":
                break;

            default:
                fail("incorrect anagram");
        }
    }

    @Test
    public void testAnagramsWithAtLeast() throws AnagramException {
        final int numAnagrams = 2;
        assertTrue(anagramService.anagramsWithAtLeast(numAnagrams).isEmpty());

        addAllWords();
        final var metrics = anagramService.anagramsWithAtLeast(numAnagrams);

        assertEquals(2, metrics.size());
        for (AnagramMetric metric : metrics) {
            assertTrue(metric.getCount() >= numAnagrams);
        }
    }

    private void addAllWords() throws AnagramException {
        for (var word : words) {
            englishWordRepository.save(new EnglishWord(word));
        }
    }
}
