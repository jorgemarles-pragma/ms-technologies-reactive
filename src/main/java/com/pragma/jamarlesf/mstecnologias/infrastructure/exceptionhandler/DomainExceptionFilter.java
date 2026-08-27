package com.pragma.jamarlesf.mstecnologias.infrastructure.exceptionhandler;

import com.pragma.jamarlesf.mstecnologias.domain.constants.TechnologyConstants;
import com.pragma.jamarlesf.mstecnologias.domain.exception.DomainException;
import com.pragma.jamarlesf.mstecnologias.domain.exception.DuplicateTechnologyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * Traduce excepciones a respuestas HTTP para las rutas funcionales.
 * {@code @ControllerAdvice} no intercepta {@code RouterFunction}, por eso el filtro se engancha
 * al router en vez de declararse como advice.
 */
@Slf4j
@Component
public class DomainExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final String MALFORMED_BODY = "Request body is missing or malformed";
    private static final String UNEXPECTED_ERROR = "Unexpected error";

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return Mono.defer(() -> next.handle(request))
                .onErrorResume(this::toErrorResponse);
    }

    private Mono<ServerResponse> toErrorResponse(Throwable error) {
        if (error instanceof DuplicateTechnologyException duplicate) {
            return build(HttpStatus.CONFLICT, duplicate.getMessage());
        }
        if (error instanceof DomainException domain) {
            return build(HttpStatus.BAD_REQUEST, domain.getMessage());
        }
        if (error instanceof ServerWebInputException) {
            return build(HttpStatus.BAD_REQUEST, MALFORMED_BODY);
        }
        // existsByName y save no son atomicos: dos peticiones concurrentes pueden chocar contra
        // el indice unico. La restriccion de la base es la garantia real; aqui solo se traduce.
        if (error instanceof DataIntegrityViolationException) {
            return build(HttpStatus.CONFLICT, TechnologyConstants.NAME_ALREADY_EXISTS);
        }
        log.error("Unhandled error while processing request", error);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR);
    }

    private Mono<ServerResponse> build(HttpStatus status, String message) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ApiErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}
