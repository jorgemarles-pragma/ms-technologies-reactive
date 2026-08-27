package com.pragma.jamarlesf.mstecnologias.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Fila de la tabla {@code technology}. R2DBC inserta cuando el id es nulo y actualiza cuando no.
 */
@Table("technology")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
