package com.tengos.sociot.api;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;
import com.tengos.sociot.api.persistence.model.sensor.SensorEventNotificationType;

public class SensorEventTest {

	private SensorEvent input;
	private String inputString;

	@Before
	public void setUp() throws Exception {
		List<SensorEventNotification> notifications = new ArrayList<SensorEventNotification>();
		notifications.add(new SensorEventNotification("", "", "", "", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "20", Double.NaN, Double.NaN));
		notifications.add(new SensorEventNotification("", "", "", "", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "22", Double.NaN, Double.NaN));
		input = new SensorEvent("0001", notifications);

		inputString = "{\"sensorId\":\"0001\", \"notifications\":[{\"time\":\"2016-05-25T22:45:13.169Z\",\"type\":\"TEMPERATURE\",\"data\":\"20\",\"longitude\":\"NaN\",\"latitude\":\"NaN\"},{\"time\":\"2016-05-25T22:45:13.296Z\",\"type\":\"TEMPERATURE\",\"data\":\"22\",\"longitude\":\"NaN\",\"latitude\":\"NaN\"}]}";
	}

	@Test
	public void testSerialization() {
		String json = input.toJson();
		System.out.println(json);
	}

	@Test
	public void testDeserialization() {
		SensorEvent se = Jackson.fromJsonString(inputString, SensorEvent.class);
		Assert.assertEquals(true, se.getNotifications().size() == input.getNotifications().size());
	}

	@Test
	public void testGetJsonValue() {
		JsonNode node = Jackson.jsonNodeOf(inputString);
		Assert.assertTrue(node.findValue("notifications").size() > 0);
	}

}
