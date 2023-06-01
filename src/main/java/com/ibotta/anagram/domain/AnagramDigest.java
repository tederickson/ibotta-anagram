package com.ibotta.anagram.domain;

import lombok.Data;

import java.util.List;

@Data
public class AnagramDigest {
    private List<String> anagrams;
}
