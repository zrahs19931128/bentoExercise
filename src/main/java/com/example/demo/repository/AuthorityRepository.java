package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entitiy.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer>{

}
