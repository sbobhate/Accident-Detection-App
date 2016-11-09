package com.lifeline;

/**
 * Created by shantanubobhate on 10/19/16.
 */

public class UserInformation {

    public String firstName, lastName, policyNumber, phoneNumber;

    public UserInformation() {

    }

    public UserInformation(String firstName, String lastName, String policyNumber, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.policyNumber = policyNumber;
        this.phoneNumber = phoneNumber;
    }
}
