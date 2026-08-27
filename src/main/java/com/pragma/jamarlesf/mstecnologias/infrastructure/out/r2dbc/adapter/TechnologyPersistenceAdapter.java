package com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.adapter;

import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import com.pragma.jamarlesf.mstecnologias.domain.spi.TechnologyPersistencePort;
import com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.mapper.TechnologyEntityMapper;
import com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Implementación PostgreSQL/R2DBC del puerto de persistencia de tecnologías.
 */
@Repository
@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements TechnologyPersistencePort {

    private final TechnologyRepository technologyRepository;
    private final TechnologyEntityMapper technologyEntityMapper;

    @Override
    public Mono<Technology> save(Technology technology) {
        return technologyRepository.save(technologyEntityMapper.toEntity(technology))
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return technologyRepository.existsByName(name);
    }
}
