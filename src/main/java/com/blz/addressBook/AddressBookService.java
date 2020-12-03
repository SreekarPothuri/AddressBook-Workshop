package com.blz.addressBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddressBookService {

	Scanner sc = new Scanner(System.in);
	List<ContactDetails> contactList = new ArrayList<ContactDetails>();
	
	public void addContact() {
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
		
		ContactDetails contact = new ContactDetails(firstName, lastName, address, city, state, zip, phoneNum, email);
		contactList.add(contact);
		System.out.println(contactList);
	}
}
