package com.example.demo.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.config.CheckPassword;
import com.example.demo.config.HandleParamToMap;
import com.example.demo.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private HandleParamToMap handleParam;

	@Autowired
	private MemberService memberService;

	@Resource
	private CheckPassword checkPassword;

//	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@RequestMapping("/memberList")
	public String page(Model model) {
		return "member";
	}

	/*
	 * 
	 * 查詢全部帳號資料
	 * 
	 */
	@RequestMapping("queryMembers")
	@ResponseBody
	public Map<String, Object> queryMembers(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		// 帳號總數
		Integer total = memberService.queryAllMember();
		List rows = memberService.queryMember(page, pageSize);

		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", rows);

		return resultMap;
	}

	/**
	 * 
	 * @新增帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * 
	 */
	@PutMapping("addMember")
	@ResponseBody
	public Map<String, Object> addMember(HttpServletRequest request) {
		
		Map<String, Object> addMember = memberService.addMember(request);
		return addMember;
	}

	/**
	 * 
	 * @更新帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * 
	 */
	@PutMapping("editMember")
	@ResponseBody
	public Map<String, Object> editMember(HttpServletRequest request) {
		Map<String, Object> editMember = memberService.editMember(request);
		return editMember;
	}
}
