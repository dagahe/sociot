/**
 * 
 */
package com.tengos.sociot.api.persistence.model.user;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.tengos.sociot.api.persistence.model.exception.DAOException;

/**
 * Singleton implementation from
 * 
 * @author dannygarciahernandez
 *
 */
public class DDBUserDAO implements UserDAO {

	private static DDBUserDAO instance = null;

	// credentials for the client come from the environment variables
	// pre-configured by Lambda. These are tied to the
	// Lambda function execution role.
	private static AmazonDynamoDBClient ddbClient = null;

	/**
	 * Returns an initialized instance of the DDBUserDAO object. DAO objects
	 * should be retrieved through the DAOFactory class
	 *
	 * @return An initialized instance of the DDBUserDAO object
	 */
	public static DDBUserDAO getInstance() {
		if (instance == null) {
			List<AWSCredentialsProvider> providers = Arrays.asList(new EnvironmentVariableCredentialsProvider(),
					new SystemPropertiesCredentialsProvider(), new ProfileCredentialsProvider("sociot-dev"),
					new InstanceProfileCredentialsProvider());
			ddbClient = new AmazonDynamoDBClient(new AWSCredentialsProviderChain(providers))
					.withRegion(Regions.EU_WEST_1);
			instance = new DDBUserDAO();
		}

		return instance;
	}

	protected DDBUserDAO() {
		// prevents instantiation
	}

	/**
	 * Inserts a new row in the DynamoDB users table.
	 *
	 * @param user
	 *            The new user information
	 * @return The username that was just inserted in DynamoDB
	 * @throws DAOException
	 */
	public String createUser(User user) throws DAOException {
		if (user.getUserId() == null || user.getUserId().trim().equals("")) {
			throw new DAOException("Cannot create user with empty username");
		}

		if (getUserById(user.getUserId()) != null) {
			throw new DAOException("Username must be unique");
		}

		getMapper().save(user);
		return user.getUserId();
	}

	@Override
	public String removeUser(User user) throws DAOException {
		if (user.getUserId() == null || user.getUserId().trim().equals("")) {
			throw new DAOException("Cannot delete user with empty username");
		}

		if (getUserById(user.getUserId()) == null) {
			throw new DAOException("Username doesn't exist");
		}
		getMapper().delete(user);
		return user.getUserId();
	}

	public User getUserById(String userId) throws DAOException {
		if (userId == null || userId.trim().equals("")) {
			throw new DAOException("Cannot lookup null or empty user");
		}
		return getMapper().load(User.class, userId);
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

}
