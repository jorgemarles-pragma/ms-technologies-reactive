package com.pragma.jamarlesf.mstecnologias.domain.usecase;

import com.pragma.jamarlesf.mstecnologias.domain.api.TechnologyServicePort;
import com.pragma.jamarlesf.mstecnologias.domain.constants.TechnologyConstants;
import com.pragma.jamarlesf.mstecnologias.domain.exception.DuplicateTechnologyException;
import com.pragma.jamarlesf.mstecnologias.domain.exception.TechnologyValidationException;
import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import com.pragma.jamarlesf.mstecnologias.domain.spi.TechnologyPersistencePort;
import reactor.core.publisher.Mono;

/**
 * Reglas de negocio del registro de tecnologías. Valida antes de tocar la persistencia y
 * señala los incumplimientos como {@code Mono.error}, no como excepciones lanzadas: en una
 * cadena reactiva el suscriptor debe recibir una señal {@code onError}, no un throw síncrono.
 */
public class TechnologyUseCase implements TechnologyServicePort {

    private final TechnologyPersistencePort technologyPersistencePort;

    public TechnologyUseCase(TechnologyPersistencePort technologyPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
    }

    @Override
    public Mono<Technology> saveTechnology(Technology technology) {
        return validate(technology)
                .flatMap(valid -> technologyPersistencePort.existsByName(valid.getName())
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                                ? duplicate()
                                : technologyPersistencePort.save(valid)));
    }

    private Mono<Technology> validate(Technology technology) {
        if (technology == null) {
            return invalid(TechnologyConstants.TECHNOLOGY_REQUIRED);
        }
        if (isBlank(technology.getName())) {
            return invalid(TechnologyConstants.NAME_REQUIRED);
        }
        if (technology.getName().length() > TechnologyConstants.NAME_MAX_LENGTH) {
            return invalid(TechnologyConstants.NAME_TOO_LONG);
        }
        if (isBlank(technology.getDescription())) {
            return invalid(TechnologyConstants.DESCRIPTION_REQUIRED);
        }
        if (technology.getDescription().length() > TechnologyConstants.DESCRIPTION_MAX_LENGTH) {
            return invalid(TechnologyConstants.DESCRIPTION_TOO_LONG);
        }
        return Mono.just(technology);
    }

    private Mono<Technology> invalid(String message) {
        return Mono.error(new TechnologyValidationException(message));
    }

    private Mono<Technology> duplicate() {
        return Mono.error(new DuplicateTechnologyException(TechnologyConstants.NAME_ALREADY_EXISTS));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
