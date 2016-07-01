/**
 * 
 */
package com.tengos.sociot.api.persistence.action;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tengos.sociot.api.persistence.configuration.DynamoDBConfiguration;

/**
 * @author dannygarciahernandez
 *
 */
public class GetNotificationsRequest {

	private String[] sensorIdList;
	private String[] sensorTypeList;
	private DateTime from;
	private DateTime to;

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
		this.from = DynamoDBConfiguration.DATE_TIME_FORMATTER.parseDateTime(from);
		this.to = DynamoDBConfiguration.DATE_TIME_FORMATTER.parseDateTime(to);
	}

	@JsonProperty(value = "sensorIdList")
	public String[] getSensorIdList() {
		return sensorIdList;
	}

	public void setSensorIdList(String[] sensorIdList) {
		this.sensorIdList = sensorIdList;
	}

	@JsonProperty(value = "sensorIdList")
	public String[] getSensorTypeList() {
		return sensorTypeList;
	}

	public void setSensorTypeList(String[] sensorTypeList) {
		this.sensorTypeList = sensorTypeList;
	}

	@JsonProperty(value = "from")
	public DateTime getFrom() {
		return from;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	@JsonProperty(value = "to")
	public DateTime getTo() {
		return to;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

}
