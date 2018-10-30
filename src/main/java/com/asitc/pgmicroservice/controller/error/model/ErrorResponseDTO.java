package com.asitc.pgmicroservice.controller.error.model;

import lombok.Data;

@Data
public class ErrorResponseDTO {

    private Integer httpCode;
    private String httpMessage;
    private String moreInformation;

}
