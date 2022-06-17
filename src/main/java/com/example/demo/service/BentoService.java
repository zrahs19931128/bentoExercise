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
	
	private Date startTime(){
		Calendar startTime = Calendar.getInstance();
		startTime.set( Calendar.HOUR_OF_DAY,00);
		startTime.set( Calendar.MINUTE, 0);
		startTime.set( Calendar.SECOND,0);
		return startTime.getTime();
	}
	
	private Date endTime(){
		Calendar endTime = Calendar.getInstance();
		endTime.set( Calendar.HOUR_OF_DAY,23);
		endTime.set( Calendar.MINUTE, 59);
		endTime.set( Calendar.SECOND,59);
		return endTime.getTime();
	}
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BentoRepository bentoRepository;
	
	@Autowired
	private OrderListRepository orderListRepository;

	@Autowired
	private HandleParamToMap handleParam;
	
	@Autowired
	private OrderNumber orderNumber;
	
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
		List<BentoVo> bentoDto = dataList.stream()
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
        BentoEditVo dto = mapper.readValue(jsonString, BentoEditVo.class);
        
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
        BentoEditVo dto = mapper.readValue(jsonString, BentoEditVo.class);
        
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
	
	/**
	 * 
	 * @新增訂購便當
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws ParseException 
	 * 
	 */
	@Transactional
	public Map<String, Object> addOrderBento(@RequestBody List<BentoOrderEditVo> param) throws  ParseException  {

		Date date = new Date();
		Map<String, Object> resultMap = new HashMap();
		//至少選取一項
		if(param.isEmpty()) {
			resultMap.put("errMsg", "請至少勾選一品項");
			return resultMap;
		}
		
		//訂單
		OrderListEntity orderList = new OrderListEntity();
		
		//訂單明細
		List<OrderDetailEntity> detaiList = new ArrayList<>();
		
		//總金額
		int totalPrice = 0;
		
		//資料驗證
		for(BentoOrderEditVo element : param) {
			
			OrderDetailEntity detailEntity = new OrderDetailEntity();
			
			//有選取的項目的「訂購數量」不可小於等於 0
			if(element.getOrderCount() <= 0) {
				resultMap.put("errMsg", element.getProductName() + " 訂購數量不可為 0 或空值" + "，請重新輸入" );
				return resultMap;
			}
			
			//每一個品項的「訂購數量」不可超過該品項的「每人可訂購上限數量」
			if(element.getOrderCount() > element.getSellPerson()) {
				resultMap.put("errMsg", "訂購數量已超過 " + element.getProductName() + " 每人可訂購數量，請重新輸入" );
				return resultMap;
			}
			
			//每一個品項的「訂購數量」不可超過該品項的「每日可販賣總數量」
			Integer orderedBentoCount = findOrderBentoCount(element);
			if((orderedBentoCount + element.getOrderCount()) > element.getSellDay()) {
				resultMap.put("errMsg", "訂購數量已超過 " + element.getProductName() + " 每日可訂購數量，剩餘數量：" + (element.getSellDay() - orderedBentoCount)+"，請重新輸入" );
				return resultMap;
			}
			
			int detailOrderPrice = element.getPrice()*element.getOrderCount();
			detailEntity.setMenuId(element.getMenuId());
			detailEntity.setOrderCount(element.getOrderCount());
			detailEntity.setOrderTime(date);
			detailEntity.setTotalPrice(detailOrderPrice);
			
			totalPrice += detailOrderPrice;
			
			orderList.addOrderDetail(detailEntity);
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		MemberEntity member = memberService.findMember("accountName",username).get(0);
		orderList.setOrderAccount(member.getAccountName());
		orderList.setOrderName(member.getMemberName());
		orderList.setTotalPrice(totalPrice);
		orderList.setOrderTime(date);
		orderList.setOrderNumber(orderNumber.getCurrentOrderNumber(date));
		orderListRepository.save(orderList);
		
		// 返回結果
		return resultMap;
	}
	
	/**
	 * 
	 * @進階查詢便當
	 * @Date 2022/06/15
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public Map<String, Object> searchBento(BentoSearchVo param) throws ParseException{
		
		String productName = param.getProductName();
		String startTime = param.getStartTime();
		String endTime = param.getEndTime();
		String shelfStatus = param.getShelfStatus();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<BentoEntity> query = cb.createQuery(BentoEntity.class);
	    
	    //from member
	    Root<BentoEntity> bentoEntityRoot = query.from(BentoEntity.class);
	    
	    //where accountName = :accountName
	    List<Predicate> predWhere = new ArrayList<Predicate>();
	    if(productName.length() > 0 ) {
	    	predWhere.add(cb.like(bentoEntityRoot.get("productName"),"%"+productName+"%"));
	    }
	    
	    if(startTime.length() > 0 ) {
	    	Date startDate = timeFormat.parse(startTime+" 00:00:00");
	    	predWhere.add(cb.greaterThanOrEqualTo(bentoEntityRoot.get("addTime"), startDate));
	    }
	    
	    if(endTime.length() > 0) {
	    	Date endDate = timeFormat.parse(endTime+" 23:59:59");
	    	predWhere.add(cb.lessThanOrEqualTo(bentoEntityRoot.get("addTime"), endDate));
	    }
	    
	    if(shelfStatus.length() > 0) {
	    	predWhere.add(cb.equal(bentoEntityRoot.get("shelfStatus"), shelfStatus));
	    }
	    
	    Order orderByIdDesc = cb.desc(bentoEntityRoot.get("addTime"));
	    
        query.where(predWhere.toArray(new Predicate[] {}));
        query.orderBy(orderByIdDesc);
        
        TypedQuery<BentoEntity> bentoEntity = em.createQuery(query);
        
        List<BentoVo> bentoDto = bentoEntity.getResultList().stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
        
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("total", bentoDto.size());
        resultMap.put("rows", bentoDto);
        
		return resultMap;
	}
	
	/**
	 * 
	 * @查詢上架便當
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public Map<String, Object> queryOnShelfBento(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page-1, pageSize,Sort.Direction.DESC,"addTime");
		List<BentoEntity> onShelfList = findBento("shelfStatus","1");
		Page<BentoEntity> pageList = new PageImpl<BentoEntity>(onShelfList,pageable,onShelfList.size());
		
		//將數據轉為List
		List<BentoEntity> dataList = pageList.getContent();
				
		//將entity轉為DTO
		List<BentoVo> bentoDto = dataList.stream()
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
	 * @查詢便當(自定義)
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
	
	/**
	 * 
	 * @查詢已訂購便當數量
	 * @Date 2022/06/14
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public Integer findOrderBentoCount(BentoOrderEditVo param) throws ParseException{
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
	    
	    //from member
	    Root<OrderDetailEntity> orderDetailEntityRoot = query.from(OrderDetailEntity.class);
	    
	    CriteriaBuilder.Coalesce<Integer> coalesce = cb.coalesce();
	    coalesce.value(orderDetailEntityRoot.<Integer>get("orderCount"));
	    query.select(cb.sum(coalesce));
	    //where accountName = :accountName
	    List<Predicate> predWhere = new ArrayList<Predicate>();
	    
	    predWhere.add(cb.greaterThanOrEqualTo(orderDetailEntityRoot.get("orderTime"), startTime()));
	    predWhere.add(cb.lessThanOrEqualTo(orderDetailEntityRoot.get("orderTime"), endTime()));
	    predWhere.add(cb.equal(orderDetailEntityRoot.get("menuId"), param.getMenuId()));
	    
        query.where(predWhere.toArray(new Predicate[] {}));
        
        TypedQuery<Integer> bentoEntity = em.createQuery(query);
        Integer count = bentoEntity.getSingleResult();
        if(count == null) {
        	return 0;
        }
        
		return count;
		
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
