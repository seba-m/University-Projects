package com.pruebas.controlador.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorWebAdmin {
	@GetMapping("/admin")
	public String adminController(Model model) {
		return "admin";
	}
}
