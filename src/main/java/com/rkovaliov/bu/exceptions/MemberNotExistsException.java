package com.rkovaliov.bu.exceptions;

public class MemberNotExistsException extends Exception {

    public MemberNotExistsException(long id) {
        super("The user with ID:" + id + " does not exist!");
    }

}
