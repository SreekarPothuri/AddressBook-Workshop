package com.blz.addressBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
	private static AddressBookDBService addressBookDBService;
	private PreparedStatement addressBookPreparedStatement;
	private List<ContactDetails> contactData;

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
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	private void prepareAddressBookStatement() throws AddressBookException {
		try {
			Connection connection = this.getConnection();
			String query = "select * from addressBookWS where FirstName = ?";
			addressBookPreparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
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
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	public int updateAddressBookData(String firstname, String address) throws AddressBookException {
		try (Connection connection = this.getConnection()) {
			String query = String.format("update addressBookWS set Address = '%s' where FirstName = '%s';", address,
					firstname);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			return preparedStatement.executeUpdate(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
		}
	}

	public List<ContactDetails> getAddressBookData(String firstname) throws AddressBookException {
		if (this.addressBookPreparedStatement == null)
			this.prepareAddressBookStatement();
		try {
			addressBookPreparedStatement.setString(1, firstname);
			ResultSet resultSet = addressBookPreparedStatement.executeQuery();
			contactData = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
		}
		System.out.println(contactData);
		return contactData;
	}

	public List<ContactDetails> readData(LocalDate start, LocalDate end) throws AddressBookException {
		String query = null;
		if (start != null)
			query = String.format("select * from addressBookWS where Date between '%s' and '%s';", start, end);
		if (start == null)
			query = "select * from addressBookWS";
		List<ContactDetails> addressBookList = new ArrayList<>();
		try (Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			addressBookList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	public int readDataBasedOnCity(String total, String city) throws AddressBookException {
		int count = 0;
		String query = String.format("select %s(State) from addressBookWS where City = '%s' group by City;", total,
				city);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			count = resultSet.getInt(1);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return count;
	}

	public ContactDetails addNewContact(String firstName, String lastName, String address, String city, String state,
			int zip, long phoneNum, String email) throws AddressBookException {
		int id = -1;
		ContactDetails addressBookData = null;
		String query = String.format("INSERT INTO addressBookWS(FirstName, LastName, Address, City, State, Zip, PhoneNumber, Email) "
						+ "values ('%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s')",
				firstName, lastName, address, city, state, zip, phoneNum, email);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowChanged = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowChanged == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					id = resultSet.getInt(1);
			}
			addressBookData = new ContactDetails(firstName, lastName, address, city, state, zip, phoneNum, email);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}
	
	public void addMultipleContactsToDBUsingThread(List<ContactDetails> record) {
		Map<Integer, Boolean> addressAdditionStatus = new HashMap<Integer, Boolean>();
		record.forEach(addressbookdata -> {
			Runnable task = () -> {
				addressAdditionStatus.put(addressbookdata.hashCode(), false);
				System.out.println("Contact Being Added:" + Thread.currentThread().getName());
				try {
					this.addNewContact(addressbookdata.getFirstName(), addressbookdata.getLastName(),
							addressbookdata.getAddress(), addressbookdata.getCity(), addressbookdata.getState(),
							addressbookdata.getZip(), addressbookdata.getPhoneNum(), addressbookdata.getEmail());
				} catch (AddressBookException e) {
					e.printStackTrace();
				}
				addressAdditionStatus.put(addressbookdata.hashCode(), true);
				System.out.println("Contact Added:" + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, addressbookdata.getFirstName());
			thread.start();
		});
		while (addressAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}
}
