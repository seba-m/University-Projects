package com.pruebas.controlador.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorWebIndex {

	private static final String INDEX = "index";

	@GetMapping("/")
	public String indexEmpty(Model model) {
		return INDEX;
	}

	@GetMapping("/index")
	public String indexWithIndex(Model model) {
		return INDEX;
	}

	@GetMapping("/index.html")
	public String indexWithIndexHtml(Model model) {
		return INDEX;
	}

	@GetMapping("/home")
	public String indexWithHome(Model model) {
		return INDEX;
	}

	@GetMapping("/home.html")
	public String indexWithHomeHtml(Model model) {
		return INDEX;
	}
}