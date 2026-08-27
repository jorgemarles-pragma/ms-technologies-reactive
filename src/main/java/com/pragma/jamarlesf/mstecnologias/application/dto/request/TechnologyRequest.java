package com.pragma.jamarlesf.mstecnologias.application.dto.request;

/**
 * Cuerpo del POST de registro. Sin anotaciones de Bean Validation a propósito:
 * las reglas viven en el caso de uso, para no tener dos fuentes de verdad.
 */
public record TechnologyRequest(String name, String description) {
}
