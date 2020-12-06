package com.blz.addressBook;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddressBookMain {

	static AddressBookService addressBook = new AddressBookService();
	static Map<String, AddressBookMain> addressBookObj = new HashMap();
	static AddressBookMain addressobj = new AddressBookMain();
	static Scanner sc = new Scanner(System.in);
	private static String addressBookName;

	public static void addAddressBook() throws IOException {
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

	public static void entries() throws IOException {
		int choice;
		System.out.println("Welcome to Address Book");

		do {
			System.out.println("1.Add Contact \n2.Edit Existing Contact \n3.Delete Contact "
					+ "\n4.Add Multiple Contacts \n5.Search By City or State \n6.View By City or State"
					+ "\n7.Count By City or State \n8.Sort Contacts Alphabetically \n9.Sort Contacts By City,State or Zip"
					+ "\n10.Read or Write into File \n11.Read or write Data into CSV File \n12.Exit");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				addressBook.addContact(addressBookName);
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
				if (!addressBook.contactList.isEmpty()) {
					System.out.println("1)Count By City   2)Count By State");
					int ch = sc.nextInt();
					if (ch == 1) {
						addressBook.countContactsByCity();
					} else if (ch == 2) {
						addressBook.countContactsByState();
					} else {
						System.out.println("Choose correct option");
					}
				} else {
					System.out.println("Address book is empty");
				}
				break;
			case 8:
				if (!addressBook.contactList.isEmpty())
					addressBook.sortContactByName();
				else
					System.out.println("Address Book is empty");
				break;
			case 9:
				if (!addressBook.contactList.isEmpty()) {
					System.out.println("1)Sort Contacts By City   2)Sort Contacts By State	3)Sort Contacts by Zip");
					int ch = sc.nextInt();
					switch (ch) {
					case 1:
						addressBook.sortEntriesByCity();
						break;
					case 2:
						addressBook.sortEntriesByState();
						break;
					case 3:
						addressBook.sortEntriesByZip();
						break;
					default:
						System.out.println("Choose right choice from above mentioned options");
						break;
					}
				} else {
					System.out.println("Address book is empty");
				}
				break;
			case 10:
				System.out.println("1)Read Data From File   2)Add Contact To File");
				int ch = sc.nextInt();
				if (ch == 1) {
					addressBook.readDataFromFile();
				} else if (ch == 2) {
					addressBook.addContact(addressBookName);
					System.out.println("Successfully Added to text File!!");
				} else {
					System.out.println("Choose correct option");
				}
				break;
			case 11:
				System.out.println("1)Read Data From CSV File   2)Add Contact To CSV File");
				int select = sc.nextInt();
				if (select == 1) {
					addressBook.readDataFromCSVFile();
				} else if (select == 2) {
					addressBook.addContact(addressBookName);
					System.out.println("Successfully Added to CSV File!!");
				} else {
					System.out.println("Choose correct option");
				}
				break;
			case 12:
				System.out.println("Exited to main menu!!");
				addAddressBook();
				break;
			default:
				System.out.println("Choose correct option from above mentioned option only!!");
				break;
			}
		} while (choice != 12);
	}

	public static void main(String[] args) throws IOException {
		addAddressBook();
	}
}
