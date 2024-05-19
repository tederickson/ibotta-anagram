package com.ibotta.anagram.rest;

import com.ibotta.anagram.domain.CreateAnagramDigest;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.service.AnagramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * Exceptions are handled by the AnagramExceptionAdvice. The exception is logged
 * and converted to the appropriate HTTP Status code.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Words API")
public class WordController {
    // RequiredArgsConstructor allows Spring to use constructor dependency injection
    private final AnagramService anagramService;

    @PostMapping("/words.json")
    public void addWords(@RequestBody CreateAnagramDigest createAnagramDigest) throws AnagramException {
        log.info("/words.json {}", createAnagramDigest);

        anagramService.addWords(Arrays.asList(createAnagramDigest.getWords()));
    }

    @DeleteMapping("/words.json")
    public void deleteAllWords() {
        log.info("deleteAllWords");

        anagramService.removeAll();
    }

    @DeleteMapping("/words/{word}.json")
    public void deleteWord(@PathVariable("word") String word) {
        log.info("deleteWord({})", word);

        anagramService.remove(word);
    }
}
