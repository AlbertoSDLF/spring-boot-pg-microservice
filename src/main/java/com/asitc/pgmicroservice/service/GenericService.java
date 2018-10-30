package com.asitc.pgmicroservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.util.CollectionUtils;

import com.asitc.pgmicroservice.controller.error.exception.ValidationErrorException;

public class GenericService<T> {
    private final Validator validator;

    protected GenericService() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    protected void validateEntity(final T entity) {
        final Set<ConstraintViolation<T>> validationErrors = this.validator.validate(entity);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            final Iterator<ConstraintViolation<T>> validationErrorIterator = validationErrors.iterator();
            final Collection<String> validationErrorMessages = new ArrayList<>();
            while (validationErrorIterator.hasNext()) {
                final ConstraintViolation<T> validationError = validationErrorIterator.next();
                validationErrorMessages
                        .add(String.format("%s %s", validationError.getPropertyPath(), validationError.getMessage()));
            }
            throw new ValidationErrorException(validationErrorMessages);
        }
    }

}
