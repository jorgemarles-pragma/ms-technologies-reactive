package com.pragma.jamarlesf.mstecnologias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Núcleo del dominio: una tecnología que las capacidades del bootcamp pueden usar.
 * Inmutable y sin dependencias de framework. Las reglas de negocio se validan en el caso de uso.
 */
@Getter
@AllArgsConstructor
public class Technology {

    private final Long id;
    private final String name;
    private final String description;
}
