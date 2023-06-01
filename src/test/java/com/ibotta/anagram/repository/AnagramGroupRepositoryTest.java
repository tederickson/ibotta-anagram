package com.ibotta.anagram.repository;

import static org.junit.Assert.assertEquals;

import com.ibotta.anagram.model.EnglishWord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AnagramGroupRepositoryTest {
    @Autowired
    private EnglishWordRepository englishWordRepository;
    @Autowired
    private AnagramGroupRepository anagramGroupRepository;

    @Before
    public void setUp() throws Exception {
        if (anagramGroupRepository.count() == 0) {
            englishWordRepository.save(new EnglishWord("apple"));
            englishWordRepository.save(new EnglishWord("dear"));
            englishWordRepository.save(new EnglishWord("dare"));
            englishWordRepository.save(new EnglishWord("rare"));
            englishWordRepository.save(new EnglishWord("rear"));
        }
    }

    @Test
    public void testFindAll() {
        assertEquals(3, anagramGroupRepository.count());

        for (var group : anagramGroupRepository.findAll()) {
            log.warn(group.toString());
        }
    }

}
