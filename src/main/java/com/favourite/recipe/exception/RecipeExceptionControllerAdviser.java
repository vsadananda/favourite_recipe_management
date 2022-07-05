package com.favourite.recipe.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@RestControllerAdvice
public class RecipeExceptionControllerAdviser extends ResponseEntityExceptionHandler {

	static final Log LOG = LogFactory.getLog(RecipeExceptionControllerAdviser.class);

	@ExceptionHandler(RecipeBadRequestException.class)
	public ResponseEntity<ErrorDetail> exceptionHandlerBadRequestEx(RecipeBadRequestException e,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ErrorDetail errorResponse = new ErrorDetail();
		errorResponse.setError(e.getError());
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(400);
        LOG.error("Recipe Bad Request Expection: " + e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	}

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetail error = new ErrorDetail();
        if (ex.getCause() instanceof InvalidFormatException || ex.getCause() instanceof JsonParseException
                || ex.getCause() instanceof MismatchedInputException) {
            error.setError("E4003");
            error.setMessage("Invalid request input");
        } else {
            error.setMessage(ex.getMessage());
        }
        error.setStatus(400);
        LOG.error("FM Http Message Not Readable Exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


	@ExceptionHandler(RecipeInternalException.class)
	public ResponseEntity<ErrorDetail> exceptionHandlerInternalEx(RecipeInternalException e) {
		ErrorDetail errorResponse = new ErrorDetail();
		errorResponse.setError(e.getError());
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(500);
        LOG.error("Recipe Internal Expection: " + e.getMessage(), e);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(RecipeNotFoundException.class)
	public ResponseEntity<ErrorDetail> exceptionHandlerNotFoundEx(RecipeNotFoundException e) {
		ErrorDetail errorResponse = new ErrorDetail();
		errorResponse.setError(e.getError());
		errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(404);
        LOG.error("Recipe Not found Expection: " + e.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}



	@ExceptionHandler(RecipeInvalidInputException.class)
	public ResponseEntity<ErrorDetail> exceptionHandlerInvalidInputEx(RecipeInvalidInputException e) {
		ErrorDetail errorResponse = new ErrorDetail();
		errorResponse.setError(e.getError());
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(422);
		LOG.error("UnprocessableEntity Expection:" + e.getMessage(), e);
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}


    @ExceptionHandler(RecipeException.class)
	public ResponseEntity<ErrorDetail> exceptionHandlerInternalAll(Exception e, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ErrorDetail errorResponse = new ErrorDetail();
		errorResponse.setError("Unknown");
		errorResponse.setMessage("Something went wrong. Please try again.");
		errorResponse.setStatus(500);
        LOG.error("Recipe Internal Expection: " + e.getMessage(), e);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}





}
