package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OrderListEntity;

@Repository
public interface OrderListRepository extends JpaRepository<OrderListEntity, Integer>{

}
