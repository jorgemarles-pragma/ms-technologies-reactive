package com.pragma.jamarlesf.mstecnologias.domain.exception;

/**
 * Raíz de las excepciones de negocio. La infraestructura la traduce a una respuesta HTTP;
 * el dominio nunca conoce códigos de estado.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
