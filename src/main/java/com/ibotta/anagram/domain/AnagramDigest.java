package com.ibotta.anagram.domain;

import java.util.List;

import lombok.Data;

@Data
public class AnagramDigest {
	private List<String> anagrams;
}
