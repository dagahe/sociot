/**
 * 
 */
package com.tengos.sociot.api.persistence.model.exception;

/**
 * @author dannygarciahernandez
 *
 */
public class DAOException extends Exception {

	public DAOException(String s, Exception e) {
		super(s, e);
	}

	public DAOException(String s) {
		super(s);
	}

}
