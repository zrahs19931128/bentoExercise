package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entitiy.BentoEntity;
import com.example.demo.util.HandleParamToMap;

@Service
public class BentoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private HandleParamToMap handleParam;

	public Map<String, Object> queryBento(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		// 帳號總數
		Integer total = jdbcTemplate.queryForObject("select count(*)from bento_menu", Integer.class);

		List<BentoEntity> rows = jdbcTemplate.query("select * from bento_menu order by add_time desc limit ?,?",
				new Object[] { (page - 1) * pageSize, pageSize },
				new BeanPropertyRowMapper<BentoEntity>(BentoEntity.class));
		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", rows);

		return resultMap;
	}
}
