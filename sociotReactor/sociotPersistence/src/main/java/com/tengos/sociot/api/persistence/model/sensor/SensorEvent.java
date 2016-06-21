/**
 * 
 */
package com.tengos.sociot.api.persistence.model.sensor;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tengos.sociot.api.persistence.configuration.DynamoDBConfiguration;

/**
 * @author dannygarciahernandez
 *
 */
public class SensorEvent {

	private String sensorId;
	private List<SensorEventNotification> notifications;

	@JsonCreator
	public SensorEvent(@JsonProperty(value = "sensorId") String sensorId,
			@JsonProperty(value = "notifications") List<SensorEventNotification> notifications) {
		this.sensorId = sensorId;
		this.notifications = notifications;
	}

	public SensorEvent() {
	}

	/**
	 * <p>
	 * Parse the JSON string into a SensorEvent object.
	 * </p>
	 * <p>
	 * The function will try its best to parse input JSON string as best as it
	 * can. It will not fail even if the JSON string contains unknown
	 * properties. The function will throw AmazonClientException if the input
	 * JSON string is not valid JSON.
	 * </p>
	 * 
	 * @param json
	 *            JSON string to parse. Typically this is the body of your SQS
	 *            notification message body.
	 *
	 * @return The resulting SensorEvent object.
	 */
	public static SensorEvent parseJson(String json) {
		return Jackson.fromJsonString(json, SensorEvent.class);
	}

	/**
	 * @return the notifications
	 */
	@JsonProperty(value = "notifications")
	public List<SensorEventNotification> getNotifications() {
		return this.notifications;
	}

	@JsonProperty(value = "sensorId")
	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public void setNotifications(List<SensorEventNotification> notifications) {
		this.notifications = notifications;
	}

	/**
	 * @return a JSON representation of this object
	 */
	public String toJson() {
		return Jackson.toJsonString(this);
	}

	@DynamoDBTable(tableName = DynamoDBConfiguration.SENSOR_NOTIFICATION_TABLE_NAME)
	public static class SensorEventNotification {

		private String eventName;
		private String eventSource;
		private String eventVersion;
		private String sensorId;
		private String notificationDateTime;
		private String notificationType;
		private String data;
		private double longitude;
		private double latitude;

		public SensorEventNotification() {

		}

		@JsonCreator
		public SensorEventNotification(@JsonProperty(value = "eventName") String eventName,
				@JsonProperty(value = "eventSource") String eventSource,
				@JsonProperty(value = "eventVersion") String eventVersion,
				@JsonProperty(value = "sensorId") String sensorId, @JsonProperty(value = "time") String time,
				@JsonProperty(value = "type") String type, @JsonProperty(value = "data") String data,
				@JsonProperty(value = "longitude") double longitude,
				@JsonProperty(value = "latitude") double latitude) {
			// Event
			this.eventName = eventName;
			this.eventSource = eventSource;
			this.eventVersion = eventVersion;
			this.sensorId = sensorId;

			if (time != null) {
				this.notificationDateTime = time;
			} else
				this.notificationDateTime = null;

			this.notificationType = type;
			this.data = data;
			this.longitude = longitude;
			this.latitude = latitude;
		}

		@JsonProperty(value = "time")
		public String getNotificationDateTime() {
			return notificationDateTime;
		}

		@JsonProperty(value = "type")
		public String getNotificationType() {
			return notificationType;
		}

		@JsonProperty(value = "data")
		public String getData() {
			return data;
		}

		@JsonProperty(value = "longitude")
		public double getLongitude() {
			return longitude;
		}

		@JsonProperty(value = "latitude")
		public double getLatitude() {
			return latitude;
		}

		@DynamoDBIgnore
		@JsonIgnore
		public String getEventName() {
			return eventName;
		}

		@DynamoDBIgnore
		@JsonIgnore
		public String getEventSource() {
			return eventSource;
		}

		@DynamoDBIgnore
		@JsonIgnore
		public String getEventVersion() {
			return eventVersion;
		}

		@DynamoDBHashKey
		@JsonProperty(value = "sensorId")
		public String getSensorId() {
			return sensorId;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public void setEventSource(String eventSource) {
			this.eventSource = eventSource;
		}

		public void setEventVersion(String eventVersion) {
			this.eventVersion = eventVersion;
		}

		public void setSensorId(String sensorId) {
			this.sensorId = sensorId;
		}

		public void setNotificationDateTime(String notificationDateTime) {
			this.notificationDateTime = notificationDateTime;
		}

		public void setNotificationType(String notificationType) {
			this.notificationType = notificationType;
		}

		public void setData(String data) {
			this.data = data;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		/**
		 * @return a JSON representation of this object
		 */
		public String toJson() {
			return Jackson.toJsonString(this);
		}

	}

}
