package com.ibotta.anagram.repository;

import com.ibotta.anagram.model.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository takes care of a huge amount of boilerplate code
public interface EnglishWordRepository extends JpaRepository<EnglishWord, String> {
    List<EnglishWord> findByAnagramKey(String key);
}
