package com.ibotta.anagram.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "ANAGRAM_GRP")
public class AnagramGroup {
	@Id
	private String anagramKey;

	private int anagramCount;
}
