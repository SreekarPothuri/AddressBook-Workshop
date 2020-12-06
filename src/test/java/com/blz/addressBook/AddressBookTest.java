package com.blz.addressBook;

import java.sql.SQLException;
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
}
