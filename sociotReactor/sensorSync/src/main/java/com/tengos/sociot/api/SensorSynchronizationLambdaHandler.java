package com.tengos.sociot.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tengos.sociot.api.persistence.action.SensorEventResponse;
import com.tengos.sociot.api.persistence.action.SensorEventResponseStatus;
import com.tengos.sociot.api.persistence.model.DAOFactory;
import com.tengos.sociot.api.persistence.model.DAOFactory.DAOType;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.sensor.SensorDAO;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent;

/**
 * Handler in charge of drive the sensor synchronization call coming in from the
 * Sociot API
 * 
 * @author dannygarciahernandez
 *
 */
public class SensorSynchronizationLambdaHandler implements RequestHandler<SensorEvent, SensorEventResponse> {

	private SensorDAO sensorDAO = DAOFactory.getSensorDAO(DAOType.DynamoDB);

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
	public SensorEventResponse handleRequest(SensorEvent se, Context context) {

		context.getLogger().log(String.format("Request received with Id: %s", context.getAwsRequestId()));

		String sensorId = se.getSensorId();
		context.getLogger().log(String.format("Request sent by sensor: %s", sensorId));

		// Log the input
		context.getLogger().log(String.format("Data sent is: %s", se.toJson()));

		// Call the save notifications process
		int saved = 0;
		if (se.getNotifications() != null && se.getNotifications().size() > 0) {
			try {
				saved = sensorDAO.createSensorEvent(se);
				context.getLogger().log("Notifications created");
				return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
						String.format("Request processed with %s notifications", saved));
			} catch (DAOException e) {
				context.getLogger().log("DAOException caught, exceptions message: " + e.getMessage());
			}
		}
		return new SensorEventResponse(SensorEventResponseStatus.OK.toString(),
				String.format("Request processed with %s notifications", 0));
	}

}
