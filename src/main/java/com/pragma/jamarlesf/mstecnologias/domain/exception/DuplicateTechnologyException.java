package com.pragma.jamarlesf.mstecnologias.domain.exception;

/**
 * Ya existe una tecnología registrada con el mismo nombre.
 */
public class DuplicateTechnologyException extends DomainException {

    public DuplicateTechnologyException(String message) {
        super(message);
    }
}
