package com.ibotta.anagram.repository;

import com.ibotta.anagram.model.EnglishWord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
public class AnagramGroupRepositoryTest {
    @Autowired
    private EnglishWordRepository englishWordRepository;
    @Autowired
    private AnagramGroupRepository anagramGroupRepository;
    private EnglishWord apple, dear, rare;


    @BeforeEach
    public void setUp() throws Exception {
        if (anagramGroupRepository.count() == 0) {
            apple = new EnglishWord("apple");
            dear = new EnglishWord("dear");
            rare = new EnglishWord("rare");

            englishWordRepository.save(apple);
            englishWordRepository.save(dear);
            englishWordRepository.save(new EnglishWord("dare"));
            englishWordRepository.save(new EnglishWord("read"));
            englishWordRepository.save(rare);
            englishWordRepository.save(new EnglishWord("rear"));
        }
    }

    @Test
    public void testFindAll() {
        assertEquals(3, anagramGroupRepository.count());

        for (var group : anagramGroupRepository.findAll()) {
            if (apple.getAnagramKey().equals(group.getAnagramKey())) {
                assertEquals(1, group.getAnagramCount());
            } else if (dear.getAnagramKey().equals(group.getAnagramKey())) {
                assertEquals(3, group.getAnagramCount());
            } else if (rare.getAnagramKey().equals(group.getAnagramKey())) {
                assertEquals(2, group.getAnagramCount());
                assertTrue(group.toString().contains(rare.getAnagramKey()));
            } else {
                fail(group.toString());
            }
        }
    }

}
