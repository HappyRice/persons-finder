package com.persons.finder.domain.utility;

import com.persons.finder.data.dto.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger LOGGER = LogManager.getLogger(ErrorHandlingControllerAdvice.class);

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

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody public ErrorResponse onMissingServletRequestParameterException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Missing request parameters").build();
	}

	@ExceptionHandler(HttpMediaTypeException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ResponseBody public ErrorResponse onHttpMediaTypeException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Unsupported media type").build();
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody public ErrorResponse onHttpMessageNotReadableException() {
		return ErrorResponse.builder().withSuccess(false).withMessage("Invalid payload").build();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody public ErrorResponse onAllOtherException(final Exception e) {
		LOGGER.error(e.getMessage(), e);

		return ErrorResponse.builder().withSuccess(false).withMessage("Internal server error").build();
	}
}
