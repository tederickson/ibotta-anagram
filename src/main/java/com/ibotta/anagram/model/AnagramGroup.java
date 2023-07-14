package com.ibotta.anagram.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ANAGRAM_GRP")
public final class AnagramGroup {
    @Id
    private String anagramKey;

    private int anagramCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnagramGroup that = (AnagramGroup) o;
        return Objects.equals(anagramKey, that.anagramKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anagramKey);
    }
}
