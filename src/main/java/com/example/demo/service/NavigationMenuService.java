package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NavigationMenuDto;
import com.example.demo.entitiy.NavigationMenuEntity;
import com.example.demo.repository.NavigationMenuRepository;

@Service
public class NavigationMenuService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NavigationMenuRepository navigationMenuRepository;
	
	public List<NavigationMenuDto> queryMenu(){
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		List<NavigationMenuEntity> dataList = jdbcTemplate.query(
				"select nm.* from navigation_menu nm \n" + "left join author_menu am on am.navigation_id = nm.id \n"
						+ "left join authority a on a.id = am.author_id \n"
						+ "left join member_author ma on ma.author_id = a.id \n"
						+ "left join member m on m.id = ma.member_id \n" + "where m.account_name  = ?",
				new Object[] { username },
				new BeanPropertyRowMapper<NavigationMenuEntity>(NavigationMenuEntity.class));
		
		List<NavigationMenuDto> menuDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		
		return menuDto;
	}
	
	public NavigationMenuDto convertToDto (NavigationMenuEntity entity){
		NavigationMenuDto dto = new NavigationMenuDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setUrl(entity.getUrl());
		return dto;
		
	}
}
