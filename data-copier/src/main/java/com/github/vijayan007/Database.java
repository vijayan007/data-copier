/**
 * 
 */
package com.github.vijayan007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Vijayan Srinivasan
 * @since 03-Feb-2017 8:05:01 pm
 * 
 */
public class Database {

	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

	private String jdbcUrl;
	private String jdbcDriverClassName;
	private String username;
	private String password;
	private Connection connection;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}

	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "Database {jdbcUrl=" + jdbcUrl + ", jdbcDriverClassName=" + jdbcDriverClassName + ", username="
				+ username + ", password=******}";
	}

	public Connection openConnection() {
		validate();
		try {
			Class.forName(jdbcDriverClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.severe(jdbcDriverClassName + " is not found in classpath");
		}
		try {
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			LOGGER.severe("unable open connection becasue of " + e.getMessage());
		}
		return connection;
	}

	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.severe(e.getMessage());
			}
		}
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.severe(e.getMessage());
			}
		}
	}

	private void validate() {
		if (jdbcDriverClassName == null) {
			throw new IllegalArgumentException("jdbcDriverClassName cannot be null");
		}
		if (jdbcUrl == null) {
			throw new IllegalArgumentException("jdbcUrl cannot be null");
		}
		if (username == null) {
			throw new IllegalArgumentException("username cannot be null");
		}
		if (password == null) {
			throw new IllegalArgumentException("password cannot be null");
		}
	}

}
