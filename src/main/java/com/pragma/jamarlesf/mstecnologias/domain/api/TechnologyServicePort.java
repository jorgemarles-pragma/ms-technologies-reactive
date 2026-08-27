package com.pragma.jamarlesf.mstecnologias.domain.api;

import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada: lo que la aplicación puede pedirle al dominio sobre tecnologías.
 */
public interface TechnologyServicePort {

    Mono<Technology> saveTechnology(Technology technology);
}
