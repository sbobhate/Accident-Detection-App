package com.lifeline;

public class EmerContact {
    String _name,_phone,_email;
    String _contact;

    // Empty constructor
    public EmerContact() {

    }

    // constructor
    public EmerContact(String contact) {
        this._contact=contact;
    }
    public EmerContact( String name, String phone,String email) {
        this._email=email;
        this._name=name;
        this._phone=phone;
    }

    public String getEmail() {
        return this._email;
    }
    public void setEmail(String email) {
        this._email= email;
    }

    public String getName() {
        return this._name;
    }
    public void setName(String name) {
        this._name = name;
    }

    public String getPhone() {
        return this._phone;
    }
    public void setPhone(String phone) {
        this._phone = phone;
    }
}
