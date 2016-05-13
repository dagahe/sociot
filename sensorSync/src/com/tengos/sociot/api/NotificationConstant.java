/**
 * 
 */
package com.tengos.sociot.api;

/**
 * @author dannygarciahernandez
 *
 */
public class NotificationConstant {
	
	public static String notificationTableName = "sensor_notification";
	public static String notificationSensorIdCN = "sensorId";
	public static String notificationTypeCN = "type";
	public static String notificationDataCN = "data";
	public static String notificationTimeCN = "time";
	public static String notificationLongitudeCN = "longitude";
	public static String notificationLatitudeCN = "latitude";	
	
	public enum NotificationType {
		TEMPERATURE, HUMIDITY
	}
	
	

}
