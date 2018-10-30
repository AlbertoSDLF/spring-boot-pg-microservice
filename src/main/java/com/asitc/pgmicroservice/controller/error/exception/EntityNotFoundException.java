package com.asitc.pgmicroservice.controller.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class EntityNotFoundException extends HttpStatusCodeException {

    private static final long serialVersionUID = 1L;
    private final String entityName;
    private final String id;

    public EntityNotFoundException(final String entityName, final String id) {
        super(HttpStatus.NOT_FOUND);
        this.entityName = entityName;
        this.id = id;
    }

}
