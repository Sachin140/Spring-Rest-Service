/**
 * 
 */
package au.com.search.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

/**
 * @author Sachin
 * 
 */
public class AbstractBaseController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBaseController.class);

	private static final String	AUTHENTICATION_HEADER	= "Authorization";
	private static final String	DECODED_STRING			= "d";

	/**
	 * Method to Authenticate Request
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void authenticateRequest(HttpServletRequest request, HttpServletResponse response) {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest
					.getHeader(AUTHENTICATION_HEADER);
			if (authenticate(authCredentials)) {
				LOGGER.debug("Authentication successful");
			}
			else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse
							.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					LOGGER.error("Authentication failed");
				}
			}
		}
	}

	/**
	 * Method to authenticate request
	 * 
	 * @param authCredentials
	 * @return authenticationStatus
	 */
	private boolean authenticate(String authCredentials)
	{
		if (null == authCredentials)
			return false;
		// header value format will be "Basic encodedString" for Basic authentication.
		final String encodedString = authCredentials.replaceFirst("Basic" + " ", "");
		String decodedString = null;
		try {
			byte[] decodedBytes = Base64.decode(encodedString.getBytes());
			decodedString = new String(decodedBytes, "UTF-8");
		}
		catch (IOException e) {
			LOGGER.error("IO Exception while decoding header string", e.getMessage());
		}
		boolean authenticationStatus = (DECODED_STRING.equals(decodedString)) ? true : false;
		return authenticationStatus;
	}
}
