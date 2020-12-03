package com.blz.addressBook;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddressBookMain {

	static AddressBookService addressBook = new AddressBookService();
	static Map<String, AddressBookMain> addressBookObj = new HashMap();
	static AddressBookMain addressobj = new AddressBookMain();
	static Scanner sc = new Scanner(System.in);

	public static void addAddressBook() {
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Enter choice \n1.Create new addressbook " + "\n2.Add contacts into existing address Book \n3.Exit ");
		int entry = input.nextInt();
		if (entry != 3) {
			switch (entry) {
			case 1:
				Scanner nameInput = new Scanner(System.in);
				System.out.println("Enter name of new address book: ");
				String nameOfNewBook = nameInput.nextLine();
				if (addressBookObj.containsKey(nameOfNewBook)) {
					System.out.println("Address book already exists");
					break;
				}
				addressBookObj.put(nameOfNewBook, addressobj);
				System.out.println("Address book " + nameOfNewBook + " has been added");
				AddressBookMain.entries();
				break;
			case 2:
				Scanner existingAddressName = new Scanner(System.in);
				System.out.println("Enter name of address book: ");
				String nameOfExistingAddressBook = existingAddressName.nextLine();
				if (addressBookObj.containsKey(nameOfExistingAddressBook)) {
					addressBookObj.get(nameOfExistingAddressBook);
					AddressBookMain.entries();
				} else {
					System.out.println("Address book not found!!");
				}
			case 3:
				entry = 3;
				break;
			default:
				System.out.println(" Enter valid Input");
				break;
			}
		}
	}

	public static void entries() {
		int choice;
		System.out.println("Welcome to Address Book");

		do {
			System.out.println("1.Add Contact \n2.Edit Existing Contact \n3.Delete Contact "
					+ "\n4.Add Multiple Contacts \n5.Search By City or State \n6.View By City or State \n7.Exit");
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
				if (!addressBook.contactList.isEmpty()) {
					System.out.println("1)Search By City   2)Search By State");
					int ch = sc.nextInt();
					if (ch == 1) {
						addressBook.searchContactByCity();
					} else if (ch == 2) {
						addressBook.searchContactByState();
					} else {
						System.out.println("Choose correct option");
					}
				} else {
					System.out.println("Address book is empty");
				}
				break;
			case 6:
				if (!addressBook.contactList.isEmpty()) {
					System.out.println("1)View By City   2)View By State");
					int ch = sc.nextInt();
					if (ch == 1) {
						addressBook.viewContactByCity();
					} else if (ch == 2) {
						addressBook.viewContactByState();
					} else {
						System.out.println("Choose correct option");
					}
				} else {
					System.out.println("Address book is empty");
				}
				break;
			case 7:
				System.out.println("Exited to main menu!!");
				addAddressBook();
				break;
			default:
				System.out.println("Choose correct option from above mentioned option only!!");
				break;
			}
		} while (choice != 7);
	}

	public static void main(String[] args) {
		addAddressBook();
	}
}
