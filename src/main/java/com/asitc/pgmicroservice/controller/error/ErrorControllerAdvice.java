package com.asitc.pgmicroservice.controller.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.asitc.pgmicroservice.controller.error.exception.EntityNotFoundException;
import com.asitc.pgmicroservice.controller.error.exception.ValidationErrorException;
import com.asitc.pgmicroservice.controller.error.model.ErrorResponseDTO;
import com.asitc.pgmicroservice.util.Constants;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @Autowired
    private ErrorResponseHelper errorResponseHelper;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(final EntityNotFoundException ex,
            final HttpServletRequest request) {
        final ErrorResponseDTO error = new ErrorResponseDTO();
        error.setHttpCode(ex.getRawStatusCode());
        error.setHttpMessage(ex.getStatusCode().getReasonPhrase());
        error.setMoreInformation(String.format(
                this.errorResponseHelper.getMessage(ex.getStatusCode(), "entityNotFound"),
                ex.getEntityName(), ex.getId()));
        request.setAttribute(Constants.Request.ERROR, error.getMoreInformation());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(final ValidationErrorException ex,
            final HttpServletRequest request) {
        final ErrorResponseDTO error = new ErrorResponseDTO();
        error.setHttpCode(ex.getRawStatusCode());
        error.setHttpMessage(ex.getStatusCode().getReasonPhrase());
        error.setMoreInformation(ex.getMoreInformation());
        request.setAttribute(Constants.Request.ERROR, error.getMoreInformation());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(final HttpRequestMethodNotSupportedException ex,
            final HttpServletRequest request) {
        final ErrorResponseDTO error = new ErrorResponseDTO();
        error.setHttpCode(HttpStatus.METHOD_NOT_ALLOWED.value());
        error.setHttpMessage(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        error.setMoreInformation(ex.getMessage());
        request.setAttribute(Constants.Request.ERROR, error.getMoreInformation());
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class,
            HttpMediaTypeNotSupportedException.class, MissingServletRequestParameterException.class })
    public ResponseEntity<ErrorResponseDTO> handle400Exception(final Exception ex, final HttpServletRequest request) {
        String message = this.errorResponseHelper.getDefaultMessage(HttpStatus.BAD_REQUEST);
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = String.format(this.errorResponseHelper.getMessage(HttpStatus.BAD_REQUEST, "wrongContentType"),
                    request.getContentType());
        }
        final ErrorResponseDTO error = new ErrorResponseDTO();
        error.setHttpCode(HttpStatus.BAD_REQUEST.value());
        error.setHttpMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setMoreInformation(message);
        request.setAttribute(Constants.Request.ERROR, error.getMoreInformation());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(final Exception ex, final HttpServletRequest request) {
        final ErrorResponseDTO error = this.errorResponseHelper.getDefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        request.setAttribute(Constants.Request.ERROR, error.getMoreInformation());
        request.setAttribute(Constants.Request.EXCEPTION, ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
