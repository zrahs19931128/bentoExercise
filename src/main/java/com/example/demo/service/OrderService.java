package com.example.demo.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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

import com.example.demo.model.BentoEntity;
import com.example.demo.model.MemberEntity;
import com.example.demo.model.OrderDetailEntity;
import com.example.demo.model.OrderListEntity;
import com.example.demo.repos.BentoRepository;
import com.example.demo.repos.OrderDetailRepository;
import com.example.demo.repos.OrderListRepository;
import com.example.demo.util.HandleParamToMap;
import com.example.demo.util.OrderNumber;
import com.example.demo.vo.BentoOrderEditVo;
import com.example.demo.vo.BentoVo;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderListVo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class OrderService {

	@PersistenceContext
    EntityManager em;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private OrderListRepository orderListRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private BentoRepository bentoRepository;
	
	@Autowired
	private OrderNumber orderNumber;
	
	@Autowired
	private HandleParamToMap handleParam;
	
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
			Optional<BentoEntity> bentoEntity = bentoRepository.findById(element.getMenuId());
			detailEntity.setBentoEntity(bentoEntity.get());
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
	    Optional<BentoEntity> bento = bentoRepository.findById(param.getMenuId());
	    predWhere.add(cb.greaterThanOrEqualTo(orderDetailEntityRoot.get("orderTime"), startTime()));
	    predWhere.add(cb.lessThanOrEqualTo(orderDetailEntityRoot.get("orderTime"), endTime()));
	    predWhere.add(cb.equal(orderDetailEntityRoot.get("bentoEntity"), bento.get()));
	    
        query.where(predWhere.toArray(new Predicate[] {}));
        
        TypedQuery<Integer> bentoEntity = em.createQuery(query);
        Integer count = bentoEntity.getSingleResult();
        if(count == null) {
        	return 0;
        }
        
		return count;
		
	}
	
	/**
	 * 
	 * @查詢訂單
	 * @Date 2022/06/17
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public Map<String, Object> queryOrderList(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page-1, pageSize,Sort.Direction.DESC,"orderTime");
		Page<OrderListEntity> pageList = orderListRepository.findAll(pageable);
		
		//將數據轉為List
		List<OrderListEntity> dataList = pageList.getContent();
				
		//將entity轉為DTO
		List<OrderListVo> bentoDto = dataList.stream()
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
	 * @查詢訂單
	 * @Date 2022/06/17
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	public List<OrderDetailVo> queryOrderListDetail(int orderId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<OrderDetailEntity> query = cb.createQuery(OrderDetailEntity.class);
	    
	    //from member
	    Root<OrderDetailEntity> detailEntityRoot = query.from(OrderDetailEntity.class);
	    Join<OrderDetailEntity,OrderListEntity> join = detailEntityRoot.join("orderListEntity");
	    
	    //where accountName = :accountName
	    Predicate predWhere = cb.equal(detailEntityRoot.get("orderId"), orderId);
	    query.where(predWhere);
	    
	    TypedQuery<OrderDetailEntity> detailEntity = em.createQuery(query);
	    List<OrderDetailEntity> dataList = detailEntity.getResultList();
//	    List<OrderDetailVo> orderDetailVo = dataList.stream()
//				.map(this::convertToDto)
//				.collect(Collectors.toList());

		return null;
	}
	
	public OrderListVo convertToDto(OrderListEntity entity) {
		OrderListVo dto = new OrderListVo();
		dto.setOrderId(entity.getOrderId());
		dto.setOrderAccount(entity.getOrderAccount());
		dto.setOrderName(entity.getOrderName());
		dto.setOrderNumber(entity.getOrderNumber());
		dto.setOrderTime(entity.getOrderTime());
		dto.setTotalPrice(entity.getTotalPrice());
		return dto;
	}
}
