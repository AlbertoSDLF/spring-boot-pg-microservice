package com.asitc.pgmicroservice.controller.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asitc.pgmicroservice.controller.error.model.ErrorResponseDTO;

@Controller
public class ServletErrorController implements ErrorController {

    @Autowired
    private ErrorResponseHelper errorResponseHelper;

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ErrorResponseDTO> handleCustomError(final HttpServletRequest request) {
        final Integer errorStatusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final ErrorResponseDTO error = this.errorResponseHelper.getDefaultErrorResponse(
                errorStatusCode != null ? errorStatusCode : HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getHttpCode()));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
