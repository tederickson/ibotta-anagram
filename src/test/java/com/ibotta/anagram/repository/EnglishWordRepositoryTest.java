package com.ibotta.anagram.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ibotta.anagram.model.EnglishWord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnglishWordRepositoryTest {
    private final static String APPLE_KEY = "10001000000100020000000000";

    @Autowired
    private EnglishWordRepository englishWordRepository;

    @Before
    public void setUp() {
        englishWordRepository.deleteAll();
    }

    @After
    public void tearDown() {
        englishWordRepository.deleteAll();
    }

    @Test
    public void testFindByKey() throws Exception {
        var anagrams = englishWordRepository.findByAnagramKey(APPLE_KEY);

        assertNotNull(anagrams);
        assertTrue(anagrams.isEmpty());

        final var word = new EnglishWord("apple");
        final var dbEntity = englishWordRepository.save(word);

        assertNotNull(dbEntity);

        anagrams = englishWordRepository.findByAnagramKey(APPLE_KEY);
        assertEquals(1, anagrams.size());
        assertEquals(dbEntity, anagrams.get(0));
    }

    @Test
    public void testFindAll() throws Exception {
        var anagrams = englishWordRepository.findAll();
        assertTrue(anagrams.isEmpty());

        final var word = new EnglishWord("apple");
        englishWordRepository.save(word);

        anagrams = englishWordRepository.findAll();
        assertEquals(1, anagrams.size());
        assertEquals(APPLE_KEY, anagrams.get(0).getAnagramKey());
    }


    /**
     * Verify all words have the same key. Verify all words are saved. Verify all
     * words are unique.
     */
    @Test
    public void testSaveAllPermutationsOfWord() throws Exception {
        final var entities = List.of(new EnglishWord("apple"),
                new EnglishWord("APPLE"),
                new EnglishWord("alppe"),
                new EnglishWord("leapp"));

        entities.forEach(it -> assertEquals(APPLE_KEY, it.getAnagramKey()));

        var rows = englishWordRepository.saveAll(entities);

        assertEquals(rows.size(), entities.size());

        rows = englishWordRepository.findByAnagramKey(APPLE_KEY);
        assertEquals(rows.size(), entities.size());
    }

    @Test
    public void testUniqueWords() throws Exception {
        for (int i = 0; i < 4; i++) {
            englishWordRepository.save(new EnglishWord("apple"));
        }

        final var anagrams = englishWordRepository.findAll();
        assertEquals(1, anagrams.size());
        assertEquals("apple", anagrams.get(0).getWord());
    }

    @Test
    public void testPhrase() throws Exception {
        englishWordRepository.save(new EnglishWord("The Rain In Spain Falls Mainly on the Plains"));
    }
}
