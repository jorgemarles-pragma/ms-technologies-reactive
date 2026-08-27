package com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.repository;

import com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Acceso reactivo a la tabla {@code technology}.
 */
@Repository
public interface TechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {

    Mono<Boolean> existsByName(String name);
}
