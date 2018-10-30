package com.asitc.pgmicroservice.configuration.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asitc.pgmicroservice.configuration.logging.model.RequestLog;

@Configuration
public class RequestLoggerConfiguration {

    @Bean
    @ConditionalOnProperty(value = "requestLogger.format", havingValue = "json", matchIfMissing = false)
    public RequestLogWriter getJsonLogger() {
        return new JsonRequestLogWriter();
    }

    @Bean
    @ConditionalOnMissingBean(RequestLogWriter.class)
    public RequestLogWriter getBasicLogger() {
        return new RequestLogWriter() {
            @Override
            protected String getLogLine(final RequestLog requestLog) {
                return requestLog.toString();
            }
        };
    }

}
