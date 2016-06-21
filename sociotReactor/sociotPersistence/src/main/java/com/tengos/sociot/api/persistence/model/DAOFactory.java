/**
 * 
 */
package com.tengos.sociot.api.persistence.model;

import com.tengos.sociot.api.persistence.model.sensor.DDBSensorDAO;
import com.tengos.sociot.api.persistence.model.sensor.SensorDAO;
import com.tengos.sociot.api.persistence.model.user.DDBUserDAO;
import com.tengos.sociot.api.persistence.model.user.UserDAO;

/**
 * @author dannygarciahernandez
 *
 */
public class DAOFactory {
	/**
	 * Contains the implementations of the DAO objects. By default we only have
	 * a DynamoDB implementation
	 */
	public enum DAOType {
		DynamoDB
	}

	/**
	 * Returns the default UserDAO object
	 *
	 * @return The default implementation of the UserDAO object - by default
	 *         this is the DynamoDB implementation
	 */
	public static UserDAO getUserDAO() {
		return getUserDAO(DAOType.DynamoDB);
	}

	/**
	 * Returns a UserDAO implementation
	 *
	 * @param daoType
	 *            A value from the DAOType enum
	 * @return The corresponding UserDAO implementation
	 */
	public static UserDAO getUserDAO(DAOType daoType) {
		UserDAO dao = null;
		switch (daoType) {
		case DynamoDB:
			dao = DDBUserDAO.getInstance();
			break;
		}

		return dao;
	}

	/**
	 * Returns the default SensorDAO object
	 *
	 * @return The default implementation of the SensorDAO object - by default
	 *         this is the DynamoDB implementation
	 */
	public static SensorDAO getSensorDAO() {
		return getSensorDAO(DAOType.DynamoDB);
	}

	/**
	 * Returns a SensorDAO implementation
	 *
	 * @param daoType
	 *            A value from the DAOType enum
	 * @return The corresponding SensorDAO implementation
	 */
	public static SensorDAO getSensorDAO(DAOType daoType) {
		SensorDAO dao = null;
		switch (daoType) {
		case DynamoDB:
			dao = DDBSensorDAO.getInstance();
			break;
		}

		return dao;
	}

}
