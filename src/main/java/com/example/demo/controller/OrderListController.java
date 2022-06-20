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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.OrderService;
import com.example.demo.vo.BentoOrderEditVo;
import com.example.demo.vo.OrderDetailVo;

@Controller
public class OrderListController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/orderList")
	public String page(Model model) {
		return "orderList";
	}
	
	/** 
	 * @查詢訂單
	 * @Date 2022/06/17
	 * @author sharz
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping("queryOrderList")
	@ResponseBody
	public Map<String, Object> queryOrderList(HttpServletRequest request) throws ParseException {
		// 返回結果
		return orderService.queryOrderList(request);
	}
	
	/** 
	 * @查詢訂單明細
	 * @Date 2022/06/20
	 * @author sharz
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping("queryOrderListDetail")
	@ResponseBody
	public List<OrderDetailVo> queryOrderListDetail(@RequestParam("orderId") int orderId) throws ParseException {
		// 返回結果
		return orderService.queryOrderListDetail(orderId);
	}
}
