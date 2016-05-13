/**
 * 
 */
package com.tengos.sociot.api;

/**
 * @author dannygarciahernandez
 *
 */
public class SensorRegistryEventResponse {
	
	private String status;
	private String description;
	
	public SensorRegistryEventResponse(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}

	public SensorRegistryEventResponse() {
		super();
		this.status = SensorRegistryEventResponseStatus.UNKNOWN.toString();
		this.description = null;		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
