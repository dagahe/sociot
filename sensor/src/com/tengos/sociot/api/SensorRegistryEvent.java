/**
 * 
 */
package com.tengos.sociot.api;

import java.util.Date;
import java.util.UUID;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * @author dannygarciahernandez
 *
 */
public class SensorRegistryEvent {
	
	private String sensorId;
	private Date registryDate;
	private String description;
	private String type;
	private String status;
	private double longitude;
	private double latitude;
	

	public SensorRegistryEvent(String sensorId, Date registryDate, String type, String description)  {
		super();
		this.sensorId = sensorId;
		this.registryDate = registryDate;
		this.type = type;
		this.description = description;
		this.status = SensorConstant.SensorStatus.UNKNOWN.name();
		this.latitude = Double.NaN;
		this.longitude = Double.NaN;
	}

	public SensorRegistryEvent() {
		super();
	}

	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public Date getRegistryDate() {
		return registryDate;
	}

	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return String.format(super.toString() + ":{sesorIdid:%s, registryDate:%s}", this.sensorId, this.registryDate);
	}
	
	
	
}
