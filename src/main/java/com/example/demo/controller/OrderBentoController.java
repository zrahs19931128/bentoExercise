package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderBentoController {

	@RequestMapping("/orderBento")
	public String page(Model model) {
		return "orderBento";
	}
}
