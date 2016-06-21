/**
 * 
 */
package com.tengos.sociot.api.persistence.model.user;

import com.tengos.sociot.api.persistence.model.exception.DAOException;

/**
 * @author dannygarciahernandez
 *
 */
public interface UserDAO {

	/**
	 * Get user by the unique identifier userId
	 * 
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	User getUserById(String userId) throws DAOException;

	/**
	 * Create (insert) the user in the backend
	 * 
	 * @param user
	 * @return
	 * @throws DAOException
	 */
	String createUser(User user) throws DAOException;

	/**
	 * Remove the user in the backend
	 * 
	 * @param user
	 * @return
	 * @throws DAOException
	 */
	String removeUser(User user) throws DAOException;

}
