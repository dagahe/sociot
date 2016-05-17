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
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handler in charge of drive the sensor synchronization call coming in from the
 * Sociot API
 * 
 * @author dannygarciahernandez
 *
 */
public class SensorSynchronizationLambdaHandler implements RequestHandler<String, SensorEventResponse> {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
	private ObjectMapper mapper = new ObjectMapper();
	private DynamoDB dynamoDB = new DynamoDB(Regions.EU_WEST_1);

	@Override
	/**
	 * API requests are transformed into JSON (using Aws request template) and
	 * passed to the the handler as String
	 * 
	 * @param input:
	 *            contains a valid JSON created from the queryString, parameters
	 *            and body (need a )
	 * @param output
	 * @param context
	 */
	public SensorEventResponse handleRequest(String json, Context context) {
		Map<String, JSONObject> jsonObjects;
		try {
			jsonObjects = getJSONObjects(json);
			// Get accessKey
			String accessKey = jsonObjects.get("path").getString("accessKey");
			context.getLogger().log(String.format("Request received with accessKey: %s", accessKey));
			// Get User from DynamoDB
			User user = getUserByAccessKey(accessKey);

			// Get sensorId from path: {sensorId:"8787878792"}
			String sensorId = jsonObjects.get("path").getString("sensorId");

			// body-json key contains the entity, in this case a SensorEvent
			SensorEvent input = mapper.readValue(jsonObjects.get("body-json").toString(), SensorEvent.class);
			// Log the input
			context.getLogger()
					.log(String.format("Sensor %s is sending the following data: %s", sensorId, input.toString()));
			// Start the notifications processing
			SensorEvenNotification[] notifications = input.getNotifications();
			if (notifications != null && notifications.length > 0) {
				// Get DynamoCLient and send SensorEvent
				AmazonDynamoDBClient dc = new AmazonDynamoDBClient(
						new BasicAWSCredentials("", ""))
								.withRegion(Regions.EU_WEST_1);
				// Fill the map to put into DynamoDB
				List<Map<String, AttributeValue>> se = getAsList(sensorId, notifications);
				for (Iterator<Map<String, AttributeValue>> iterator = se.iterator(); iterator.hasNext();) {
					Map<String, AttributeValue> map = (Map<String, AttributeValue>) iterator.next();
					dc.putItem(NotificationConstant.notificationTableName, map);
				}
				return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
						String.format("Request processed with %s notifications", notifications.length));
			}
			return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
					String.format("Request processed with %s notifications", 0));
		} catch (Exception e) {
			return new SensorEventResponse(SensorEventResponseStatus.ERROR.toString(),
					String.format("Request processed failed with exception: %s", e.getMessage()));
		}
	}

	private User getUserByAccessKey(String accessKey) throws Exception {
		Table table = dynamoDB.getTable(NotificationConstant.USER_TABLE_NAME);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(NotificationConstant.USER_RANGEKEY_NAME, accessKey);
		Item item = table.getItem(spec);
		if (item != null)
			return mapper.readValue(item.toJSON(), User.class);
		else
			throw new Exception(String.format("No user found for accesKey: %s", accessKey));

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

	private Map<String, JSONObject> getJSONObjects(String input) throws JSONException {
		HashMap<String, JSONObject> objects = new HashMap<String, JSONObject>();
		JSONObject object = new JSONObject(input);
		objects.put("path", object.getJSONObject("path"));
		objects.put("querystring", object.getJSONObject("querystring"));
		objects.put("header", object.getJSONObject("header"));
		objects.put("body-json", object.getJSONObject("body-json"));
		objects.put("stage-variables", object.getJSONObject("stage-variables"));
		objects.put("context", object.getJSONObject("context"));
		return objects;
	}

}
