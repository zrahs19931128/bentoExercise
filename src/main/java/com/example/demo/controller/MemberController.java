package com.example.demo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.MemberService;
import com.example.demo.util.CheckPassword;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Resource
	private CheckPassword checkPassword;

	@RequestMapping("/memberList")
	public String page(Model model) {
		return "member";
	}

	/**
	 * 
	 * @查詢帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * @apiNote 查詢全部帳號資料
	 * 
	 */
	@RequestMapping("queryMembers")
	@ResponseBody
	public Map<String, Object> queryMembers(HttpServletRequest request) {
		return memberService.queryMember(request);
	}

	/**
	 * 
	 * @新增帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
//	@PutMapping("addMember")
	@ResponseBody
	@PutMapping("addMember")
	public Map<String, Object> addMember(@RequestParam Map<String, Object> param) throws JsonProcessingException {
		
		Map<String, Object> addMember = memberService.addMember(param);
		return addMember;
	}

	/**
	 * 
	 * @更新帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@PutMapping("editMember")
	@ResponseBody
	public Map<String, Object> editMember(@RequestParam Map<String, Object> param) throws JsonProcessingException {
		Map<String, Object> editMember = memberService.editMember(param);
		return editMember;
	}
}
