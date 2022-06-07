package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entitiy.MemberEntity;

@Service
public class BentoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {
			MemberEntity member = memberService.queryMember(username);
			
//			List<SimpleGrantedAuthority> authorities = member.getAuthorities().stream()
//	                .map(auth -> new SimpleGrantedAuthority(auth.name()))
//	                .collect(Collectors.toList());
			
			return new User(member.getAccount_name(),member.getPassword(), Collections.emptyList());
      } catch (Exception e) {
          throw new UsernameNotFoundException("Username is wrong.");
      }
	}
}
