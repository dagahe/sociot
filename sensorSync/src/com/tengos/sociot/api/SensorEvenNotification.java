/**
 * 
 */
package com.tengos.sociot.api;

import java.util.Date;


/**
 * @author dannygarciahernandez
 *
 */
public class SensorEvenNotification {
	
	private Date time;
	private String type;
	private String data;
	private double longitude;
	private double latitude;
	
	public SensorEvenNotification() {
		super();
	}
	public SensorEvenNotification(Date time, String type, String data) {
		super();
		this.time = time;
		this.type = type;
		this.data = data;
		this.longitude = Double.NaN;
		this.latitude = Double.NaN;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	

}
