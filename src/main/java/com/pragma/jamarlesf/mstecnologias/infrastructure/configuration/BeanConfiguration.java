package com.pragma.jamarlesf.mstecnologias.infrastructure.configuration;

import com.pragma.jamarlesf.mstecnologias.domain.api.TechnologyServicePort;
import com.pragma.jamarlesf.mstecnologias.domain.spi.TechnologyPersistencePort;
import com.pragma.jamarlesf.mstecnologias.domain.usecase.TechnologyUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado de los casos de uso. Vive aquí para que el dominio no necesite anotaciones de Spring.
 * El adaptador de persistencia lo aporta el component scan ({@code @Repository}).
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public TechnologyServicePort technologyServicePort(TechnologyPersistencePort technologyPersistencePort) {
        return new TechnologyUseCase(technologyPersistencePort);
    }
}
