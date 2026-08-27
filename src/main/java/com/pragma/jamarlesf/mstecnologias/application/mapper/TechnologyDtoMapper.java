package com.pragma.jamarlesf.mstecnologias.application.mapper;

import com.pragma.jamarlesf.mstecnologias.application.dto.request.TechnologyRequest;
import com.pragma.jamarlesf.mstecnologias.application.dto.response.TechnologyResponse;
import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Traduce entre los DTO de la API y el modelo de dominio.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TechnologyDtoMapper {

    @Mapping(target = "id", ignore = true)
    Technology toModel(TechnologyRequest request);

    TechnologyResponse toResponse(Technology technology);
}
