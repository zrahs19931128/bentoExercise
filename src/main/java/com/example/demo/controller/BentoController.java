package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.config.HandleParamToMap;
import com.example.demo.service.BentoService;

@Controller
public class BentoController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private HandleParamToMap handleParam;
	
	@Autowired
	private BentoService bentoService;

	@RequestMapping("/bentoMenu")
	public String page(Model model) {
		return "bentoMenu";
	}

	/*
	 * 
	 * 查詢全部帳號資料
	 * 
	 */
	@RequestMapping("queryBento")
	@ResponseBody
	public Map<String, Object> queryMembers(HttpServletRequest request) {
		// 返回結果
		Map<String, Object> resultMap = bentoService.queryBento(request);
		return resultMap;
	}
}
