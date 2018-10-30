package com.asitc.pgmicroservice.configuration.logging.model;

import java.util.Collection;
import java.util.Map;

import lombok.Data;

@Data
public class RequestLog {

    private String id;
    private String method;
    private String uri;
    private Map<String, Collection<String>> parameters;
    private Map<String, Collection<String>> headers;
    private Map<String, String> cookies;
    private ResponseLog response;
    private String clientHost;

}
