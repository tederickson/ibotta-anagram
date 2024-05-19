package com.ibotta.anagram.rest;

import com.ibotta.anagram.domain.AnagramDigest;
import com.ibotta.anagram.domain.AnagramMetric;
import com.ibotta.anagram.domain.CreateAnagramDigest;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.service.AnagramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "Anagram API")
@RequestMapping("anagrams")
public class AnagramController {
    // RequiredArgsConstructor allows Spring to use constructor dependency injection
    private final AnagramService anagramService;

    @GetMapping("{word}.json")
    public AnagramDigest getAnagrams(@PathVariable("word") String word,
                                     @RequestParam(value = "limit", required = false) Integer limit,
                                     @RequestParam(value = "allowProperNoun", required = false) boolean allowProperNoun)
            throws AnagramException {
        log.info("/anagrams/{}.json", word);
        log.info("limit: {}", limit);
        log.info("allowProperNoun: {}", allowProperNoun);

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

    @DeleteMapping("{word}")
    public void deleteAllAnagrams(@PathVariable("word") String word) throws AnagramException {
        log.info("deleteAllAnagrams({})", word);

        anagramService.removeAllAnagramsOf(word);
    }

    @GetMapping("metrics")
    public WordMetric getAnagramMetrics() {
        log.info("getAnagramMetrics");

        return anagramService.retrieveWordMetrics();
    }

    @GetMapping("group")
    public List<AnagramMetric> getAnagramGroups(@RequestParam(value = "size", required = false) Integer numAnagrams) {
        log.info("/anagrams/group/size: {}", numAnagrams);

        if (numAnagrams == null) {
            return anagramService.mostAnagrams();
        }

        return anagramService.anagramsWithAtLeast(numAnagrams);
    }

    @PostMapping()
    public boolean determineWordsSameAnagram(@RequestBody CreateAnagramDigest createAnagramDigest)
            throws AnagramException {
        log.info("/anagrams {}", createAnagramDigest);

        return anagramService.areSameAnagram(Arrays.asList(createAnagramDigest.getWords()));
    }
}
