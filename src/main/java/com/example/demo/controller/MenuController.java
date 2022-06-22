package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.NavigationMenuService;

@Controller
@RequestMapping("menu")
public class MenuController {
	
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
