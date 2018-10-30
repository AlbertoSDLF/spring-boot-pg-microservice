package com.asitc.pgmicroservice.controller.error.exception;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ValidationErrorException extends HttpStatusCodeException {

    private static final long serialVersionUID = 1L;
    private final String moreInformation;

    public ValidationErrorException(final Collection<String> validationErrorMessages) {
        super(HttpStatus.BAD_REQUEST);
        final StringBuilder moreInformationSb = new StringBuilder("");
        final Iterator<String> validationErrorMessageIterator = validationErrorMessages.iterator();
        while (validationErrorMessageIterator.hasNext()) {
            final String validationErrorMessage = validationErrorMessageIterator.next();
            moreInformationSb.append(validationErrorMessage);
            if (validationErrorMessageIterator.hasNext()) {
                moreInformationSb.append(" | ");
            }
        }
        this.moreInformation = moreInformationSb.toString();
    }

}
