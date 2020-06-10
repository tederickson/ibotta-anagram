drop table ENGLISH_WORD if exists;
create table ENGLISH_WORD (
	word varchar(255) not null, 
	anagram_key varchar(26) not null, 
	primary key (word)
);
create index IDX_ANAGRAM_KEY on ENGLISH_WORD (anagram_key);

CREATE VIEW ANAGRAM_GRP(anagram_key, anagram_count)
AS 	SELECT anagram_key, COUNT(*) 
	FROM ENGLISH_WORD
	GROUP BY anagram_key;
