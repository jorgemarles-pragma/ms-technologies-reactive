package com.pragma.jamarlesf.mstecnologias.application.handler;

import com.pragma.jamarlesf.mstecnologias.application.dto.request.TechnologyRequest;
import com.pragma.jamarlesf.mstecnologias.application.dto.response.TechnologyResponse;
import reactor.core.publisher.Mono;

/**
 * Punto de entrada de la aplicación para el registro de tecnologías.
 * Habla en DTO, no en tipos de transporte: cualquier adaptador de entrada (REST, mensajería,
 * gRPC, un caso batch) puede invocarlo sin arrastrar dependencias de WebFlux.
 */
public interface TechnologyHandler {

    Mono<TechnologyResponse> saveTechnology(TechnologyRequest request);
}
