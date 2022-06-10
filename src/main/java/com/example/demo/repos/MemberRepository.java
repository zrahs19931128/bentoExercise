package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

}
