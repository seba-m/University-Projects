package com.pruebas.modelo.configuraciones;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = MultipartException.class)
	public String handleFileUploadException(MultipartException mpex, HttpServletRequest request) {
		mpex.printStackTrace();

		return "redirect:/app/dashboard?error=file_too_big";
	}

	/*-@ExceptionHandler(value = MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request) {
		return "redirect:/app/account?error=file_too_big";
	}*/
}