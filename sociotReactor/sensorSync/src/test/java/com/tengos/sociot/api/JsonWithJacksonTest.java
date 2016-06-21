package com.tengos.sociot.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;
import com.tengos.sociot.api.persistence.model.sensor.SensorEventNotificationType;

public class JsonWithJacksonTest {

	private SensorEvent input;
	private String inputString;

	@Before
	public void setUp() throws Exception {
		// SensorEvent instance under test
		List<SensorEventNotification> notifications = new ArrayList<SensorEventNotification>();
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "20", Double.NaN, Double.NaN));
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "22", Double.NaN, Double.NaN));
		input = new SensorEvent("0001", notifications);

		// SensorEvent instance under test as JSON string
		ObjectMapper mapper = new ObjectMapper();
		inputString = "{\"path\": {\"sensorId\": \"0001\", \"token\":\"" + "00000"
				+ "\"}, \"querystring\":{}, \"header\":{}, \"stage-variables\":{}, \"context\":{},\"body-json\":"
				+ mapper.writeValueAsString(input) + "}";

	}

	@Test
	public void testJacksonSerialization() {
		JsonNode node = Jackson.jsonNodeOf(inputString);
		for (Iterator<Entry<String, JsonNode>> iter = node.fields(); iter.hasNext();) {
			Entry<String, JsonNode> entry = (Entry<String, JsonNode>) iter.next();
			System.out.println(entry.getKey());
		}
		System.out.println(node);
	}

	@Test
	public void testJacksonDeserialization() throws JsonParseException, JsonMappingException, IOException {
		JsonNode node = Jackson.jsonNodeOf(inputString);
		Map<String, JsonNode> nodes = new HashMap<String, JsonNode>();
		for (Iterator<Entry<String, JsonNode>> iter = node.fields(); iter.hasNext();) {
			Entry<String, JsonNode> entry = (Entry<String, JsonNode>) iter.next();
			nodes.put(entry.getKey(), entry.getValue());
		}
		Assert.assertNotNull(SensorEvent.parseJson(nodes.get("body-json").toString()));
	}

}
