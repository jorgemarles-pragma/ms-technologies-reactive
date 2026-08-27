package com.pragma.jamarlesf.mstecnologias.domain.spi;

import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida: lo que el dominio necesita de un almacén de tecnologías.
 */
public interface TechnologyPersistencePort {

    Mono<Technology> save(Technology technology);

    Mono<Boolean> existsByName(String name);
}
