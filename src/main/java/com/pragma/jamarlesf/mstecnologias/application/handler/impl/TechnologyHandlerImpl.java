package com.pragma.jamarlesf.mstecnologias.application.handler.impl;

import com.pragma.jamarlesf.mstecnologias.application.dto.request.TechnologyRequest;
import com.pragma.jamarlesf.mstecnologias.application.dto.response.TechnologyResponse;
import com.pragma.jamarlesf.mstecnologias.application.handler.TechnologyHandler;
import com.pragma.jamarlesf.mstecnologias.application.mapper.TechnologyDtoMapper;
import com.pragma.jamarlesf.mstecnologias.domain.api.TechnologyServicePort;
import com.pragma.jamarlesf.mstecnologias.domain.constants.TechnologyConstants;
import com.pragma.jamarlesf.mstecnologias.domain.exception.TechnologyValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Traduce el DTO de entrada al modelo de dominio, delega en el puerto de servicio y devuelve el
 * DTO de salida. Sin reglas de negocio y sin tipos HTTP.
 */
@Service
@RequiredArgsConstructor
public class TechnologyHandlerImpl implements TechnologyHandler {

    private final TechnologyServicePort technologyServicePort;
    private final TechnologyDtoMapper technologyDtoMapper;

    @Override
    public Mono<TechnologyResponse> saveTechnology(TechnologyRequest request) {
        return Mono.justOrEmpty(request)
                .switchIfEmpty(Mono.error(
                        new TechnologyValidationException(TechnologyConstants.TECHNOLOGY_REQUIRED)))
                .map(technologyDtoMapper::toModel)
                .flatMap(technologyServicePort::saveTechnology)
                .map(technologyDtoMapper::toResponse);
    }
}
