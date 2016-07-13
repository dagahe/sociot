package com.tengos.sociot.api;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tengos.sociot.api.persistence.action.SensorEventResponse;
import com.tengos.sociot.api.persistence.action.SensorEventResponseStatus;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;
import com.tengos.sociot.api.persistence.model.sensor.SensorEventNotificationType;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SensorSynchronizationLambdaHandlerTest {

	private SensorEvent input;
	private SensorSynchronizationLambdaHandler handler;

	@Before
	public void setup() throws Exception {
		// Handler
		handler = new SensorSynchronizationLambdaHandler();
		// SensorEvent instance under test
		List<SensorEventNotification> notifications = new ArrayList<SensorEventNotification>();
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "20", 0, 0));
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "22", 0, 0));
		input = new SensorEvent("0001", notifications);
	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("sensorSynch");
		return ctx;
	}

	@Test
	public void testSensorSynchronization() throws JsonProcessingException {
		Context ctx = createContext();

		SensorEventResponse output = handler.handleRequest(input, ctx);
		if (output != null) {
			System.out.println(output.toString());
		}
		Assert.assertTrue(output.getStatus().equals(SensorEventResponseStatus.OK.toString()));
	}

}
