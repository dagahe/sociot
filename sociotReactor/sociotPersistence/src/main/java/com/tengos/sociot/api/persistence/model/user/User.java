/**
 * 
 */
package com.tengos.sociot.api.persistence.model.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.tengos.sociot.api.persistence.configuration.DynamoDBConfiguration;

/**
 * @author dannygarciahernandez
 *
 */

@DynamoDBTable(tableName = DynamoDBConfiguration.USERS_TABLE_NAME)
public class User {

	private String userId;
	private String name;
	private String email;
	private String token;

	@DynamoDBHashKey(attributeName = "userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
