package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.model.AuthorMenuEntity;
import com.example.demo.model.AuthorityEntity;
import com.example.demo.model.BentoEntity;
import com.example.demo.model.MemberAuthorEntity;
import com.example.demo.model.MemberEntity;
import com.example.demo.model.NavigationMenuEntity;
import com.example.demo.model.OrderDetailEntity;
import com.example.demo.model.OrderListEntity;
import com.example.demo.repos.NavigationMenuRepository;
import com.example.demo.vo.NavigationMenuVo;

@Service
public class NavigationMenuService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NavigationMenuRepository navigationMenuRepository;
	
	@PersistenceContext
    EntityManager em;
	
	public List<NavigationMenuVo> queryMenu(){
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<NavigationMenuEntity> query = cb.createQuery(NavigationMenuEntity.class);
	    
	    //from member
	    Root<NavigationMenuEntity> navigationMenuRoot = query.from(NavigationMenuEntity.class);
	    Join<AuthorMenuEntity,NavigationMenuEntity> navigationJoin = navigationMenuRoot.join("authorMenuEntity", JoinType.LEFT);
	    Join<AuthorityEntity,AuthorMenuEntity> authorJoin = navigationJoin.join("authorityEntity",JoinType.LEFT);
	    Join<MemberAuthorEntity,AuthorityEntity> memberAuthorJoin = authorJoin.join("memberAuthorEntity",JoinType.LEFT);
	    Join<MemberEntity,MemberAuthorEntity> memberJoin = memberAuthorJoin.join("memberEntity",JoinType.LEFT);
	    
	    //where accountName = :accountName
	    Predicate predWhere = cb.equal(memberJoin.get("accountName"), username);
	    query.where(predWhere);
	    
	    TypedQuery<NavigationMenuEntity> detailEntity = em.createQuery(query);
	    List<NavigationMenuEntity> dataList = detailEntity.getResultList();
	    
//		List<NavigationMenuEntity> dataList = jdbcTemplate.query(
//						"select nm.* from navigation_menu nm \n" 
//						+ "left join author_menu am on am.navigation_id = nm.id \n"
//						+ "left join authority a on a.id = am.author_id \n"
//						+ "left join member_author ma on ma.author_id = a.id \n"
//						+ "left join member m on m.id = ma.member_id \n" + "where m.account_name  = ?",
//				new Object[] { username },
//				new BeanPropertyRowMapper<NavigationMenuEntity>(NavigationMenuEntity.class));
		System.out.println("");
		List<NavigationMenuVo> menuDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		
		return menuDto;
	}
	
	public NavigationMenuVo convertToDto (NavigationMenuEntity entity){
		NavigationMenuVo dto = new NavigationMenuVo();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setUrl(entity.getUrl());
		return dto;
		
	}
}
