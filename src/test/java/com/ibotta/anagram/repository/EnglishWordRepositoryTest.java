package com.ibotta.anagram.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibotta.anagram.model.EnglishWord;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnglishWordRepositoryTest {
	private final static String APPLE_KEY = "10001000000100020000000000";

	@Autowired
	private EnglishWordRepository englishWordRepository;

	@Before
	public void setUp() throws Exception {
		englishWordRepository.deleteAll();
	}

	@After
	public void tearDown() throws Exception {
		englishWordRepository.deleteAll();
	}

	@Test
	public void testFindByKey() {
		List<EnglishWord> anagrams = englishWordRepository.findByAnagramKey(APPLE_KEY);

		assertNotNull(anagrams);
		assertTrue(anagrams.isEmpty());

		EnglishWord word = new EnglishWord("apple");
		EnglishWord dbEntity = englishWordRepository.save(word);

		assertNotNull(dbEntity);

		anagrams = englishWordRepository.findByAnagramKey(APPLE_KEY);
		assertTrue(anagrams.size() == 1);
		assertEquals(dbEntity, anagrams.get(0));
	}

	@Test
	public void testFindAll() {
		List<EnglishWord> anagrams = englishWordRepository.findAll();
		assertTrue(anagrams.isEmpty());

		EnglishWord word = new EnglishWord("apple");
		englishWordRepository.save(word);

		anagrams = englishWordRepository.findAll();
		assertEquals(1, anagrams.size());
		assertEquals(APPLE_KEY, anagrams.get(0).getAnagramKey());
	}

	@Test
	/**
	 * Verify all words have the same key. Verify all words are saved. Verify all
	 * words are unique.
	 */
	public void testSaveAllIterableOfS() {
		List<EnglishWord> entities = new ArrayList<>();

		entities.add(new EnglishWord("apple"));
		entities.add(new EnglishWord("APPLE"));
		entities.add(new EnglishWord("alppe"));
		entities.add(new EnglishWord("leapp"));

		entities.forEach(it -> {
			assertEquals(APPLE_KEY, it.getAnagramKey());
		});

		List<EnglishWord> rows = englishWordRepository.saveAll(entities);

		assertEquals(rows.size(), entities.size());

		rows = englishWordRepository.findByAnagramKey(APPLE_KEY);
		assertEquals(rows.size(), entities.size());
	}

	@Test
	public void testUniqueWords() {
		for (int i = 0; i < 4; i++) {
			englishWordRepository.save(new EnglishWord("apple"));
		}

		List<EnglishWord> anagrams = englishWordRepository.findAll();
		assertEquals(1, anagrams.size());
	}

	@Test
	public void testPhrase() {
		englishWordRepository.save(new EnglishWord("The Rain In Spain Falls Mainly on the Plains"));
	}
}
