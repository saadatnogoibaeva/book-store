package com.LibraryManagement.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Borrower {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMyBook() {
        return myBook;
    }

    public void setMyBook(String myBook) {
        this.myBook = myBook;
    }

    public int getId() {
        return id;
    }
    private String name, email, phone, myBook;
}
