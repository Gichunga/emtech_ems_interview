package com.gichungasoftwares.emtech_ems.exceptions;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
