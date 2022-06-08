package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitiy.NavigationMenuEntity;

@Repository
public interface NavigationMenuRepository extends JpaRepository<NavigationMenuEntity, Integer>{

}
