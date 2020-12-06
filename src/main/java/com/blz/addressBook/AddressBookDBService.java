package com.blz.addressBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService addressBookDBService;

	private AddressBookDBService() {
	}
	
	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressBookWorkShop?useSSL=false";
		String username = "root";
		String password = "1234";
		Connection connection;
		System.out.println("Connecting to database:" + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, username, password);
		System.out.println("Connection is successful:" + connection);
		return connection;

	}

	public List<ContactDetails> readData() throws AddressBookException {
		String query = "SELECT * FROM addressBookWS; ";
		return this.getAddressBookDataUsingDB(query);
	}

	private List<ContactDetails> getAddressBookDataUsingDB(String sql) throws AddressBookException {
		List<ContactDetails> addressBookData = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(sql);
			addressBookData = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(),
					AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	private List<ContactDetails> getAddressBookData(ResultSet resultSet) throws AddressBookException {
		List<ContactDetails> addressBookData = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				int zip = resultSet.getInt("Zip");
				long phoneNumber = resultSet.getLong("PhoneNumber");
				String email = resultSet.getString("Email");
				addressBookData
						.add(new ContactDetails(firstName, lastName, address, city, state, zip, phoneNumber, email));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(),
					AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}
}
