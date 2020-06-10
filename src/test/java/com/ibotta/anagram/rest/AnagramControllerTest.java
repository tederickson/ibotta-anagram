package com.ibotta.anagram.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibotta.anagram.AnagramApplication;
import com.ibotta.anagram.domain.AnagramDigest;
import com.ibotta.anagram.domain.CreateAnagramDigest;
import com.ibotta.anagram.domain.WordMetric;
import com.ibotta.anagram.exception.AnagramException;
import com.ibotta.anagram.service.AnagramService;

@RunWith(SpringRunner.class)
@WebMvcTest(AnagramController.class)
@ComponentScan(basePackageClasses = AnagramApplication.class)
public class AnagramControllerTest {
	private final String[] words = { "finite", "infinity", "dear", "dare", "read", "rare", "rear", "unicorn", "to", "thirteen", "twelve" };

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AnagramService anagramService;

	@Test
	public void testGetAnagrams() throws Exception {
		String word = "dear";
		boolean allowProperNoun = false;

		AnagramDigest digest = new AnagramDigest();
		List<String> anagrams = Arrays.asList("dare", "read");

		digest.setAnagrams(anagrams);

		when(anagramService.findAnagrams(word, allowProperNoun)).thenReturn(anagrams);
		MockHttpServletRequestBuilder builder = get("/anagrams/" + word + ".json").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		AnagramDigest actual = new ObjectMapper().readValue(json, AnagramDigest.class);

		assertEquals(digest, actual);
	}

	@Test
	public void testGetAnagramsWithLimit() throws Exception {
		String word = "dear";
		boolean allowProperNoun = false;

		AnagramDigest digest = new AnagramDigest();
		List<String> anagrams = Arrays.asList("dare", "read");

		digest.setAnagrams(anagrams);

		when(anagramService.findAnagrams(word, allowProperNoun)).thenReturn(anagrams);
		MockHttpServletRequestBuilder builder = get("/anagrams/" + word + ".json").param("limit", "1")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		AnagramDigest actual = new ObjectMapper().readValue(json, AnagramDigest.class);

		assertEquals(1, actual.getAnagrams().size());
		assertEquals(anagrams.get(0), actual.getAnagrams().get(0));
	}

	@Test
	public void testAddWords() throws Exception {
		CreateAnagramDigest createAnagramDigest = new CreateAnagramDigest();
		createAnagramDigest.setWords(words);

		String content = new ObjectMapper().writeValueAsString(createAnagramDigest);

		MockHttpServletRequestBuilder builder = post("/words.json").contentType(MediaType.APPLICATION_JSON).content(content);
		mvc.perform(builder).andExpect(status().isOk());
	}

	@Test
	public void testAddWordsThrowsException() throws Exception {
		CreateAnagramDigest createAnagramDigest = new CreateAnagramDigest();
		createAnagramDigest.setWords(words);

		String content = new ObjectMapper().writeValueAsString(createAnagramDigest);

		List<String> anagrams = Arrays.asList(words);
		Mockito.doThrow(new AnagramException("unit test worked")).when(anagramService).addWords(anagrams);

		MockHttpServletRequestBuilder builder = post("/words.json").contentType(MediaType.APPLICATION_JSON).content(content);
		mvc.perform(builder).andExpect(status().is5xxServerError());
	}

	@Test
	public void testGetAnagramMetrics() throws Exception {
		WordMetric expected = new WordMetric();
		expected.setCount(12);
		expected.setMax(7);
		expected.setMin(3);
		expected.setAverage(5);

		when(anagramService.retrieveWordMetrics()).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get("/anagrams/metrics").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		WordMetric actual = new ObjectMapper().readValue(json, WordMetric.class);

		assertEquals(expected, actual);
	}

	@Test
	public void testGetAnagramGroups() throws Exception {
		MockHttpServletRequestBuilder builder = get("/anagrams/group").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().isOk()).andReturn();
	}

	@Test
	public void testDetermineWordsSameAnagram() throws Exception {
		CreateAnagramDigest createAnagramDigest = new CreateAnagramDigest();
		createAnagramDigest.setWords(words);

		String content = new ObjectMapper().writeValueAsString(createAnagramDigest);
		MockHttpServletRequestBuilder builder = post("/anagrams").contentType(MediaType.APPLICATION_JSON).content(content);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		assertEquals("false", json);
	}

	@Test
	public void testDeleteAllWords() throws Exception {
		MockHttpServletRequestBuilder builder = delete("/words.json").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().isOk());

		// verify multiple deletes return OK status code
		mvc.perform(builder).andExpect(status().isOk());
	}

	@Test
	public void testDeleteWord() throws Exception {
		MockHttpServletRequestBuilder builder = delete("/words/parrot.json").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().isOk());

		// verify multiple deletes return OK status code
		mvc.perform(builder).andExpect(status().isOk());
	}

	@Test
	public void testDeleteAllAnagrams() throws Exception {
		MockHttpServletRequestBuilder builder = delete("/anagrams/apple").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().isOk());
	}

}
