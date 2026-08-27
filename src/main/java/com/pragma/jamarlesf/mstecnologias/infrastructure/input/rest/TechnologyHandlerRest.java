package com.pragma.jamarlesf.mstecnologias.infrastructure.input.rest;

import java.net.URI;

import com.pragma.jamarlesf.mstecnologias.application.dto.request.TechnologyRequest;
import com.pragma.jamarlesf.mstecnologias.application.dto.response.TechnologyResponse;
import com.pragma.jamarlesf.mstecnologias.application.handler.TechnologyHandler;
import com.pragma.jamarlesf.mstecnologias.domain.constants.TechnologyConstants;
import com.pragma.jamarlesf.mstecnologias.domain.exception.TechnologyValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Adaptador HTTP: extrae el DTO del cuerpo, llama al punto de entrada de la aplicación y
 * convierte el DTO de salida en una respuesta WebFlux. Toda la dependencia de HTTP vive aquí.
 */
@Component
@RequiredArgsConstructor
public class TechnologyHandlerRest {

    private final TechnologyHandler technologyHandler;

    public Mono<ServerResponse> saveTechnology(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .switchIfEmpty(Mono.error(
                        new TechnologyValidationException(TechnologyConstants.TECHNOLOGY_REQUIRED)))
                .flatMap(technologyHandler::saveTechnology)
                .flatMap(response -> created(request, response));
    }

    private Mono<ServerResponse> created(ServerRequest request, TechnologyResponse response) {
        return ServerResponse.created(URI.create(request.path() + "/" + response.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
