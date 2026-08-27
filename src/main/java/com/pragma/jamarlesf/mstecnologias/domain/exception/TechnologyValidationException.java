package com.pragma.jamarlesf.mstecnologias.domain.exception;

/**
 * Una tecnología incumple una regla de forma: campo obligatorio ausente o longitud excedida.
 */
public class TechnologyValidationException extends DomainException {

    public TechnologyValidationException(String message) {
        super(message);
    }
}
