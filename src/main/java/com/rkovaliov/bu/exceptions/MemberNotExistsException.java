package com.rkovaliov.bu.exceptions;

public class MemberNotExistsException extends Exception {

    public MemberNotExistsException() {
    }

    public MemberNotExistsException(String message) {
        super(message);
    }

    public MemberNotExistsException(long id) {
        super("The member with ID:" + id + " does not exist!");
    }
}
