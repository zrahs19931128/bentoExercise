package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitiy.NavigationMenuEntity;
import com.example.demo.repository.NavigationMenuRepository;

@Service
public class NavigationMenuService {

	@Autowired
	private NavigationMenuRepository navigationMenuRepository;
	
	public List<NavigationMenuEntity> queryMenu(){
		return null;
	}
}
