package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.NavigationMenuEntity;
import com.example.demo.service.NavigationMenuService;
import com.example.demo.vo.BentoVo;
import com.example.demo.vo.NavigationMenuVo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("menu")
public class MenuController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NavigationMenuService navigationMenuService;

	/**
	 * 
	 * @讀取選單
	 * @Date 2022/05/27
	 * @author sharz
	 * 
	 */
	@RequestMapping("queryMenu")
	@ResponseBody
	public List queryMenu() {
		return navigationMenuService.queryMenu();
	}
}
