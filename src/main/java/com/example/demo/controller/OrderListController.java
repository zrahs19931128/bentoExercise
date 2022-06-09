package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderListController {

	@RequestMapping("/orderList")
	public String page(Model model) {
		return "orderList";
	}
}
