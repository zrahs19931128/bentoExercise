package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.MemberEntity;

@Service
public class BentoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {
			MemberEntity member = memberService.findMember("accountName",username).get(0);
			
			List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
			SimpleGrantedAuthority role =new SimpleGrantedAuthority(member.getAuthorityEntity().getName());
			authorities.add(role);
			return new User(member.getAccountName(),member.getPassword(), authorities);
      } catch (Exception e) {
          throw new UsernameNotFoundException("Username is wrong.");
      }
	}
}
