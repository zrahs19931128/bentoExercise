package com.example.demo.controller;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.BentoService;
import com.example.demo.vo.BentoSearchVo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("/bento")
public class BentoController {

	@Autowired
	private BentoService bentoService;

	@RequestMapping("/bentoMenu")
	public String page(Model model) {
		return "bentoMenu";
	}

	/** 
	 * @查詢全部便當
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws ParseException 
	 * @throws JsonProcessingException 
	 * 
	 */
	@RequestMapping("queryBento")
	@ResponseBody
	public Map<String, Object> queryBento(HttpServletRequest request) throws ParseException {
		// 返回結果
		return bentoService.queryBento(request);
	}
	
	/**
	 * 
	 * @新增便當
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@ResponseBody
	@PutMapping("addBento")
	public Map<String, Object> addMember(@RequestParam Map<String, Object> param) throws JsonProcessingException {
		
		return bentoService.addBento(param);
	}
	
	/**
	 * 
	 * @編輯便當
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@ResponseBody
	@PutMapping("editBento")
	public Map<String, Object> editBento(@RequestParam Map<String, Object> param) throws JsonProcessingException {
		
		return bentoService.editBento(param);
	}
}
