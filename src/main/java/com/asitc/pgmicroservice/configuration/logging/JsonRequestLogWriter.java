package com.asitc.pgmicroservice.configuration.logging;

import com.asitc.pgmicroservice.configuration.logging.model.RequestLog;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequestLogWriter extends RequestLogWriter {

    private final ObjectMapper objectMapper;

    public JsonRequestLogWriter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Override
    protected String getLogLine(final RequestLog requestLog) {
        try {
            return this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestLog);
        } catch (final JsonProcessingException e) {
            return "<< Unable to generate JSON >>";
        }
    }

}
