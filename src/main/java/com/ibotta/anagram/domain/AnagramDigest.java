package com.ibotta.anagram.domain;

import lombok.Data;

import java.util.List;

@Data
public final class AnagramDigest {
    private List<String> anagrams;
}
