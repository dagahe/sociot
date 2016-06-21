package com.tengos.sociot.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.tengos.sociot.api.persistence.action.GetNotificationResponse;
import com.tengos.sociot.api.persistence.action.GetNotificationsRequest;
import com.tengos.sociot.api.persistence.action.SensorEventResponseStatus;

public class SensorNotificationLambdaHandlerTest {

	private SensorNotificationLambdaHandler handler;

	@Before
	public void setUp() throws Exception {
		handler = new SensorNotificationLambdaHandler();
	}

	@Test
	public void testGetNotifications() {
		Context ctx = createContext();
		GetNotificationsRequest request = new GetNotificationsRequest();
		request.setSensorIdList(new String[] { "0001" });
		request.setSensorTypeList(new String[] { "HUMIDITY" });
		GetNotificationResponse response = handler.handleRequest(request, ctx);
		System.out.println(response.getNotifications().length);
		Assert.assertTrue(response.getStatus() == SensorEventResponseStatus.OK.toString());

	}

	private Context createContext() {
		TestContext ctx = new TestContext();
		ctx.setFunctionName("sensorGetNotifications");
		return ctx;
	}

}
