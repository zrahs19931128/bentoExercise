package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BentoDto;
import com.example.demo.entitiy.BentoEntity;
import com.example.demo.repository.BentoRepository;
import com.example.demo.util.HandleParamToMap;

@Service
public class BentoService {
	
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
				
		//將entity轉為DTO
		List<BentoDto> bentoDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		
		// 帳號總數
		Integer total = (int)pageList.getTotalElements();
		
		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", bentoDto);

		return resultMap;
	}
	
	public BentoDto convertToDto(BentoEntity entity) {
		BentoDto dto = new BentoDto();
		dto.setMenuId(entity.getMenuId());
		dto.setProductName(entity.getProductName());
		dto.setPrice(entity.getPrice());
		dto.setShelfStatus(entity.getShelfStatus());
		dto.setSellDay(entity.getSellDay());
		dto.setSellPerson(entity.getSellPerson());
		dto.setAddTime(entity.getAddTime());
		return dto;
	}
}
