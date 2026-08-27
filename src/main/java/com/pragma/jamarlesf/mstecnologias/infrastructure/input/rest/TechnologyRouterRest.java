package com.pragma.jamarlesf.mstecnologias.infrastructure.input.rest;

import com.pragma.jamarlesf.mstecnologias.application.dto.request.TechnologyRequest;
import com.pragma.jamarlesf.mstecnologias.application.dto.response.TechnologyResponse;
import com.pragma.jamarlesf.mstecnologias.infrastructure.exceptionhandler.ApiErrorResponse;
import com.pragma.jamarlesf.mstecnologias.infrastructure.exceptionhandler.DomainExceptionFilter;
import com.pragma.jamarlesf.mstecnologias.infrastructure.input.rest.util.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Enrutamiento funcional de tecnologías. Con {@code RouterFunctions} springdoc no descubre la
 * ruta por sí solo, de ahí el {@code @RouterOperation}.
 */
@Configuration
public class TechnologyRouterRest {

    @Bean
    @RouterOperation(
            path = ApiPaths.TECHNOLOGIES,
            method = RequestMethod.POST,
            beanClass = TechnologyHandlerRest.class,
            beanMethod = "saveTechnology",
            operation = @Operation(
                    operationId = "saveTechnology",
                    tags = "Technologies",
                    summary = "Register a technology",
                    description = "Registers a technology that bootcamp capabilities can use. "
                            + "The name must be unique, at most 50 characters, and the description "
                            + "is mandatory with at most 90 characters.",
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TechnologyRequest.class))),
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Technology registered",
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = TechnologyResponse.class))),
                            @ApiResponse(responseCode = "400", description = "Validation failed or malformed body",
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ApiErrorResponse.class))),
                            @ApiResponse(responseCode = "409", description = "Technology name already exists",
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ApiErrorResponse.class)))
                    }))
    public RouterFunction<ServerResponse> technologyRoutes(TechnologyHandlerRest technologyHandlerRest,
                                                           DomainExceptionFilter domainExceptionFilter) {
        return RouterFunctions
                .route(RequestPredicates.POST(ApiPaths.TECHNOLOGIES)
                                .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)),
                        technologyHandlerRest::saveTechnology)
                .filter(domainExceptionFilter);
    }
}
