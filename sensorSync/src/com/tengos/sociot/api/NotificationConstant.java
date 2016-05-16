/**
 * 
 */
package com.tengos.sociot.api;

/**
 * @author dannygarciahernandez
 *
 */
public class NotificationConstant {

	public static final String USER_TABLE_NAME = "user";
	public static final String USER_HASHKEY_NAME = "userId";
	public static final String USER_RANGEKEY_NAME = "accessKey";
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
