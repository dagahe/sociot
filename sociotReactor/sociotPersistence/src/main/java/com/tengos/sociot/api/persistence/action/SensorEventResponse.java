/**
 * 
 */
package com.tengos.sociot.api.persistence.action;

/**
 * @author dannygarciahernandez
 *
 */
public class SensorEventResponse {
	
	private String status;
	private String description;
	
	public SensorEventResponse(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}

	public SensorEventResponse() {
		super();
		this.status = SensorEventResponseStatus.UNKNOWN.toString();
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format(super.toString() + ":{status:%s, description:%s}", this.status, this.description);
	}
	
	

}
