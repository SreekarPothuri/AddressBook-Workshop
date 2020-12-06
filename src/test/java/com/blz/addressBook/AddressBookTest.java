package com.blz.addressBook;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.addressBook.AddressBookService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookTest {

	private static final IOService REST_IO = null;
	private static AddressBookService addressBook;
	
	@AfterClass
	public static void nullObj() {
		addressBook = null;
	}
	
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	@BeforeClass
	public static void createAddressBookObj() {
		addressBook = new AddressBookService();
		System.out.println("Welcome to the Address Book System.. ");
	}
	
	private ContactDetails[] getAddressbookList() {
		Response response = RestAssured.get("/addressBookWS");
		System.out.println("Adddressbook entries in JsonServer :\n" + response.asString());
		ContactDetails[] arrayOfPerson = new Gson().fromJson(response.asString(), ContactDetails[].class);
		return arrayOfPerson;
	}
	
	private Response addContactToJsonServer(ContactDetails addressbookData) {
		String contactJson = new Gson().toJson(addressbookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		return request.post("/addressBookWS");
	}

	@Test
	public void givenAddressBook_WhenRetrieved_ShouldMatchCount() throws AddressBookException, SQLException {
		List<ContactDetails> data = addressBook.readAddressBookData(IOService.DB_IO);
		Assert.assertEquals(2, data.size());
	}

	@Test
	public void givenAddressBook_WhenUpdate_ShouldSyncWithDB() throws AddressBookException {
		List<ContactDetails> contactDetails = addressBook.readAddressBookData(IOService.DB_IO);
		addressBook.updateRecord("Anudeep", "Gandhi Nagar");
		boolean result = addressBook.checkUpdatedRecordSyncWithDB("Anudeep");
		Assert.assertFalse(result);
	}

	@Test
	public void givenAddressBook_WhenRetrieved_ShouldMatchCountInGivenRange() throws AddressBookException {
		List<ContactDetails> addressBookData = addressBook.readAddressBookData(IOService.DB_IO, "2018-02-14",
				"2020-06-02");
		Assert.assertEquals(2, addressBookData.size());
	}

	@Test
	public void givenAddresBook_WhenRetrieved_ShouldReturnCountOfCity() throws AddressBookException {
		Assert.assertEquals(1, addressBook.readAddressBookData("count", "Ponnur"));
	}
	
	@Test
	public void givenAddresBookDetails_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		addressBook.readAddressBookData(IOService.DB_IO);
		addressBook.addNewContact("Sreekar", "Pothuri", "Ring Road", "Ponnur", "Andhra Pradesh", 522124, 879061343,
				"pothuri98@gmail.com");
		boolean result = addressBook.checkUpdatedRecordSyncWithDB("Sreekar");
		Assert.assertFalse(result);
	}
	
	@Test
	public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		ContactDetails[] contactArray = {
				new ContactDetails("Nikhil", "Baireddy", "NelaKondapalli", "Khammam", "Telangana", 514526, 986354874,
						"nikil@gmail.com"),
				new ContactDetails("Naveen", "Pothuri", "Meena vati Nagar", "Darsi", "Andhra Pradesh", 562543, 987456320,
						"nani9876@gmail.com") };
		addressBook.addMultipleContactsToDBUsingThreads(Arrays.asList(contactArray));
		boolean result1 = addressBook.checkUpdatedRecordSyncWithDB("Nikhil");
		boolean result2 = addressBook.checkUpdatedRecordSyncWithDB("Naveen");
		Assert.assertTrue(result1);
		Assert.assertTrue(result2);
	}
	
	@Test
	public void givenAddressbookDataInJsonServer_WhenRetrieved_ShouldMatchCount() {
		ContactDetails[] arrayOfPerson = getAddressbookList();
		addressBook = new AddressBookService(Arrays.asList(arrayOfPerson));
		long entries = addressBook.countEntries(IOService.REST_IO);
		Assert.assertEquals(2, entries);
	}
	
	@Test
	public void givenMultiplePerson_WhenAdded_ShouldMatch201ResponseAndCount() {
		ContactDetails[] arrayOfContacts = getAddressbookList();
		addressBook = new AddressBookService(Arrays.asList(arrayOfContacts));
		ContactDetails[] arrayOfPerson = {
				new ContactDetails(0, "Yeswanth", "Kothuri", "Bavanagar Colony", "Ponnur", "AP", 522124, 987654421,
						"yeswanth@gmail.com"),
				new ContactDetails(0, "Abhinav", "Pulikonda", "GBC Road", "Ponnur", "AP", 522124, 988944321,
						"abhi@gmail.com"),
				new ContactDetails(0, "Jagadeesh", "Chilla", "Bandar", "Machillipatnam", "AP", 510214, 985654321,
						"jaggu@gmail.com") };

		for (ContactDetails addressbookData : arrayOfPerson) {

			Response response = addContactToJsonServer(addressbookData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);

			addressbookData = new Gson().fromJson(response.asString(), ContactDetails.class);
			addressBook.addContactToAddressbook(addressbookData, REST_IO);
		}
		long entries = addressBook.countEntries(REST_IO);
		Assert.assertEquals(5, entries);
	}
}
