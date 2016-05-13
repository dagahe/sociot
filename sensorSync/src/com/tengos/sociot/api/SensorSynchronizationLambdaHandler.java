package com.tengos.sociot.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SensorSynchronizationLambdaHandler implements RequestHandler<SensorEvent, SensorEventResponse> {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");

	@Override
	public SensorEventResponse handleRequest(SensorEvent input, Context context) {
		context.getLogger().log("Input: " + input.toString());
		SensorEvenNotification[] notifications = input.getNotifications();
		if (notifications != null && notifications.length > 0) {
			// Get DynamoCLient and send SensorEvent
			AmazonDynamoDBClient dc = new AmazonDynamoDBClient(
					new BasicAWSCredentials("AKIAID272RX7TAE6QLQQ", "hEDU6DdViYzpMfCNj9LQYa9NRia/qAS3rjg8C5jc"))
							.withRegion(Regions.EU_WEST_1);
			// Fill the map to put into DynamoDB
			List<Map<String, AttributeValue>> se = getAsList("0001", notifications);
			for (Iterator<Map<String, AttributeValue>> iterator = se.iterator(); iterator.hasNext();) {
				Map<String, AttributeValue> map = (Map<String, AttributeValue>) iterator.next();
				dc.putItem(NotificationConstant.notificationTableName, map);
			}
			return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
					String.format("Request processed with %s notifications", notifications.length));
		}
		return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
				String.format("Request processed with %s notifications", 0));

	}

	private List<Map<String, AttributeValue>> getAsList(String sensorId, SensorEvenNotification[] notifications) {
		List<Map<String, AttributeValue>> l = new ArrayList<Map<String, AttributeValue>>(notifications.length);
		for (int i = 0; i < notifications.length; i++) {
			SensorEvenNotification sensorEvenNotification = notifications[i];
			Map<String, AttributeValue> map = getAsMap(sensorEvenNotification);
			map.put(NotificationConstant.notificationSensorIdCN, new AttributeValue(sensorId));
			l.add(map);			
		}
		return l;
	}

	private Map<String, AttributeValue> getAsMap(SensorEvenNotification notification) {
		HashMap<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		map.put(NotificationConstant.notificationTypeCN, new AttributeValue(notification.getType()));
		map.put(NotificationConstant.notificationTimeCN, new AttributeValue(sdf.format(notification.getTime())));
		map.put(NotificationConstant.notificationDataCN, new AttributeValue(notification.getData()));
		map.put(NotificationConstant.notificationLongitudeCN,
				new AttributeValue(String.valueOf(notification.getLongitude())));
		map.put(NotificationConstant.notificationLatitudeCN,
				new AttributeValue(String.valueOf(notification.getLatitude())));
		return map;
	}

}
