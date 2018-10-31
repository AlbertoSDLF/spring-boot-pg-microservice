package com.asitc.pgmicroservice.configuration.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.iterators.EnumerationIterator;
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
		final Map<String, Collection<String>> parameters = request.getParameterMap().entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, e -> new ArrayList<>(Arrays.asList(e.getValue()))));
		return parameters;
	}

	private Map<String, Collection<String>> getHeaders(final HttpServletRequest request) {
		final Iterator<String> iterator = new EnumerationIterator(request.getHeaderNames());
		Stream<String> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
				false);
		final Map<String, Collection<String>> headers = stream
				.collect(Collectors.toMap(Function.identity(), h -> Collections.list(request.getHeaders(h))));
		return headers;
	}

	private Map<String, String> getCookies(final HttpServletRequest request) {
		final Map<String, String> cookies = new HashMap<>();
		if (request.getCookies() != null) {
			Arrays.stream(request.getCookies()).forEach(c -> cookies.put(c.getName(), String.format("%s=%s;%s;%s;%s",
					c.getName(), c.getValue(), c.getDomain(), c.getPath(), c.getSecure())));
		}
		return cookies;
	}

}
