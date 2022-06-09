package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.BentoDto;
import com.example.demo.dto.BentoEditDto;
import com.example.demo.dto.MemberEditDto;
import com.example.demo.entitiy.BentoEntity;
import com.example.demo.entitiy.MemberEntity;
import com.example.demo.repository.BentoRepository;
import com.example.demo.util.HandleParamToMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BentoService {
	
	@Autowired
	private BentoRepository bentoRepository;

	@Autowired
	private HandleParamToMap handleParam;
	
	@PersistenceContext
    EntityManager em;

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
	
	/**
	 * 
	 * @新增便當
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@Transactional
	public Map<String, Object> addBento(@RequestParam Map<String, Object> param) throws JsonProcessingException  {

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        BentoEditDto dto = mapper.readValue(jsonString, BentoEditDto.class);
        
		// params參數組合
		String productName = dto.getProductName();
		int price = dto.getPrice();
		int status = dto.getShelfStatus();
		int sellDay = dto.getSellDay();
		int sellPerson = dto.getSellPerson();
		
		// 返回結果
		Map<String, Object> resultMap = new HashMap();

		List existedBento = findBento("productName",productName);
		if (!existedBento.isEmpty()) {
			resultMap.put("errMsg", "便當已存在，請重新輸入");
			return resultMap;
		}
		

		try {
			BentoEntity entity = new BentoEntity();
			entity.setProductName(productName);
			entity.setPrice(price);
			entity.setShelfStatus(status);
			entity.setSellDay(sellDay);
			entity.setSellPerson(sellPerson);
			entity.setAddTime(new Date());
			bentoRepository.save(entity);
			
		}catch(RuntimeException e) {
			resultMap.put("errMsg", "新增失敗");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @編輯便當
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@Transactional
	public Map<String, Object> editBento(@RequestParam Map<String, Object> param) throws JsonProcessingException  {
		
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        BentoEditDto dto = mapper.readValue(jsonString, BentoEditDto.class);
        
		// params參數組合
		String productName = dto.getProductName();
		int menuId= dto.getMenuId();
		int price = dto.getPrice();
		int status = dto.getShelfStatus();
		int sellDay = dto.getSellDay();
		int sellPerson = dto.getSellPerson();
		
		// 返回結果
		Map<String, Object> resultMap = new HashMap();

		List<BentoEntity> existedBento = findBento("productName",productName);
		BentoEntity entity = bentoRepository.findById(menuId).get();
		
		if (!existedBento.isEmpty()) {
			if(existedBento.get(0).getMenuId() != entity.getMenuId()) {
				resultMap.put("errMsg", "便當已存在，請重新輸入");
				return resultMap;
			}
		}
		

		try {
			entity.setProductName(productName);
			entity.setPrice(price);
			entity.setShelfStatus(status);
			entity.setSellDay(sellDay);
			entity.setSellPerson(sellPerson);
			entity.setUpdateTime(new Date());
			bentoRepository.save(entity);
			
		}catch(RuntimeException e) {
			resultMap.put("errMsg", "新增失敗");
		}
		
		return resultMap;
	}
	
	public Map<String, Object> searchBento(HttpServletRequest request){
		
		return null;
	}
	
	public List<BentoEntity> findBento(String column,String value) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<BentoEntity> query = cb.createQuery(BentoEntity.class);
	    
	    //from member
	    Root<BentoEntity> bentoEntityRoot = query.from(BentoEntity.class);
	    
	    //where accountName = :accountName
	    Predicate predName = cb.equal(bentoEntityRoot.get(column), value);
        query.where(predName);
        
        TypedQuery<BentoEntity> bentoEntity = em.createQuery(query);
        return bentoEntity.getResultList();
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
