# ms-tecnologias

Microservicio reactivo hexagonal de gestión de tecnologías para el reto 3.

Este repositorio es un **andamiaje**: contiene toda la configuración lista (build, perfiles, Docker,
seguridad, observabilidad) y el árbol de carpetas de la arquitectura hexagonal, pero **sin código de
dominio, aplicación ni infraestructura**. El único archivo fuente es la clase principal de Spring Boot.

## Stack

| Componente | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.16 |
| Gradle (wrapper) | 8.14.5 |
| Web | Spring WebFlux (reactivo, sin servlet) |
| Persistencia | Spring Data R2DBC + PostgreSQL (`r2dbc-postgresql`) |
| Seguridad | Spring Security reactivo + jjwt 0.13.0 |
| Documentación | springdoc-openapi WebFlux UI 2.9.0 |
| Resiliencia | Resilience4j 2.3.0 (circuit breaker, retry, bulkhead, time limiter) |
| Observabilidad | Actuator + Micrometer + Prometheus + tracing Brave |
| Mapeo | MapStruct 1.6.3 + Lombok 1.18.46 |
| Cobertura | JaCoCo |

Todo el stack es **no bloqueante**: nada de `spring-boot-starter-web`, JPA, JDBC, Hibernate,
OpenFeign ni `RestTemplate`. Las llamadas salientes se hacen con `WebClient`.

## Requisitos

- JDK 17
- Docker y Docker Compose (para el modo contenedor)
- PostgreSQL 17 accesible (o el contenedor del `docker-compose.yml`)

## Correr en local

```bash
cp .env.example .env      # ajusta credenciales y JWT_SECRET
./gradlew bootRun --args='--spring.profiles.active=local'
```

Servicio en `http://localhost:8080`.
Swagger UI en `http://localhost:8080/swagger-ui.html`.
Health en `http://localhost:8080/actuator/health`.

## Correr con Docker

```bash
cp .env.example .env
docker compose up --build
```

Levanta PostgreSQL (`mstecnologias-db`) con healthcheck y la app (`mstecnologias-app`), que sólo
arranca cuando la base de datos está sana. La app usa el perfil `docker`.

Sólo la imagen de la app:

```bash
docker build -t ms-tecnologias .
docker run --rm -p 8080:8080 --env-file .env ms-tecnologias
```

## Tests y cobertura

```bash
./gradlew test              # ejecuta tests y genera el reporte JaCoCo
```

Reporte HTML en `build/reports/jacoco/test/html/index.html`.

## Esquema de base de datos

R2DBC **no** tiene `ddl-auto`. El esquema se declara a mano en `src/main/resources/schema.sql` y se
ejecuta según `spring.sql.init.mode` (`always` por defecto). Escribe ahí las sentencias
`CREATE TABLE IF NOT EXISTS ...` del microservicio.

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `SERVER_PORT` | Puerto HTTP del servicio | `8080` |
| `SPRING_R2DBC_URL` | URL R2DBC de la base de datos | `r2dbc:postgresql://localhost:5432/mstecnologias` |
| `DB_USERNAME` | Usuario de base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de base de datos | `postgres` |
| `POSTGRES_DB` | Nombre de la base (sólo compose) | `mstecnologias` |
| `DB_PORT` | Puerto publicado de PostgreSQL (sólo compose) | `5432` |
| `SQL_INIT_MODE` | Ejecución de `schema.sql` (`always`/`never`) | `always` |
| `JWT_SECRET` | Clave HS256 en Base64 para firmar/validar tokens | sin valor (obligatoria) |
| `TRACING_SAMPLING` | Probabilidad de muestreo de trazas | `0.1` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`local` / `docker`) | ninguno |

## Arquitectura hexagonal

```
src/main/java/com/pragma/jamarlesf/mstecnologias/
├── MsTecnologiasApplication.java   clase principal (único fuente existente)
│
├── domain/                         núcleo puro: sin Spring, sin anotaciones de framework
│   ├── model/                      entidades y objetos de valor del negocio
│   ├── api/                        puertos de ENTRADA (interfaces *ServicePort)
│   ├── spi/                        puertos de SALIDA (*PersistencePort, *Gateway)
│   ├── usecase/                    casos de uso: implementan api/, dependen de spi/
│   ├── exception/                  excepciones de negocio
│   ├── constants/                  mensajes y constantes de dominio
│   └── enums/                      enumeraciones del dominio
│
├── application/                    orquestación entre infraestructura y dominio
│   ├── dto/request/                DTO de entrada
│   ├── dto/response/               DTO de salida
│   ├── handler/                    interfaces de handler
│   ├── handler/impl/               implementaciones: DTO <-> modelo, invocan api/
│   ├── mapper/                     MapStruct entre DTO y modelo
│   └── config/                     @Bean que arman los casos de uso (wiring)
│
└── infrastructure/                 detalles técnicos, todo lo que se puede reemplazar
    ├── configuration/              configuración general de Spring
    ├── documentation/              OpenAPI / springdoc
    ├── exceptionhandler/           traducción de excepciones a respuestas HTTP
    ├── input/rest/                 RouterFunction + Handler (WebFlux funcional)
    │   ├── dto/  mapper/  util/
    ├── out/r2dbc/                  adaptador de persistencia reactiva
    │   ├── adapter/                implementa los puertos spi/
    │   ├── entity/                 entidades R2DBC (@Table)
    │   ├── mapper/                 MapStruct entre entidad y modelo
    │   └── repository/             ReactiveCrudRepository / R2dbcRepository
    ├── out/rest/                   clientes salientes con WebClient
    │   ├── adapter/  client/  configuration/  dto/  exception/
    └── security/                   JWT reactivo
        ├── filter/                 WebFilter de autenticación
        └── utils/                  generación y validación de tokens
```

Regla de dependencias: `infrastructure` → `application` → `domain`. El dominio no conoce a nadie.
Todo lo que cruce una capa lo hace a través de un puerto (`api/` o `spi/`) y devuelve `Mono`/`Flux`.
