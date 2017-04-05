package au.com.search.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import au.com.search.service.SearchService;

@RestController
@RequestMapping("/service")
public class SearchController extends AbstractBaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	SearchService	searchService;
	
	/**
	 * This method will take array string and make a service call to search
	 * words in a paragraph and return the result count in a array.
	 * 
	 * @param searchText
	 * @param request
	 * @param response
	 * @return searchResult
	 */
	@RequestMapping(value = "/searchText", method = RequestMethod.GET)
	public ResponseEntity<String[]> getSearchResults(@PathVariable("searchText") String[] searchText,
			@Context HttpServletRequest request, @Context HttpServletResponse response) 	{
		// Validate the request parameters
		super.authenticateRequest(request, response);
		String[] searchResult = searchService.getSearchResults(searchText);
		return new ResponseEntity<String[]>(searchResult, HttpStatus.OK);
	}

	/**
	 * This method will take count number and make a service call to return the
	 * top counts of words in a paragraph and transform the result into csv
	 * format.
	 * 
	 * @param count
	 * @param request
	 * @param response
	 * @return csvResult
	 */
	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public ResponseEntity<ICsvBeanWriter> getSearchResultsInCSV(@PathVariable("count") int count,
			@Context HttpServletRequest request, @Context HttpServletResponse response)	{
		// Validate the request parameters
		super.authenticateRequest(request, response);
		List<Entry<String, Integer>> searchResult = searchService.getSearchResultsInCSV(count);
		response.setContentType("text/csv");
		response.setHeader("content-disposition", "attachment;filename = SearchResult.csv");
		ICsvBeanWriter csvResult = null;
		try {
			csvResult = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			for (Entry<String, Integer> result : searchResult) {
				csvResult.write(result);
			}
			csvResult.close();
		}
		catch (IOException e) {
			LOGGER.error("Exception occured while writing response to csv writer");
		}
		return new ResponseEntity<ICsvBeanWriter>(csvResult, HttpStatus.OK);
	}
}