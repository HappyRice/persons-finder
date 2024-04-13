package com.persons.finder.domain.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.persons.finder.data.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody ErrorResponse onMethodArgumentNotValidException(final Exception e) {
		final BindingResult bindingResult;

		if (e instanceof MethodArgumentNotValidException) {
			bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
		} else {
			bindingResult = ((BindException) e).getBindingResult();
		}

		final StringBuilder stringBuilder = new StringBuilder();

		for (final FieldError fieldError : bindingResult.getFieldErrors()) {
			stringBuilder.append(fieldError.getDefaultMessage()).append(";");
		}

		return ErrorResponse.builder().withSuccess(false).withMessage(stringBuilder.toString()).build();
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody public ErrorResponse onHttpMessageNotReadableException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Payload must not be null").build();
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody public ErrorResponse onMissingServletRequestParameterException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Missing request parameters").build();
	}

	@ExceptionHandler(JsonProcessingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody public ErrorResponse onJsonProcessingException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Invalid JSON").build();
	}

	@ExceptionHandler(HttpMediaTypeException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ResponseBody public ErrorResponse onHttpMediaTypeException(final HttpMediaTypeException e) {
		return ErrorResponse.builder().withSuccess(false).withMessage(e.getMessage()).build();
	}
}
