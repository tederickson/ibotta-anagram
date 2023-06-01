package com.ibotta.anagram.util;

public class AnagramUtil {

    private AnagramUtil() {
    }

    static public String createKey(String word) {
        int[] letterCount = new int[26];
        char[] tokens = word.toLowerCase().toCharArray();
        final char a = 'a';

        for (char token : tokens) {
            if (Character.isLetter(token)) {
                int index = token - a;
                letterCount[index]++;
            }
        }

        StringBuilder key = new StringBuilder();
        for (int count : letterCount) {
            key.append(count);
        }

        return key.toString();
    }
}
