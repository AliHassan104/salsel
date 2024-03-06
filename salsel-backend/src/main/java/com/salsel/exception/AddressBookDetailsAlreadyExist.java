package com.salsel.exception;

public class AddressBookDetailsAlreadyExist extends RuntimeException{
    public AddressBookDetailsAlreadyExist(String message)
    {
        super(message);
    }
}
