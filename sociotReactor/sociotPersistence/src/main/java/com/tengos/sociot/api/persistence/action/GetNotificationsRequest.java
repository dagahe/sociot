/**
 * 
 */
package com.tengos.sociot.api.persistence.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dannygarciahernandez
 *
 */
public class GetNotificationsRequest {

	private String[] sensorIdList;
	private String[] sensorTypeList;
	private String from;
	private String to;

	public GetNotificationsRequest() {
		super();
	}

	@JsonCreator
	public GetNotificationsRequest(@JsonProperty(value = "sensorIdList") String[] sensorIdList,
			@JsonProperty(value = "sensorTypeList") String[] sensorTypeList, @JsonProperty(value = "from") String from,
			@JsonProperty(value = "to") String to) {
		super();
		this.sensorIdList = sensorIdList;
		this.sensorTypeList = sensorTypeList;
		this.from = from;
		this.to = to;
	}

	@JsonProperty(value = "sensorIdList")
	public String[] getSensorIdList() {
		return sensorIdList;
	}

	public void setSensorIdList(String[] sensorIdList) {
		this.sensorIdList = sensorIdList;
	}

	@JsonProperty(value = "sensorTypeList")
	public String[] getSensorTypeList() {
		return sensorTypeList;
	}

	public void setSensorTypeList(String[] sensorTypeList) {
		this.sensorTypeList = sensorTypeList;
	}

	@JsonProperty(value = "from")
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@JsonProperty(value = "to")
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
