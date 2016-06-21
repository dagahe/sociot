package com.tengos.sociot.api.persistence.model.sensor;

import com.tengos.sociot.api.persistence.action.GetNotificationsRequest;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;

public interface SensorDAO {

	/**
	 * Create (insert) the SensorEvent
	 * 
	 * @param event
	 * 
	 * @return
	 * @throws DAOException
	 */
	int createSensorEvent(SensorEvent event) throws DAOException;

	SensorEventNotification[] getNotifications(GetNotificationsRequest request) throws DAOException;

}
