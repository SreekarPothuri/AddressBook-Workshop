package com.blz.addressBook;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.addressBook.AddressBookService.IOService;

public class AddressBookTest {

	private static AddressBookService addressBook;

	@BeforeClass
	public static void createAddressBookObj() {
		addressBook = new AddressBookService();
		System.out.println("Welcome to the Address Book System.. ");
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
}
