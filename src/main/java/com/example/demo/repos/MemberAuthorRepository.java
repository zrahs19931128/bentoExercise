package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MemberAuthorEntity;

public interface MemberAuthorRepository extends JpaRepository<MemberAuthorEntity, Integer>{

}
