package com.tengos.sociot.api.persistence.model.user;

import org.junit.Before;
import org.junit.Test;

import com.tengos.sociot.api.persistence.model.DAOFactory;
import com.tengos.sociot.api.persistence.model.DAOFactory.DAOType;
import com.tengos.sociot.api.persistence.model.exception.DAOException;

public class DDBUserDAOTest {

	private UserDAO dao;
	private User user;

	@Before
	public void setUp() throws Exception {
		dao = DAOFactory.getUserDAO(DAOType.DynamoDB);
		user = new User();
		user.setUserId("xxx");
		user.setName("XXX");
		user.setEmail("xxx@xxx.com");
		user.setToken("00000");
	}

	@Test
	public void testCreateUser() throws DAOException {
		dao.createUser(user);
	}

	@Test
	public void testGetUserById() throws DAOException {
		dao.getUserById(user.getUserId());

	}

	@Test(expected = DAOException.class)
	public void testGetUserByIdException() throws DAOException {
		dao.getUserById("");
	}

	@Test
	public void testRemoveUser() throws DAOException {
		dao.removeUser(user);

	}
}
