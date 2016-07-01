/**
 * 
 */
package com.tengos.sociot.api.persistence.configuration;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.amazonaws.regions.Regions;

/**
 * @author dannygarciahernandez
 *
 */
public class DynamoDBConfiguration {

	public static final Regions REGION = Regions.EU_WEST_1;
	public static final String USERS_TABLE_NAME = "user";
	public static final String SENSOR_NOTIFICATION_TABLE_NAME = "notification";
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String TIME_ZONE = "UTC";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);

}
