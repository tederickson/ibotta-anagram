package com.ibotta.anagram.service;

import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.exception.InvalidWordException;
import com.ibotta.anagram.model.AnagramGroup;
import com.ibotta.anagram.model.EnglishWord;
import com.ibotta.anagram.repository.AnagramGroupRepository;
import com.ibotta.anagram.repository.EnglishWordRepository;
import com.ibotta.anagram.util.AnagramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class AnagramServiceImpl implements AnagramService {
    private final static Set<String> DICTIONARY = new HashSet<>();
    @Value("classpath:static/dictionary.txt")
    Resource dictionary;
    @Autowired
    private EnglishWordRepository englishWordRepository;
    @Autowired
    private AnagramGroupRepository anagramGroupRepository;

    @Override
    public void addWords(List<String> words) throws AnagramException {
        if (words.isEmpty()) {
            throw new AnagramException("empty list of words");
        }

        for (var word : words) {
            validateEnglishWord(word);
            if (englishWordRepository.findById(word).isEmpty()) {
                englishWordRepository.save(new EnglishWord(word));
            }
        }
    }

    @Override
    public List<String> findAnagrams(String word, boolean allowProperNoun) throws AnagramException {
        validateEnglishWord(word);
        final var key = AnagramUtil.createKey(word);
        final var anagrams = englishWordRepository.findByAnagramKey(key);
        List<String> words = new ArrayList<>();

        anagrams.forEach(entity -> {
            String dbWord = entity.getWord();
            if (!word.equals(dbWord)) {
                if (allowProperNoun) {
                    words.add(dbWord);
                } else if (dbWord.equals(dbWord.toLowerCase())) {
                    words.add(dbWord);
                }
            }
        });

        return words;
    }

    @Override
    public void remove(String word) {
        if (englishWordRepository.findById(word).isPresent()) {
            englishWordRepository.deleteById(word);
        }
    }

    @Override
    public void removeAll() {
        englishWordRepository.deleteAll();
    }

    @Override
    public void removeAllAnagramsOf(String word) throws AnagramException {
        final var key = AnagramUtil.createKey(word);
        final var anagrams = englishWordRepository.findByAnagramKey(key);

        if (!anagrams.isEmpty()) {
            englishWordRepository.deleteAllInBatch(anagrams);
        }
    }

    @Override
    public WordMetric retrieveWordMetrics() {
        final var metric = new WordMetric();
        List<Integer> lengths = new ArrayList<>();

        for (EnglishWord word : englishWordRepository.findAll()) {
            lengths.add(word.getWord().length());
        }
        if (!lengths.isEmpty()) {
            final int count = lengths.size();
            Collections.sort(lengths);

            metric.setCount(count);
            metric.setMin(lengths.getFirst());
            metric.setMax(lengths.get(count - 1));

            final float total = lengths.stream().reduce(0, Integer::sum);
            metric.setAverage(total / count);

            int middle = count / 2;

            if (count % 2 == 1) {
                metric.setMedian(lengths.get(middle));
            } else {
                // Average the two middle entries
                int left = lengths.size() / 2;
                int right = left + 1;
                float combinedMiddle = lengths.get(left) + lengths.get(right);

                metric.setMedian(combinedMiddle / 2f);
            }
        }

        return metric;
    }

    @Override
    public boolean areSameAnagram(List<String> words) throws AnagramException {
        final var anagramKey = AnagramUtil.createKey(words.getFirst());

        for (var word : words) {
            validateEnglishWord(word);
            if (!anagramKey.equals(AnagramUtil.createKey(word))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<AnagramMetric> mostAnagrams() {
        List<AnagramMetric> metrics = new ArrayList<>();
        final var groups = anagramGroupRepository.findAll();
        int max = 0;

        for (AnagramGroup group : groups) {
            if (max < group.getAnagramCount()) {
                max = group.getAnagramCount();
            }
        }

        for (AnagramGroup group : groups) {
            if (max == group.getAnagramCount()) {
                metrics.add(buildAnagramMetric(group));
            }
        }

        return metrics;
    }

    @Override
    public List<AnagramMetric> anagramsWithAtLeast(int count) {
        List<AnagramMetric> metrics = new ArrayList<>();

        for (AnagramGroup group : anagramGroupRepository.findAll()) {
            if (group.getAnagramCount() >= count) {
                metrics.add(buildAnagramMetric(group));
            }
        }

        return metrics;
    }

    private AnagramMetric buildAnagramMetric(AnagramGroup group) {
        List<EnglishWord> anagrams = englishWordRepository.findByAnagramKey(group.getAnagramKey());

        AnagramMetric metric = new AnagramMetric();
        metric.setWord(anagrams.getFirst().getWord());
        metric.setCount(group.getAnagramCount());

        return metric;
    }

    // Ensure thread-safe access to the dictionary. Prevent a thread accessing
    // the dictionary while another thread is creating the dictionary
    private synchronized void validateEnglishWord(String text) throws AnagramException {
        if (DICTIONARY.isEmpty()) {
            initializeDictionary();
        }

        if (!DICTIONARY.contains(text)) {
            throw new InvalidWordException("invalid English word [" + text + "]");
        }
    }

    private void initializeDictionary() throws AnagramException {
        try (BufferedReader in = new BufferedReader(new FileReader(dictionary.getFile()))) {
            String text = in.readLine();

            while (text != null) {
                DICTIONARY.add(text);
                text = in.readLine();
            }
        } catch (IOException e) {
            throw new AnagramException("unable to read dictionary " + dictionary.getFilename());
        }
    }
}
