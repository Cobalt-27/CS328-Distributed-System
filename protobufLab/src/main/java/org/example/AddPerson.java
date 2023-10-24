package org.example;// See README.md for information and build instructions.

import com.example.tutorial.protos.AddressBook;
import com.example.tutorial.protos.Person;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

class AddPerson {

  static Person addDefaultPerson() {
    return Person.newBuilder()
            .setId(1234)
            .setName("John Doe")
            .setEmail("jdoe@example.com")
            .addPhones(
                    Person.PhoneNumber.newBuilder()
                            .setNumber("555-4321")
                            .setType(Person.PhoneType.HOME))
            .setAddress("SZU, Shenzhen, China")
            .build();
  }

  // Main function:  Reads the entire address book from a file,
  //   adds one person based on default information, then writes it back out to the same
  //   file.
  public static void main(String[] args) throws Exception {

    // The default path of the address book file
    String path = "protobufLab/src/main/resources/addressbook.data";

    AddressBook.Builder addressBook = AddressBook.newBuilder();

    // Read the existing address book.
    try {
      FileInputStream input = new FileInputStream(path);
      try {
        addressBook.mergeFrom(input);
      } finally {
        try { input.close(); } catch (Throwable ignore) {}
      }
    } catch (FileNotFoundException e) {
      System.out.println(path + ": File not found.  Creating a new file.");
      //create new file if not exist 

    }

    addressBook.addPeople(addDefaultPerson());

    // Write the new address book back to disk.
    FileOutputStream output = new FileOutputStream(path);
    try {
      addressBook.build().writeTo(output);
      System.out.println("Address book successfully updated.");
    } finally {
      output.close();
    }
  }
}
