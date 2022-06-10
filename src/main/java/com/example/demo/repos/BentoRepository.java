package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BentoEntity;

@Repository
public interface BentoRepository extends JpaRepository<BentoEntity,Integer>{

}
