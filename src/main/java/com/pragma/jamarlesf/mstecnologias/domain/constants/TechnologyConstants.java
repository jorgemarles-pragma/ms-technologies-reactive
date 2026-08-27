package com.pragma.jamarlesf.mstecnologias.domain.constants;

/**
 * Límites y mensajes de las reglas de negocio de tecnologías.
 */
public final class TechnologyConstants {

    public static final int NAME_MAX_LENGTH = 50;
    public static final int DESCRIPTION_MAX_LENGTH = 90;

    public static final String TECHNOLOGY_REQUIRED = "Technology is required";
    public static final String NAME_REQUIRED = "Technology name is required";
    public static final String NAME_TOO_LONG =
            "Technology name must not exceed " + NAME_MAX_LENGTH + " characters";
    public static final String DESCRIPTION_REQUIRED = "Technology description is required";
    public static final String DESCRIPTION_TOO_LONG =
            "Technology description must not exceed " + DESCRIPTION_MAX_LENGTH + " characters";
    public static final String NAME_ALREADY_EXISTS = "Technology name already exists";

    private TechnologyConstants() {
    }
}
