package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AuthorityEntity;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer>{

}
