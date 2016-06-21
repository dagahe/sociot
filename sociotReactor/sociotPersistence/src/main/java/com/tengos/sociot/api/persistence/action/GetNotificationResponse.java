package com.tengos.sociot.api.persistence.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;

public class GetNotificationResponse extends SensorEventResponse {

	private GetNotificationsRequest request;
	private SensorEventNotification[] notifications;

	public GetNotificationResponse() {
		super();
	}

	public GetNotificationResponse(String status, String description, GetNotificationsRequest request,
			SensorEventNotification[] notifications) {
		super(status, description);
		this.request = request;
		this.notifications = notifications;
	}

	@JsonProperty(value = "request")
	public GetNotificationsRequest getRequest() {
		return request;
	}

	public void setRequest(GetNotificationsRequest request) {
		this.request = request;
	}

	@JsonProperty(value = "notifications")
	public SensorEventNotification[] getNotifications() {
		return notifications;
	}

	public void setNotifications(SensorEventNotification[] notifications) {
		this.notifications = notifications;
	}

}
