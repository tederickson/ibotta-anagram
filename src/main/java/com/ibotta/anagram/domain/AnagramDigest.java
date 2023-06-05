package com.ibotta.anagram.domain;

import java.util.List;

import lombok.Data;

@Data
public final class AnagramDigest {
    private List<String> anagrams;
}
