package com.example.instagramv1.utils

enum class ValidationErrorMessage(val message : String) {
    NAME_NOT_GIVEN("Full Name is empty"),
    NAME_NOT_VALID("Name not valid - Minimum 3 letters and maximum 20 letters , cannot contain numbers and empty spaces at end and start and contain only few special characters ' -"),
    USER_NAME_ALREADY_EXIST("Username already exist"),
    USER_NAME_DOES_NOT_MEET_REQUIREMENTS("Minimum 4 letters, maximum 20 letters, at least 3 alphabets,cannot contain empty spaces at end and start, can contain numbers and . _ "),
    USER_NAME_NOT_GIVEN("Username is empty"),
    EMAIL_NOT_VALID("Email not valid"),
    EMAIL_NOT_GIVEN("Email is empty"),
    EMAIL_ALREADY_EXIST("Email already exist"),
    PHONE_ALREADY_EXIST("Phone already exist"),
    PHONE_NOT_VALID("Phone not in format"),
    PHONE_NOT_GIVEN("Phone is empty"),
    PASSWORD_NOT_GIVEN("Password is empty"),
    CONFIRM_PASSWORD_NOT_GIVEN("Confirm Password is empty"),
    PASSWORD_DOES_NOT_MEET_REQUIREMENTS("Password should contain 8 chars with at least 1 uppercase , 1 special character and a number"),
    PASSWORD_NOT_MATCHED("Password not matched")

}