package com.tengos.sociot.api.persistence.model.sensor;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tengos.sociot.api.persistence.action.GetNotificationsRequest;
import com.tengos.sociot.api.persistence.model.DAOFactory;
import com.tengos.sociot.api.persistence.model.DAOFactory.DAOType;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.sensor.SensorEvent.SensorEventNotification;

public class DDBSensorDAOTest {

	private SensorDAO dao;
	private SensorEvent event;

	@Before
	public void setUp() throws Exception {
		// DAO under test
		dao = DAOFactory.getSensorDAO(DAOType.DynamoDB);
		// SensorEvent instance under test
		List<SensorEventNotification> notifications = new ArrayList<SensorEventNotification>();
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "20", 0, 0));
		notifications.add(new SensorEventNotification("", "", "", "0001", DateTime.now().toString(),
				SensorEventNotificationType.TEMPERATURE.name(), "22", 0, 0));
		event = new SensorEvent("0001", notifications);

	}

	@Test
	public void testCreateSensorEvent() throws DAOException {
		int created = dao.createSensorEvent(event);
		Assert.assertEquals(2, created);
	}

	@Test
	public void testGetNotifications() throws DAOException {
		// Create GetNotificationRequest
		GetNotificationsRequest request = new GetNotificationsRequest();
		request.setSensorIdList(new String[] { "0001" });
		request.setTo(DateTime.now());
		request.setSensorTypeList(new String[] { "TEMPERATURE", "HUMIDITY" });
		SensorEventNotification[] notifications = dao.getNotifications(request);
		Assert.assertTrue(notifications.length > 0);
	}

}
