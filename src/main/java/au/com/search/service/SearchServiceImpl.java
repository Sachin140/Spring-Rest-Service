package au.com.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import au.com.search.model.Search;
import au.com.search.web.controller.SearchController;

@Component("searchService")
public class SearchServiceImpl implements SearchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
	public static final String REST_SERVICE_URI = "http://localhost:8080/counter-api/searchPara";
	
	@Autowired
	RestTemplate restTemplate;

	
	@Override
	public String[] getSearchResults(String[] searchText)	{
		Search search = new Search();
		Map<String, Integer> results = retrieveString(searchText);
		String[] searchResult = results.values().toArray(new String[0]);
		search.setCounts(searchResult);
		return searchResult;
	}

	@Override
	public List<Entry<String, Integer>> getSearchResultsInCSV(int count)	{ 
		return sortSearchResult(count);
	}

	/**
	 * Method to call REST service to retrieve search paragraph
	 * 
	 * @param searchText
	 * @return result map
	 */
	private Map<String, Integer> retrieveString(String[] searchText)	{
		HttpHeaders headers = generateRequestHeader("L12345");
		final HttpEntity<Object> requestEntity = new HttpEntity<Object>(searchText, headers);
		String searchPara = restTemplate.postForObject(REST_SERVICE_URI, requestEntity, String.class);
		return searchForText(searchPara, searchText);
	}

	/**
	 * Logic to search for requested text in paragraph
	 * 
	 * @param searchPara
	 * @param searchTextArray
	 * @return result map
	 */
	private Map<String, Integer> searchForText(String searchPara, String[] searchTextArray)	{
		ArrayList<String> searchArray = new ArrayList<String>();
		searchPara = searchPara.replaceAll("\\W", " "); // Strip non words
		searchPara = searchPara.replaceAll("  ", " "); // strip double space
		String[] tokens = searchPara.split(" "); // create array after splitting words
		searchArray.addAll(Arrays.asList(tokens));
		Map<String, Integer> map = new TreeMap<String, Integer>();
		for (String searchWord : searchTextArray) {
			int count = 0;
			for (String searchContent : searchArray) {
				if (searchContent.equals(searchWord)) {
					if (!map.containsKey(searchWord)) {
						map.put(searchWord, ++count);
					}
					else {
						map.put(searchWord, map.get(searchWord) + 1);
					}
				}
			}
		}
		return map;
	}

	/**
	 * Method to generate request header
	 * 
	 * @param userId
	 * @return headers
	 */
	private HttpHeaders generateRequestHeader(String userId)	{
		final String plainCreds = userId;
		final byte[] plainCredsBytes = plainCreds.getBytes();
		final byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		final String base64Creds = new String(base64CredsBytes);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic " + base64Creds);
		return headers;
	}

	private List<Entry<String, Integer>> sortSearchResult(int count)	{
		// get the paragraph from Rest API call
		String searchPara = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla sed suscipit metus..";
		ArrayList<String> searchArray = new ArrayList<String>();
		searchPara = searchPara.replaceAll("\\W", " ");
		searchPara = searchPara.replaceAll("  ", " ");
		String[] tokens = searchPara.split(" ");
		searchArray.addAll(Arrays.asList(tokens));
		final Map<String, Integer> map = new HashMap<String, Integer>();
		int counter = 0;
		for (String content : searchArray) {
			for (String word : searchArray) {
				if (content.equals(word)) {
					if (!map.containsKey(word)) {
						map.put(word, ++counter);
					}
					else {
						map.put(word, map.get(word) + 1);
					}
				}
			}
		}
		List<Entry<String, Integer>> list = sortMapForValues(map);
		List<Entry<String, Integer>> listBasedOnCount = list.subList(0, count);
		// convertListToString(listBasedOnCount);
		return listBasedOnCount;
	}

	/**
	 * @param contestWinners
	 * @return result
	 */
	@SuppressWarnings("unused")
	private static StringBuilder convertListToString(
			List<Entry<String, Integer>> listBasedOnCount)	{
		StringBuilder result = new StringBuilder();
		for (Object item : listBasedOnCount) {
			result.append(item.toString());
			result.append("|");
		}
		return result;
	}

	/**
	 * @param map
	 * @return list
	 */
	private static List<Entry<String, Integer>> sortMapForValues(
			final Map<String, Integer> map) 	{
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return list;
	}
}
