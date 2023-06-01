package com.ibotta.anagram.util;

import com.ibotta.anagram.exception.AnagramException;

public class AnagramUtil {

    private AnagramUtil() {
    }

    static public String createKey(String word) throws AnagramException {
        final var letterCount = new int[26];
        final var tokens = word.toLowerCase().toCharArray();
        final char a = 'a';

        for (char token : tokens) {
            if (Character.isLetter(token)) {
                int index = token - a;
                letterCount[index]++;
            }
        }

        final var key = new StringBuilder();
        for (int count : letterCount) {
            if (count > 9) {
                throw new AnagramException("invalid English word [" + word + "]");
            }
            key.append(count);
        }

        return key.toString();
    }
}
