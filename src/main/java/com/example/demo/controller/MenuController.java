package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entitiy.NavigationMenuEntity;

@Controller
@RequestMapping("menu")
public class MenuController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@RequestMapping("queryMenu")
//	@ResponseBody
//	public List queryMenu() {
//
//		List<NavigationMenuEntity> menu = jdbcTemplate.query("select * from navigation_menu",
//				new BeanPropertyRowMapper<NavigationMenuEntity>(NavigationMenuEntity.class));
//		return menu;
//	}

	@RequestMapping("queryMenu")
	@ResponseBody
	public List queryMenu() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		List<NavigationMenuEntity> menu = jdbcTemplate.query(
				"select nm.* from navigation_menu nm \n" + "left join author_menu am on am.navigation_id = nm.id \n"
						+ "left join authority a on a.id = am.author_id \n"
						+ "left join member_author ma on ma.author_id = a.id \n"
						+ "left join member m on m.id = ma.member_id \n" + "where m.account_name  = ?",
				new Object[] { username },
				new BeanPropertyRowMapper<NavigationMenuEntity>(NavigationMenuEntity.class));
		return menu;
	}
}
