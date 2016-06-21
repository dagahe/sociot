package com.tengos.sociot.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.Jackson;
import com.tengos.sociot.api.persistence.action.GetNotificationResponse;
import com.tengos.sociot.api.persistence.action.GetNotificationsRequest;
import com.tengos.sociot.api.persistence.action.SensorEventResponseStatus;
import com.tengos.sociot.api.persistence.model.DAOFactory;
import com.tengos.sociot.api.persistence.model.DAOFactory.DAOType;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.sensor.SensorDAO;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;

public class SensorNotificationLambdaHandler
		implements RequestHandler<GetNotificationsRequest, GetNotificationResponse> {

	private SensorDAO sensorDAO = DAOFactory.getSensorDAO(DAOType.DynamoDB);

	@Override
	public GetNotificationResponse handleRequest(GetNotificationsRequest request, Context context) {

		context.getLogger().log(String.format("Request received with Id: %s", context.getAwsRequestId()));
		context.getLogger().log(String.format("Data sent is: %s", Jackson.toJsonString(request)));
		GetNotificationResponse response = new GetNotificationResponse(SensorEventResponseStatus.OK.toString(), "",
				request, null);
		try {
			SensorEventNotification[] notifications = sensorDAO.getNotifications(request);
			response.setDescription(
					String.format("Request processed returning %s notifications", notifications.length));
			response.setNotifications(notifications);
		} catch (DAOException e) {
			context.getLogger().log("DAOException caught, exceptions message is: " + e.getMessage());
			response.setStatus(SensorEventResponseStatus.ERROR.toString());
			response.setDescription("An error has been found while processing the request");
			response.setNotifications(null);
		}
		return response;
	}

}
