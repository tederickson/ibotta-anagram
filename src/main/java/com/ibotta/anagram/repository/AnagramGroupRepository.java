package com.ibotta.anagram.repository;

import com.ibotta.anagram.model.AnagramGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnagramGroupRepository extends JpaRepository<AnagramGroup, String> {
}
