package com.rkovaliov.bu.advices;

import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.services.impl.MemberServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MemberServiceImpl.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception e) {
        LOG.error("Unhandled exception: ", e);
        return new ResponseEntity<>("Unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberNotExistsException.class)
    public ResponseEntity<Object> handleMemberNotExistsException(final Exception e) {
        LOG.error("Trying to get member with id: " + e.getMessage());
        return new ResponseEntity<>("Member with this id is not exists", HttpStatus.NOT_FOUND);
    }
}
