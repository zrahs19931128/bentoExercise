package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.NavigationMenuEntity;

@Repository
public interface NavigationMenuRepository extends JpaRepository<NavigationMenuEntity, Integer>{

}
