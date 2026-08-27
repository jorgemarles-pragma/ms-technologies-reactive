-- R2DBC no soporta ddl-auto. Declara aqui el esquema del microservicio.
-- Se ejecuta segun spring.sql.init.mode.

CREATE TABLE IF NOT EXISTS technology (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(90) NOT NULL,
    CONSTRAINT uk_technology_name UNIQUE (name)
);
