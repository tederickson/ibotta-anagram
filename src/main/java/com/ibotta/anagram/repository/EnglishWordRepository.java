package com.ibotta.anagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibotta.anagram.model.EnglishWord;

// JpaRepository takes care of a huge amount of boiler plate code
public interface EnglishWordRepository extends JpaRepository<EnglishWord, String> {
	List<EnglishWord> findByAnagramKey(String key);
}
