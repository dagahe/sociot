package com.tengos.sociot.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SensorSynchronizationLambdaHandlerTest {

	private static SensorEvent input;
	private static String inputString;
	private static SensorEvenNotification[] notifications = new SensorEvenNotification[] {
			new SensorEvenNotification(new Date(System.currentTimeMillis()),
					NotificationConstant.NotificationType.TEMPERATURE.name(), "20"),
			new SensorEvenNotification(new Date(System.currentTimeMillis()),
					NotificationConstant.NotificationType.TEMPERATURE.name(), "22")};

	@BeforeClass
	public static void createInput() throws IOException {
		// SensorEvent instance under test
		input = new SensorEvent("0001", new Date(System.currentTimeMillis()), "Temperature measurement", notifications);
		// SensorEvent instance under test but as JSON string
		ObjectMapper mapper = new ObjectMapper();
		inputString = mapper.writeValueAsString(input);
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("Your Function Name");
		return ctx;
	}

	@Test
	public void testSensorSynchronization() {
		SensorSynchronizationLambdaHandler handler = new SensorSynchronizationLambdaHandler();
		Context ctx = createContext();
		SensorEventResponse output = handler.handleRequest(input, ctx);
		if (output != null) {
			System.out.println(output.toString());
		}
		Assert.assertTrue(output.getStatus().equals(SensorEventResponseStatus.OK.toString()));
	}

	@Test
	public void testSensorEventDeserialization() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		SensorEvent se = mapper.readValue(inputString, SensorEvent.class);
		Assert.assertTrue(se.getNotifications().length == 2);
	}
}
