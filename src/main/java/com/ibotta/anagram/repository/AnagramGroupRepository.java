package com.ibotta.anagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibotta.anagram.model.AnagramGroup;

public interface AnagramGroupRepository extends JpaRepository<AnagramGroup, String> {
}
