package com.blz.addressBook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddressBookService {

	Scanner sc = new Scanner(System.in);
	List<ContactDetails> contactList = new ArrayList<ContactDetails>();
	ContactDetails newEntry;
	boolean isExist;
	private String addressBookName;

	public void addContact(String addressBookName) {
		isExist = false;
		System.out.println("Enter First Name: ");
		String firstName = sc.nextLine();
		System.out.println("Enter Last Name: ");
		String lastName = sc.nextLine();
		System.out.println("Enter your address: ");
		String address = sc.nextLine();
		System.out.println("Enter your city: ");
		String city = sc.nextLine();
		System.out.println("Enter your state: ");
		String state = sc.nextLine();
		System.out.println("Enter your ZipCode: ");
		int zip = sc.nextInt();
		System.out.println("Enter your Phone number: ");
		long phoneNum = sc.nextLong();
		sc.nextLine();
		System.out.println("Enter your emailId: ");
		String email = sc.nextLine();
		if (contactList.size() > 0) {
			for (ContactDetails details : contactList) {
				newEntry = details;
				if (firstName.equals(newEntry.firstName) && lastName.equals(newEntry.lastName)) {
					System.out.println("Contact " + newEntry.firstName + " " + newEntry.lastName + " already exists");
					isExist = true;
					break;
				}
			}
		}
		if (!isExist) {
			newEntry = new ContactDetails(firstName, lastName, address, city, state, zip, phoneNum, email);
			contactList.add(newEntry);
			addDataToFile(firstName, lastName, address, city, state, phoneNum, zip, email, addressBookName);
		}
		System.out.println(contactList);
	}

	public void editContact() {
		System.out.println("Enter First name of contact that you want to edit: ");
		String firstNameToEdit = sc.nextLine();
		for (int id = 0; id < contactList.size(); id++) {
			if (contactList.get(id).getFirstName().equals(firstNameToEdit)) {
				System.out.println("Index: " + contactList.get(id));
				Scanner editInput = new Scanner(System.in);
				System.out.println("Enter your choice: \n1.First Name \n2.Last Name \n3.Address \n4.City \n5.State "
						+ "\n6.Zip Code \n7.Phone Number \n8.EmailId");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					System.out.println("Enter new First Name: ");
					String newFirstName = editInput.nextLine();
					contactList.get(id).setFirstName(newFirstName);
					break;
				case 2:
					System.out.println("Enter new Last Name: ");
					String newLastName = editInput.nextLine();
					contactList.get(id).setLastName(newLastName);
					break;
				case 3:
					System.out.println("Enter new Address: ");
					String newAddress = editInput.nextLine();
					contactList.get(id).setAddress(newAddress);
					break;
				case 4:
					System.out.println("Enter new City: ");
					String newCity = editInput.nextLine();
					contactList.get(id).setCity(newCity);
					break;
				case 5:
					System.out.println("Enter new State: ");
					String newState = editInput.nextLine();
					contactList.get(id).setState(newState);
					break;
				case 6:
					System.out.println("Enter new ZipCode: ");
					int newZip = editInput.nextInt();
					contactList.get(id).setZip(newZip);
					break;
				case 7:
					System.out.println("Enter new Phone Number: ");
					long newPhoneNum = editInput.nextLong();
					contactList.get(id).setPhoneNum(newPhoneNum);
					break;
				case 8:
					System.out.println("Enter new Email Id: ");
					String newEmail = editInput.nextLine();
					contactList.get(id).setEmail(newEmail);
					break;
				default:
					System.out.println("Choose right choice!!");
					break;
				}
			}
		}
		System.out.println(contactList);
	}

	public void deleteContact() {
		for (int i = 0; i < contactList.size(); i++) {
			System.out.println("Enter First name of contact to delete: ");
			String deletefirstName = sc.nextLine();
			if (contactList.get(i).getFirstName().equalsIgnoreCase(deletefirstName)) {
				contactList.remove(i);
				System.out.println(deletefirstName + " deleted successfully!");
			} else {
				System.out.println("No such contact exists!!");
			}
		}
	}

	public void addMultipleContacts() {
		System.out.println("Enter number of persons to add to Address Book: ");
		int noOfPersons = sc.nextInt();
		sc.nextLine();
		int count = 1;
		while (count <= noOfPersons) {
			addContact(addressBookName);
			count++;
		}
	}

	public void searchContactByCity() {
		System.out.println("Enter City Name:");
		String city = sc.nextLine();
		contactList.stream().filter(n -> n.getCity().equals(city)).forEach(i -> System.out.println(i.firstName));
	}

	public void searchContactByState() {
		System.out.println("Enter State Name:");
		String state = sc.nextLine();
		contactList.stream().filter(n -> n.getState().equals(state)).forEach(i -> System.out.println(i.firstName));
	}

	public void viewContactByCity() {
		System.out.println("Enter City Name:");
		String city = sc.nextLine();
		contactList.stream().filter(n -> n.getCity().equals(city)).forEach(i -> System.out.println(i));
	}

	public void viewContactByState() {
		System.out.println("Enter State Name:");
		String state = sc.nextLine();
		contactList.stream().filter(n -> n.getState().equals(state)).forEach(i -> System.out.println(i));
	}

	public void countContactsByCity() {
		int count = 0;
		System.out.println("Enter City Name : ");
		String city = sc.nextLine();
		count = (int) contactList.stream().filter(n -> n.getCity().equals(city)).count();
		System.out.println(count);
	}

	public void countContactsByState() {
		int count = 0;
		System.out.println("Enter State Name : ");
		String state = sc.nextLine();
		count = (int) contactList.stream().filter(n -> n.getState().equals(state)).count();
		System.out.println(count);
	}

	public void sortContactByName() {
		contactList = contactList.stream().sorted(Comparator.comparing(ContactDetails::getFirstName))
				.collect(Collectors.toList());
		contactList.forEach(i -> System.out.println(i));
	}

	public void sortEntriesByCity() {
		contactList = contactList.stream().sorted(Comparator.comparing(ContactDetails::getCity))
				.collect(Collectors.toList());
		contactList.forEach(i -> System.out.println(i));
	}

	public void sortEntriesByState() {
		contactList = contactList.stream().sorted(Comparator.comparing(ContactDetails::getState))
				.collect(Collectors.toList());
		contactList.forEach(i -> System.out.println(i));
	}

	public void sortEntriesByZip() {
		contactList = contactList.stream().sorted(Comparator.comparing(ContactDetails::getZip))
				.collect(Collectors.toList());
		contactList.forEach(i -> System.out.println(i));
	}

	public void addDataToFile(String firstName, String lastName, String address, String city, String state,
			long phoneNumber, int zip, String email, String addressBookName) {
		System.out.println("Enter name for txt written file : ");
		String fileName = sc.nextLine();
		File file = new File("F:\\BridgeLabz Fellowship Program\\FilesIncluded\\AddressBook\\" + fileName + ".txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("Contact:" + "\n1.First name: " + firstName + "\n2.Last name: " + lastName
					+ "\n3.Address: " + address + "\n4.City: " + city + "\n5.State: " + state + "\n6.Phone number: "
					+ phoneNumber + "\n7.Zip: " + zip + "\n8.email: " + email + "\n");
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readDataFromFile() {
		System.out.println("Enter address book name : ");
		String fileName = sc.nextLine();
		Path filePath = Paths
				.get("F:\\BridgeLabz Fellowship Program\\FilesIncluded\\AddressBook\\" + fileName + ".txt");
		try {
			Files.lines(filePath).map(line -> line.trim()).forEach(line -> System.out.println(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
