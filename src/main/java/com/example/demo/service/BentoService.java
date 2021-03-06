package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.BentoEntity;
import com.example.demo.model.MemberEntity;
import com.example.demo.model.OrderDetailEntity;
import com.example.demo.model.OrderListEntity;
import com.example.demo.repos.BentoRepository;
import com.example.demo.repos.OrderListRepository;
import com.example.demo.util.HandleParamToMap;
import com.example.demo.util.OrderNumber;
import com.example.demo.vo.BentoVo;
import com.example.demo.vo.BentoEditVo;
import com.example.demo.vo.BentoOrderEditVo;
import com.example.demo.vo.BentoSearchVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BentoService {
	
	private SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	@Autowired
	private BentoRepository bentoRepository;

	@Autowired
	private HandleParamToMap handleParam;
	
	@PersistenceContext
    EntityManager em;

	public Map<String, Object> queryBento(HttpServletRequest request) throws ParseException {

		// request????????????
		Map<String, Object> params = handleParam.handleParamToMap(request);
		String productName = (String)params.get("productName");
		String startTime = (String)params.get("startTime");
		String endTime = (String)params.get("endTime");
		String shelfStatus = (String)params.get("shelfStatus");
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page-1, pageSize,Sort.Direction.DESC,"addTime");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<BentoEntity> query = cb.createQuery(BentoEntity.class);
	    
	    //from member
	    Root<BentoEntity> bentoEntityRoot = query.from(BentoEntity.class);
	    
		//where accountName = :accountName
	    List<Predicate> predWhere = new ArrayList<Predicate>();
	    if( productName !=null && productName.length() > 0 ) {
	    	predWhere.add(cb.like(bentoEntityRoot.get("productName"),"%"+productName+"%"));
	    }
	    
	    if( startTime !=null && startTime.length() > 0 ) {
	    	Date startDate = timeFormat.parse(startTime+" 00:00:00");
	    	predWhere.add(cb.greaterThanOrEqualTo(bentoEntityRoot.get("addTime"), startDate));
	    }
	    
	    if( endTime !=null && endTime.length() > 0) {
	    	Date endDate = timeFormat.parse(endTime+" 23:59:59");
	    	predWhere.add(cb.lessThanOrEqualTo(bentoEntityRoot.get("addTime"), endDate));
	    }
	    
	    if( shelfStatus !=null && shelfStatus.length() > 0) {
	    	predWhere.add(cb.equal(bentoEntityRoot.get("shelfStatus"), shelfStatus));
	    }
	    
	    if(!predWhere.isEmpty()) {	    	
	    	query.where(predWhere.toArray(new Predicate[] {}));
	    }
	    
        Order orderByIdDesc = cb.desc(bentoEntityRoot.get("addTime"));
        query.orderBy(orderByIdDesc);
        
	    TypedQuery<BentoEntity> bentoEntity = em.createQuery(query);
        bentoEntity.setFirstResult((page-1)*pageSize);
        bentoEntity.setMaxResults(pageSize);
        
        List<BentoEntity> searchList = em.createQuery(query).getResultList();
		
		Page<BentoEntity> pageList = new PageImpl<BentoEntity>(bentoEntity.getResultList(),pageable,searchList.size());
		
		//???????????????List
		List<BentoEntity> dataList = pageList.getContent();
				
		//???entity??????DTO
		List<BentoVo> bentoDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		
		// ????????????
		Integer total = (int)pageList.getTotalElements();
		
		// ????????????
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", bentoDto);

		return resultMap;
	}
	
	/**
	 * 
	 * @????????????
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@Transactional
	public Map<String, Object> addBento(@RequestParam Map<String, Object> param) throws JsonProcessingException  {

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        BentoEditVo dto = mapper.readValue(jsonString, BentoEditVo.class);
        
		// params????????????
		String productName = dto.getProductName();
		int price = dto.getPrice();
		int status = dto.getShelfStatus();
		int sellDay = dto.getSellDay();
		int sellPerson = dto.getSellPerson();
		
		// ????????????
		Map<String, Object> resultMap = new HashMap();

		List existedBento = findBento("productName",productName);
		if (!existedBento.isEmpty()) {
			resultMap.put("errMsg", "?????????????????????????????????");
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
			resultMap.put("errMsg", "????????????");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @????????????
	 * @Date 2022/06/08
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@Transactional
	public Map<String, Object> editBento(@RequestParam Map<String, Object> param) throws JsonProcessingException  {
		
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        BentoEditVo dto = mapper.readValue(jsonString, BentoEditVo.class);
        
		// params????????????
		String productName = dto.getProductName();
		int menuId= dto.getMenuId();
		int price = dto.getPrice();
		int status = dto.getShelfStatus();
		int sellDay = dto.getSellDay();
		int sellPerson = dto.getSellPerson();
		
		// ????????????
		Map<String, Object> resultMap = new HashMap();

		List<BentoEntity> existedBento = findBento("productName",productName);
		BentoEntity entity = bentoRepository.findById(menuId).get();
		
		if (!existedBento.isEmpty()) {
			if(existedBento.get(0).getMenuId() != entity.getMenuId()) {
				resultMap.put("errMsg", "?????????????????????????????????");
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
			resultMap.put("errMsg", "????????????");
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @??????????????????
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public Map<String, Object> queryOnShelfBento(HttpServletRequest request) {

		// request????????????
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page-1, pageSize,Sort.Direction.DESC,"addTime");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<BentoEntity> query = cb.createQuery(BentoEntity.class);
	    
	    //from member
	    Root<BentoEntity> bentoEntityRoot = query.from(BentoEntity.class);
	    
	    //where column = :value
	    Predicate predWhere = cb.equal(bentoEntityRoot.get("shelfStatus"), 1);
        query.where(predWhere);
        
        Order orderByIdDesc = cb.desc(bentoEntityRoot.get("addTime"));
        query.orderBy(orderByIdDesc);
        
        TypedQuery<BentoEntity> bentoEntity = em.createQuery(query);
        bentoEntity.setFirstResult((page-1)*pageSize);
        bentoEntity.setMaxResults(pageSize);
        
		List<BentoEntity> onShelfList = em.createQuery(query).getResultList();
		
		Page<BentoEntity> pageList = new PageImpl<BentoEntity>(bentoEntity.getResultList(),pageable,onShelfList.size());
		
		//???????????????List
		List<BentoEntity> dataList = pageList.getContent();
				
		//???entity??????DTO
		List<BentoVo> bentoDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		
		// ????????????
		Integer total = (int)pageList.getTotalElements();
		
		// ????????????
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", bentoDto);

		return resultMap;
	}
	
	/**
	 * 
	 * @????????????(?????????)
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public List<BentoEntity> findBento(String column,String value) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<BentoEntity> query = cb.createQuery(BentoEntity.class);
	    
	    //from member
	    Root<BentoEntity> bentoEntityRoot = query.from(BentoEntity.class);
	    
	    //where column = :value
	    Predicate predWhere = cb.equal(bentoEntityRoot.get(column), value);
        query.where(predWhere);
        
        TypedQuery<BentoEntity> bentoEntity = em.createQuery(query);
        return bentoEntity.getResultList();
	}
	
	public BentoVo convertToDto(BentoEntity entity) {
		BentoVo dto = new BentoVo();
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
