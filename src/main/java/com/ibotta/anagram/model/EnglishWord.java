package com.ibotta.anagram.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.util.AnagramUtil;
import lombok.Data;

// The LomBok project annotation takes care of the getters, setters, toString, equals and hash code.
// Modify your class and the bytecode is updated.
// Caution: foreign keys will cause a recursive loop so use the IDE to generate the toString method
@Data
@Entity
@Table(name = "ENGLISH_WORD", indexes = {@Index(name = "IDX_ANAGRAM_KEY", columnList = "anagramKey")})
public class EnglishWord {
    @Id
    private String word;

    @Column(length = 26)
    private String anagramKey;

    public EnglishWord() {
    }

    public EnglishWord(String word) throws AnagramException {
        this.word = word;
        this.anagramKey = AnagramUtil.createKey(word);
    }

}
