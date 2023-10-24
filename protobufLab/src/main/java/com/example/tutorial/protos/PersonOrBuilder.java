// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/addressbook.proto

package com.example.tutorial.protos;

public interface PersonOrBuilder extends
    // @@protoc_insertion_point(interface_extends:tutorial.Person)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * Unique ID number for this person.
   * </pre>
   *
   * <code>int32 id = 2;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>string email = 3;</code>
   * @return The email.
   */
  java.lang.String getEmail();
  /**
   * <code>string email = 3;</code>
   * @return The bytes for email.
   */
  com.google.protobuf.ByteString
      getEmailBytes();

  /**
   * <pre>
   * New field
   * </pre>
   *
   * <code>string address = 6;</code>
   * @return The address.
   */
  java.lang.String getAddress();
  /**
   * <pre>
   * New field
   * </pre>
   *
   * <code>string address = 6;</code>
   * @return The bytes for address.
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <code>repeated .tutorial.Person.PhoneNumber phones = 4;</code>
   */
  java.util.List<com.example.tutorial.protos.Person.PhoneNumber> 
      getPhonesList();
  /**
   * <code>repeated .tutorial.Person.PhoneNumber phones = 4;</code>
   */
  com.example.tutorial.protos.Person.PhoneNumber getPhones(int index);
  /**
   * <code>repeated .tutorial.Person.PhoneNumber phones = 4;</code>
   */
  int getPhonesCount();
  /**
   * <code>repeated .tutorial.Person.PhoneNumber phones = 4;</code>
   */
  java.util.List<? extends com.example.tutorial.protos.Person.PhoneNumberOrBuilder> 
      getPhonesOrBuilderList();
  /**
   * <code>repeated .tutorial.Person.PhoneNumber phones = 4;</code>
   */
  com.example.tutorial.protos.Person.PhoneNumberOrBuilder getPhonesOrBuilder(
      int index);
}