package com.asitc.pgmicroservice.configuration.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asitc.pgmicroservice.configuration.logging.model.RequestLog;

public abstract class RequestLogWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger("requestLogger");

    protected abstract String getLogLine(final RequestLog requestLog);

    public void log(final RequestLog requestLog, final Exception ex) {
        if (requestLog != null) {
            if (requestLog.getResponse().isError()) {
                RequestLogWriter.LOGGER.error(this.getLogLine(requestLog));
            } else {
                RequestLogWriter.LOGGER.info(this.getLogLine(requestLog));
            }
        }
        if (ex != null) {
            RequestLogWriter.LOGGER.error("Request error: ", ex);
        }
    }

}
