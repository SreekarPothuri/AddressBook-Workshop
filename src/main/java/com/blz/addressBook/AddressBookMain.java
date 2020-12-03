package com.blz.addressBook;

import java.util.Scanner;

public class AddressBookMain {

	static AddressBookService addressBook = new AddressBookService();

	public static void main(String[] args) {
		int choice;
		System.out.println("Welcome to Address Book");
		do {
			System.out.println("1.Add Contact \n2.Edit Existing Contact \n3.Delete Contact "
										+ "\n4.Add Multiple Contacts \n5.Exit");
			Scanner sc = new Scanner(System.in);
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				addressBook.addContact();
				break;
			case 2:
				if (!addressBook.contactList.isEmpty())
					addressBook.editContact();
				else
					System.out.println("Address Book is empty");
				break;
			case 3:
				if (!addressBook.contactList.isEmpty())
					addressBook.deleteContact();
				else
					System.out.println("Address Book is empty");
				break;
			case 4:
				addressBook.addMultipleContacts();
				break;
			case 5:
				System.out.println("Exited Successfully!");
				break;
			default:
				System.out.println("Choose correct option from above mentioned option only!!");
				break;
			}
		} while (choice != 5);
	}
}