package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entitiy.BentoEntity;
import com.example.demo.repository.BentoRepository;
import com.example.demo.util.HandleParamToMap;

@Service
public class BentoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private BentoRepository bentoRepository;

	@Autowired
	private HandleParamToMap handleParam;

	public Map<String, Object> queryBento(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page-1, pageSize,Sort.Direction.DESC,"addTime");
		Page<BentoEntity> pageList = bentoRepository.findAll(pageable);
		
		//將數據轉為List
		List<BentoEntity> dataList = pageList.getContent();
				
		// 帳號總數
		Integer total = (int)pageList.getTotalElements();
		
		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", dataList);

		return resultMap;
	}
}
