package com.pragma.jamarlesf.mstecnologias.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.pragma.jamarlesf.mstecnologias.domain.constants.TechnologyConstants;
import com.pragma.jamarlesf.mstecnologias.domain.exception.DuplicateTechnologyException;
import com.pragma.jamarlesf.mstecnologias.domain.exception.TechnologyValidationException;
import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import com.pragma.jamarlesf.mstecnologias.domain.spi.TechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    private static final Long GENERATED_ID = 1L;
    private static final String VALID_NAME = "Java";
    private static final String VALID_DESCRIPTION = "Object oriented programming language";

    @Mock
    private TechnologyPersistencePort technologyPersistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    private Technology validTechnology;

    @BeforeEach
    void setUp() {
        validTechnology = new Technology(null, VALID_NAME, VALID_DESCRIPTION);
    }

    @Test
    void saveTechnology_WhenDataIsValid_ShouldPersistAndReturnTechnologyWithId() {
        when(technologyPersistencePort.existsByName(VALID_NAME)).thenReturn(Mono.just(false));
        when(technologyPersistencePort.save(validTechnology))
                .thenReturn(Mono.just(new Technology(GENERATED_ID, VALID_NAME, VALID_DESCRIPTION)));

        StepVerifier.create(technologyUseCase.saveTechnology(validTechnology))
                .assertNext(saved -> {
                    assertEquals(GENERATED_ID, saved.getId());
                    assertEquals(VALID_NAME, saved.getName());
                    assertEquals(VALID_DESCRIPTION, saved.getDescription());
                })
                .verifyComplete();

        verify(technologyPersistencePort).existsByName(VALID_NAME);
        verify(technologyPersistencePort).save(validTechnology);
    }

    @Test
    void saveTechnology_WhenNameAlreadyExists_ShouldFailWithDuplicateTechnologyException() {
        when(technologyPersistencePort.existsByName(VALID_NAME)).thenReturn(Mono.just(true));

        StepVerifier.create(technologyUseCase.saveTechnology(validTechnology))
                .expectErrorMatches(error -> error instanceof DuplicateTechnologyException
                        && TechnologyConstants.NAME_ALREADY_EXISTS.equals(error.getMessage()))
                .verify();

        verify(technologyPersistencePort, never()).save(any());
    }

    @Test
    void saveTechnology_WhenTechnologyIsNull_ShouldFailWithValidationException() {
        expectValidationError(null, TechnologyConstants.TECHNOLOGY_REQUIRED);
    }

    @Test
    void saveTechnology_WhenNameIsNull_ShouldFailWithValidationException() {
        expectValidationError(new Technology(null, null, VALID_DESCRIPTION),
                TechnologyConstants.NAME_REQUIRED);
    }

    @Test
    void saveTechnology_WhenNameIsBlank_ShouldFailWithValidationException() {
        expectValidationError(new Technology(null, "   ", VALID_DESCRIPTION),
                TechnologyConstants.NAME_REQUIRED);
    }

    @Test
    void saveTechnology_WhenNameExceedsMaxLength_ShouldFailWithValidationException() {
        String tooLongName = "A".repeat(TechnologyConstants.NAME_MAX_LENGTH + 1);
        expectValidationError(new Technology(null, tooLongName, VALID_DESCRIPTION),
                TechnologyConstants.NAME_TOO_LONG);
    }

    @Test
    void saveTechnology_WhenNameHasExactlyMaxLength_ShouldPersist() {
        String boundaryName = "A".repeat(TechnologyConstants.NAME_MAX_LENGTH);
        expectPersisted(new Technology(null, boundaryName, VALID_DESCRIPTION));
    }

    @Test
    void saveTechnology_WhenDescriptionIsNull_ShouldFailWithValidationException() {
        expectValidationError(new Technology(null, VALID_NAME, null),
                TechnologyConstants.DESCRIPTION_REQUIRED);
    }

    @Test
    void saveTechnology_WhenDescriptionIsBlank_ShouldFailWithValidationException() {
        expectValidationError(new Technology(null, VALID_NAME, "   "),
                TechnologyConstants.DESCRIPTION_REQUIRED);
    }

    @Test
    void saveTechnology_WhenDescriptionExceedsMaxLength_ShouldFailWithValidationException() {
        String tooLongDescription = "A".repeat(TechnologyConstants.DESCRIPTION_MAX_LENGTH + 1);
        expectValidationError(new Technology(null, VALID_NAME, tooLongDescription),
                TechnologyConstants.DESCRIPTION_TOO_LONG);
    }

    @Test
    void saveTechnology_WhenDescriptionHasExactlyMaxLength_ShouldPersist() {
        String boundaryDescription = "A".repeat(TechnologyConstants.DESCRIPTION_MAX_LENGTH);
        expectPersisted(new Technology(null, VALID_NAME, boundaryDescription));
    }

    @Test
    void saveTechnology_WhenPersistenceFails_ShouldPropagateErrorUnchanged() {
        IllegalStateException failure = new IllegalStateException("connection lost");
        when(technologyPersistencePort.existsByName(VALID_NAME)).thenReturn(Mono.just(false));
        when(technologyPersistencePort.save(validTechnology)).thenReturn(Mono.error(failure));

        StepVerifier.create(technologyUseCase.saveTechnology(validTechnology))
                .expectErrorMatches(error -> error == failure)
                .verify();
    }

    /** Una entrada invalida se rechaza sin consultar la persistencia. */
    private void expectValidationError(Technology technology, String expectedMessage) {
        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .expectErrorMatches(error -> error instanceof TechnologyValidationException
                        && expectedMessage.equals(error.getMessage()))
                .verify();

        verifyNoInteractions(technologyPersistencePort);
    }

    private void expectPersisted(Technology technology) {
        when(technologyPersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(technologyPersistencePort.save(technology)).thenReturn(Mono.just(
                new Technology(GENERATED_ID, technology.getName(), technology.getDescription())));

        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .assertNext(saved -> assertEquals(GENERATED_ID, saved.getId()))
                .verifyComplete();

        verify(technologyPersistencePort).save(technology);
    }
}
