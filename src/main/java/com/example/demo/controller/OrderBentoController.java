package com.example.demo.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.BentoService;
import com.example.demo.service.OrderService;
import com.example.demo.vo.BentoOrderEditVo;

@Controller
public class OrderBentoController {

	@Autowired
	private BentoService bentoService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/orderBento")
	public String page(Model model) {
		return "orderBento";
	}
	
	/** 
	 * @查詢上架便當
	 * @Date 2022/06/08
	 * @author sharz
	 * 
	 */
	@RequestMapping("queryOnShelfBento")
	@ResponseBody
	public Map<String, Object> queryBento(HttpServletRequest request) {
		// 返回結果
		return bentoService.queryOnShelfBento(request);
	}
	
	/** 
	 * @新增訂購便當
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping("addOrderBento")
	@ResponseBody
	public Map<String, Object> addOrderBento(@RequestBody List<BentoOrderEditVo> param) throws ParseException {
		// 返回結果
		return orderService.addOrderBento(param);
	}
}
