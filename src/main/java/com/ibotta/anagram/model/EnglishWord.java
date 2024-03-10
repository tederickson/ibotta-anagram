package com.ibotta.anagram.model;

import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.util.AnagramUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

// The LomBok project annotation takes care of the getters, setters, toString, equals and hash code.
// Modify your class and the bytecode is updated.
// Caution: foreign keys will cause a recursive loop so use the IDE to generate the toString method
@Data
@Entity
@Table(name = "ENGLISH_WORD", indexes = {@Index(name = "IDX_ANAGRAM_KEY", columnList = "anagramKey")})
public final class EnglishWord {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnglishWord that = (EnglishWord) o;
        return Objects.equals(anagramKey, that.anagramKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anagramKey);
    }
}
