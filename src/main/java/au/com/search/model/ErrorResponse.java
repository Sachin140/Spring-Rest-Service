package au.com.search.model;

import javax.ws.rs.core.Response.Status;

/**
 * @author Sachin Kumar
 */
public class ErrorResponse {

	/*
	 * technical description for the errors
	 */
	private String				description;
	private String				resource;
	private Status				httpStatus;

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the resource
	 */
	public String getResource()
	{
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(String resource)
	{
		this.resource = resource;
	}

	public Status getHttpStatus()
	{
		return httpStatus;
	}

	public void setHttpStatus(Status httpStatus)
	{
		this.httpStatus = httpStatus;
	}
}
