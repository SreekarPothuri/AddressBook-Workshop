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
}
