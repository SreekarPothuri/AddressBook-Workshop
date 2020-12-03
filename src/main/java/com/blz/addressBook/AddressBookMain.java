package com.blz.addressBook;

public class AddressBookMain {

	static AddressBookService addressBook = new AddressBookService();

	public static void main(String[] args) {
		addressBook.addContact();
	}
}
