package com.pruebas.modelo.tags;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TagExceptionAdvice {
	@ResponseBody
	@ExceptionHandler(TagException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String tagException(TagException ex) {
		return ex.getMessage();
	}
}
