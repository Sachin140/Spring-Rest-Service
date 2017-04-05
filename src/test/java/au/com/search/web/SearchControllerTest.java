package au.com.search.web;


public class SearchControllerTest {

	public static final String REST_SERVICE_URI = "http://localhost:8080/counter-api/searchPara";
	
	@DataProvider
	public static Object[][] setUpData) throws JsonParseException, IOException {
		
		return new Object[][] { {null,  null} };
	}
}