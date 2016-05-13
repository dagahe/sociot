/**
 * 
 */
package com.tengos.sociot.api;

/**
 * @author dannygarciahernandez
 *
 */
public class SensorConstant {
	
	public static String sensorTableName = "sensor";
	public static String sensorUserIdCN = "userId";	
	public static String sensorIdCN = "sensorId";
	public static String sensorTypeCN = "type";
	public static String sensorDescriptionCN = "description";
	public static String sensorRegistryDateCN = "registryDate";
	public static String sensorStatusCN = "status";
	public static String sensorLongitudeCN = "longitude";
	public static String sensorLatitudeCN = "latitude";	
	
	public enum SensorType {
		TEMPERATURE, HUMIDITY
	}
	
	public enum SensorStatus {
		OK, UNKNOWN, DISCONNECTED 
	}
	

}
