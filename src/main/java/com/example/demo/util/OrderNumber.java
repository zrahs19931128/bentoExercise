package com.example.demo.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.example.demo.model.OrderListEntity;

@Service
public class OrderNumber {

	private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
	
	@PersistenceContext
    EntityManager em;
	
	public String getCurrentOrderNumber(Date date) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    CriteriaQuery<OrderListEntity> query = cb.createQuery(OrderListEntity.class);
	    
	    Root<OrderListEntity> orderListEntityRoot = query.from(OrderListEntity.class);
	    
	    Order orderByIdDesc = cb.desc(orderListEntityRoot.get("orderTime"));
	    
        query.orderBy(orderByIdDesc);
        
        TypedQuery<OrderListEntity> orderListEntity = em.createQuery(query).setMaxResults(1);
        List<OrderListEntity> entity = orderListEntity.getResultList();
        
        String today = timeFormat.format(date);
        
        if(entity.isEmpty()) {
        	return today+"00001";
        }
        
        long currentNumber = Long.valueOf(entity.get(0).getOrderNumber());
        String currentNumberDate = entity.get(0).getOrderNumber().substring(0, 8);
        
        if(today.equals(currentNumberDate)) {
        	
        	currentNumber = currentNumber+1;
        	
        	return String.valueOf(currentNumber);
        }
        
        return today+"00001";
	}
}
