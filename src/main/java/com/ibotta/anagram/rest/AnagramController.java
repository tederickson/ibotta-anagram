package com.ibotta.anagram.rest;

import com.ibotta.anagram.domain.AnagramDigest;
import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.domain.CreateAnagramDigest;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.service.AnagramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exceptions are handled by the AnagramExceptionAdvice. The exception is logged
 * and converted to the appropriate HTTP Status code.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class AnagramController {
    // RequiredArgsConstructor allows Spring to use constructor dependency injection
    private final AnagramService anagramService;

    @PostMapping("/words.json")
    public void addWords(@RequestBody CreateAnagramDigest createAnagramDigest) throws AnagramException {
        log.info("/words.json " + createAnagramDigest);

        anagramService.addWords(Arrays.asList(createAnagramDigest.getWords()));
    }

    @GetMapping("/anagrams/{word}.json")
    public AnagramDigest getAnagrams(@PathVariable("word") String word,
                                     @RequestParam(value = "limit", required = false) Integer limit,
                                     @RequestParam(value = "allowProperNoun", required = false) boolean allowProperNoun)
            throws AnagramException {
        log.info("/anagrams/" + word + ".json");
        log.info("limit: " + limit);
        log.info("allowProperNoun: " + allowProperNoun);

        final var digest = new AnagramDigest();
        digest.setAnagrams(anagramService.findAnagrams(word, allowProperNoun));

        if (limit != null && limit < digest.getAnagrams().size()) {
            List<String> reducedList = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                reducedList.add(digest.getAnagrams().get(i));
            }
            digest.setAnagrams(reducedList);
        }
        return digest;
    }

    @DeleteMapping("/words.json")
    public void deleteAllWords() {
        log.info("deleteAllWords");

        anagramService.removeAll();
    }

    @DeleteMapping("/words/{word}.json")
    public void deleteWord(@PathVariable("word") String word) {
        log.info("deleteWord(" + word + ")");

        anagramService.remove(word);
    }

    @DeleteMapping("/anagrams/{word}")
    public void deleteAllAnagrams(@PathVariable("word") String word) throws AnagramException {
        log.info("deleteAllAnagrams(" + word + ")");

        anagramService.removeAllAnagramsOf(word);
    }

    @GetMapping("/anagrams/metrics")
    public WordMetric getAnagramMetrics() {
        log.info("getAnagramMetrics");

        return anagramService.retrieveWordMetrics();
    }

    @GetMapping("/anagrams/group")
    public List<AnagramMetric> getAnagramGroups(@RequestParam(value = "size", required = false) Integer numAnagrams) {
        log.info("/anagrams/group/size: " + numAnagrams);

        if (numAnagrams == null) {
            return anagramService.mostAnagrams();
        }

        return anagramService.anagramsWithAtLeast(numAnagrams);
    }

    @PostMapping("/anagrams")
    public boolean determineWordsSameAnagram(@RequestBody CreateAnagramDigest createAnagramDigest)
            throws AnagramException {
        log.info("/anagrams " + createAnagramDigest);

        return anagramService.areSameAnagram(Arrays.asList(createAnagramDigest.getWords()));
    }
}
