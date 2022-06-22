package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.service.BentoUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	BentoUserDetailsService bentoUserDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(bentoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeRequests() // 認證機制設置
				.anyRequest().authenticated() // 除了以上的 URL 外, 都需要認證才可以訪問
				.and().formLogin()
//		    .loginPage("/login") // 認證頁面指向頁頁
				.defaultSuccessUrl("/purchase/orderBento").permitAll().and().logout().permitAll();
		// disable page caching
		httpSecurity.headers().frameOptions().sameOrigin().cacheControl();
		httpSecurity.csrf().disable();
	}
}