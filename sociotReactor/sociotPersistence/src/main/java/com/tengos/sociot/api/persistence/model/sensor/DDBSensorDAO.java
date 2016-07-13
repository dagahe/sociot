/**
 * 
 */
package com.tengos.sociot.api.persistence.model.sensor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tengos.sociot.api.persistence.action.GetNotificationsRequest;
import com.tengos.sociot.api.persistence.configuration.DynamoDBConfiguration;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;

/**
 * @author dannygarciahernandez
 *
 */
public class DDBSensorDAO implements SensorDAO {

	private static DDBSensorDAO instance = null;

	// credentials for the client come from the environment variables
	// pre-configured by Lambda. These are tied to the
	// Lambda function execution role.
	private static AmazonDynamoDBClient ddbClient = null;

	/**
	 * Returns an initialized instance of the DDBSensorDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBSensorDAO object
	 */
	public static DDBSensorDAO getInstance() {
		if (instance == null) {
			// Provider list to be able to work with the same code in dev and
			// prod
			List<AWSCredentialsProvider> providers = Arrays.asList(new EnvironmentVariableCredentialsProvider(),
					new SystemPropertiesCredentialsProvider(), new ProfileCredentialsProvider("sociot-dev"),
					new InstanceProfileCredentialsProvider());
			ddbClient = new AmazonDynamoDBClient(new AWSCredentialsProviderChain(providers))
					.withRegion(DynamoDBConfiguration.REGION);
			instance = new DDBSensorDAO();
		}
		return instance;
	}

	protected DDBSensorDAO() {
		// prevents instantiation
	}

	@Override
	public int createSensorEvent(SensorEvent event) throws DAOException {
		// Call batchSave to get advantages from the batch processing
		List<FailedBatch> batchSave = getMapper().batchSave(event.getNotifications());
		// TODO: Due the DynamoDB provisioned throughput unprocessed items
		// should be reprocessed in a do while fashion
		return event.getNotifications().size();

	}

	/**
	 * Returns a DynamoDBMapper object initialized with the default DynamoDB
	 * client
	 *
	 * @return An initialized DynamoDBMapper
	 */
	protected DynamoDBMapper getMapper() {
		return new DynamoDBMapper(ddbClient);
	}

	@Override
	public SensorEventNotification[] getNotifications(GetNotificationsRequest request) throws DAOException {
		String to = request.getTo();
		String from = request.getFrom();

		// Check date values to filter
		if (request.getTo() == null) {
			to = DynamoDBConfiguration.DATE_TIME_FORMATTER.print(DateTime.now());
		}

		if (request.getFrom() == null) {
			from = DynamoDBConfiguration.DATE_TIME_FORMATTER.print(DateTime.now().minusDays(1)); // 24
																									// hours
																									// ago
		}

		// Sensor list to filter
		String[] sensors = request.getSensorIdList();

		// Evaluation filter
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":sensorId", new AttributeValue().withS(sensors[0]));
		eav.put(":from", new AttributeValue().withS(from));
		eav.put(":to", new AttributeValue().withS(to));

		// Expression
		DynamoDBQueryExpression<SensorEventNotification> qe = new DynamoDBQueryExpression<SensorEventNotification>();
		qe.withKeyConditionExpression("(sensorId = :sensorId and (notificationDateTime between :from and :to))");
		// Check type filter
		String[] types = request.getSensorTypeList();
		if (types != null && types.length > 0) {
			eav.put(":types", new AttributeValue().withS(types[0]));
			qe.withFilterExpression("(contains(notificationType,:types))");
		}
		qe.withExpressionAttributeValues(eav);

		PaginatedQueryList<SensorEventNotification> list = getMapper().query(SensorEventNotification.class, qe);
		return list != null ? list.toArray(new SensorEventNotification[list.size()]) : null;
	}

}
