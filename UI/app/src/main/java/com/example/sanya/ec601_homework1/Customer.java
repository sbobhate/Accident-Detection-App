package com.example.sanya.ec601_homework1;

/**
 * Created by sanya on 22/5/15.
 */
public class Customer {
    String _name,_email, _password, _phone, _policy;
    String _verified;
    int _imagecl, _videocl;

    // Empty constructor
    public Customer() {

    }

    // constructor
    public Customer(String name, String email, String password, String phone) {
        this._name=name;
        this._email = email;
        this._password = password;
        this._phone = phone;
        //this._verified = verified;

    }
    public Customer(String name, String policy, String phone) {
        this._name=name;
        this._policy = policy;
        this._phone = phone;
        //this._verified = verified;

    }
    public String getName() {
        return this._name;
    }
    public void setName(String name) {
        this._name = name;
    }

    public String getEmail() {
        return this._email;
    }
    public void setEmail(String email) {
        this._email = email;
    }

    public String getPolicy() {
        return this._policy;
    }
    public void setPolicy(String policy) {
        this._policy = policy;
    }

    public String getPassword() {
        return this._password;
    }
    public void setPassword(String password) {
        this._password = password;
    }

    public String getPhone() {
        return this._phone;
    }
    public void setPhone(String phone) {
        this._phone = phone;
    }

//    public String getVerified() {
//        return this._verified;
//    }
//    public void setVerified(String verified) {
//        this._verified = verified;
//    }

}
