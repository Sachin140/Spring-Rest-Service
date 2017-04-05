package au.com.search.service;

import java.util.List;
import java.util.Map.Entry;

public interface SearchService {

	public String[] getSearchResults(String[] searchText);

	public List<Entry<String, Integer>> getSearchResultsInCSV(int count);
}
