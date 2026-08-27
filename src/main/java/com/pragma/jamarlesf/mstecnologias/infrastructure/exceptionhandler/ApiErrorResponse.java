package com.pragma.jamarlesf.mstecnologias.infrastructure.exceptionhandler;

/**
 * Cuerpo uniforme de los errores de la API.
 */
public record ApiErrorResponse(int status, String error, String message) {
}
