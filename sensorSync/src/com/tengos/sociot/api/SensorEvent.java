/**
 * 
 */
package com.tengos.sociot.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author dannygarciahernandez
 *
 */
public class SensorEvent {
	
	private String id;
	private String sensorId;
	private Date time;
	private String description;
	private SensorEvenNotification[] notifications; 

	public SensorEvent(String sensorId, Date time, String description, SensorEvenNotification[] notifications) {
		this.id = UUID.randomUUID().toString();
		this.sensorId = sensorId;
		this.time = time;
		this.description = description;
		this.notifications = notifications;
	}

	public SensorEvent() {
		super();
	}

	

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	
	
	public SensorEvenNotification[] getNotifications() {
		return notifications;
	}

	public void setNotifications(SensorEvenNotification[] notifications) {
		this.notifications = notifications;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	@Override
	public String toString() {
		return String.format(super.toString() + ":{id:%s, time:%s}", this.id, this.time);
	}
	
	
	
}
