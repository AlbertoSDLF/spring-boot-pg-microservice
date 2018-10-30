package com.asitc.pgmicroservice.controller.error;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.asitc.pgmicroservice.controller.error.model.ErrorResponseDTO;

@Component
public class ErrorResponseHelper {

    private final Map<String, String> errors = new HashMap<>();

    public String getDefaultMessage(final HttpStatus errorStatus) {
        return this.errors.get("[" + errorStatus.value() + "].default");
    }

    public String getMessage(final HttpStatus errorStatus, final String errorName) {
        return this.errors.get("[" + errorStatus.value() + "]." + errorName);
    }

    public String getDefaultMessage(final Integer errorStatusCode) {
        return this.getDefaultMessage(HttpStatus.valueOf(errorStatusCode));
    }

    public String getMessage(final Integer errorStatusCode, final String errorName) {
        return this.getMessage(HttpStatus.valueOf(errorStatusCode), errorName);
    }

    public ErrorResponseDTO getDefaultErrorResponse(final HttpStatus errorStatusCode) {
        final ErrorResponseDTO error = new ErrorResponseDTO();
        error.setHttpCode(errorStatusCode.value());
        error.setHttpMessage(errorStatusCode.getReasonPhrase());
        error.setMoreInformation(this.getDefaultMessage(errorStatusCode));
        return error;
    }

    public ErrorResponseDTO getDefaultErrorResponse(final Integer errorStatus) {
        return this.getDefaultErrorResponse(HttpStatus.valueOf(errorStatus));
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void initialize() {
        final Map<String, Object> entries = this.getYamlFactory().getObject();
        for (final Map.Entry<String, Object> entry : entries.entrySet()) {
            final Map<String, Object> customMessageMap = (Map<String, Object>) entry.getValue();
            for (final Map.Entry<String, Object> customMessageMapEntry : customMessageMap.entrySet()) {
                this.errors.put(entry.getKey() + "." + customMessageMapEntry.getKey(),
                        (String) customMessageMapEntry.getValue());
            }
        }
    }

    private YamlMapFactoryBean getYamlFactory() {
        final YamlMapFactoryBean factory = new YamlMapFactoryBean();
        factory.setResources(new ClassPathResource("errorMessages.yml"));
        return factory;
    }

}
