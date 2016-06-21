/**
 * 
 */
package com.tengos.sociot.api.persistence.action;

import org.joda.time.DateTime;

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

	public String[] getSensorIdList() {
		return sensorIdList;
	}

	public void setSensorIdList(String[] sensorIdList) {
		this.sensorIdList = sensorIdList;
	}

	public String[] getSensorTypeList() {
		return sensorTypeList;
	}

	public void setSensorTypeList(String[] sensorTypeList) {
		this.sensorTypeList = sensorTypeList;
	}

	public DateTime getFrom() {
		return from;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public DateTime getTo() {
		return to;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

}
