package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bentoExercise")
public class LoginController {

	private Authentication auth;

	@Autowired
	UserDetailsService userDetailsService;

	@RequestMapping("/login")
	public String loginForm(HttpServletRequest request, Model model) {

		auth = SecurityContextHolder.getContext().getAuthentication();

		if (!auth.getPrincipal().equals("anonymousUser")) {
			return "test_Login";
		}

		return "login";
	}

	@RequestMapping("/auth/home")
	public String authHome(Model model) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("username", auth.getName()).addAttribute("roles", auth.getAuthorities());
		return "member";
	}
}
