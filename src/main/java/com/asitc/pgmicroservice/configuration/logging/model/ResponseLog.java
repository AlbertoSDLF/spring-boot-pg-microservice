package com.asitc.pgmicroservice.configuration.logging.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseLog {

    private String status;
    private String details;

    public boolean isError() {
        final HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(this.status.substring(0, 3)));
        return httpStatus.is4xxClientError() || httpStatus.is5xxServerError();
    }

}
