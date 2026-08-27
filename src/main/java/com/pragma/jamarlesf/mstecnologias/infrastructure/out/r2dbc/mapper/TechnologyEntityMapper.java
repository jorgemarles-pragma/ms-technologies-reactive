package com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.mapper;

import com.pragma.jamarlesf.mstecnologias.domain.model.Technology;
import com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Traduce entre el modelo de dominio y la entidad de persistencia.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TechnologyEntityMapper {

    TechnologyEntity toEntity(Technology technology);

    Technology toModel(TechnologyEntity entity);
}
