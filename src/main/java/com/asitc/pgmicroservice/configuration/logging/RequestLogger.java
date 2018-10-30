package com.asitc.pgmicroservice.configuration.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import com.asitc.pgmicroservice.configuration.logging.model.RequestLog;
import com.asitc.pgmicroservice.configuration.logging.model.ResponseLog;
import com.asitc.pgmicroservice.util.Constants;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@Component
@ConditionalOnProperty(value = "requestLogger.enabled", havingValue = "true", matchIfMissing = false)
public class RequestLogger extends AbstractRequestLoggingFilter {

    @Value("${requestLogger.excluded}")
    private String[] excludedPaths;
    @Value("${requestLogger.level:ERROR}")
    private String level;
    @Autowired
    private RequestLogWriter logWriter;

    @PostConstruct
    private void initialize() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("requestLogger").setLevel(Level.valueOf(this.level));
    }

    @Override
    protected void beforeRequest(final HttpServletRequest request, final String message) {
        /* Not used. All logging is made when the request has completed */
    }

    @Override
    protected void afterRequest(final HttpServletRequest request, final String message) {
        final UUID requestId = UUID.randomUUID();
        final RequestLog requestLog = new RequestLog();
        requestLog.setClientHost(request.getRemoteHost());
        requestLog.setCookies(this.getCookies(request));
        requestLog.setHeaders(this.getHeaders(request));
        requestLog.setId(requestId.toString());
        requestLog.setMethod(request.getMethod());
        requestLog.setParameters(this.getParameters(request));
        requestLog.setUri(request.getRequestURI());
        final HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        final ResponseLog responseLog = new ResponseLog();
        final HttpStatus responseHttpStatus = HttpStatus.valueOf(response.getStatus());
        responseLog.setStatus(String.format("%s %s", response.getStatus(), responseHttpStatus.getReasonPhrase()));
        final String forwardedError = (String) request.getAttribute(Constants.Request.ERROR);
        if (forwardedError != null) {
            responseLog.setDetails(forwardedError);
        }
        requestLog.setResponse(responseLog);
        final Exception exception = (Exception) request.getAttribute(Constants.Request.EXCEPTION);
        this.logWriter.log(requestLog, exception);
    }

    @Override
    protected boolean shouldLog(final HttpServletRequest request) {
        final PathMatcher pathMatcher = new AntPathMatcher();
        if (this.excludedPaths != null) {
            for (final String excludedPath : this.excludedPaths) {
                if (pathMatcher.match(excludedPath, request.getServletPath())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Map<String, Collection<String>> getParameters(final HttpServletRequest request) {
        final Map<String, Collection<String>> parameters = new HashMap<>();
        final Map<String, String[]> requestParameterMap = request.getParameterMap();
        for (final Map.Entry<String, String[]> parameter : requestParameterMap.entrySet()) {
            parameters.put(parameter.getKey(), new ArrayList<>(Arrays.asList(parameter.getValue())));
        }
        return parameters;
    }

    private Map<String, Collection<String>> getHeaders(final HttpServletRequest request) {
        final Map<String, Collection<String>> headers = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String header = headerNames.nextElement();
            headers.put(header, Collections.list(request.getHeaders(header)));
        }
        return headers;
    }

    private Map<String, String> getCookies(final HttpServletRequest request) {
        final Map<String, String> cookies = new HashMap<>();
        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), String.format("%s=%s;%s;%s;%s", cookie.getName(), cookie.getValue(),
                        cookie.getDomain(), cookie.getPath(), cookie.getSecure()));
            }
        }
        return cookies;
    }

}
