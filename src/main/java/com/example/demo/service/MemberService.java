package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entitiy.MemberEntity;
import com.example.demo.util.AuthorEnum;
import com.example.demo.util.CheckPassword;
import com.example.demo.util.HandleParamToMap;

@Service
public class MemberService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private HandleParamToMap handleParam;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Resource
	private CheckPassword checkPassword;

	private BCryptPasswordEncoder passwordEncoder;

	public MemberService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public int queryAllMember() {

		Integer total = jdbcTemplate.queryForObject("select count(*)from member", Integer.class);
		return total;
	}

	public List queryMember(int page, int pageSize) {

		List<MemberEntity> menu = jdbcTemplate.query("select * from member order by add_time desc limit ?,?",
				new Object[] { (page-1)*pageSize, pageSize }, new BeanPropertyRowMapper<MemberEntity>(MemberEntity.class));
		return menu;
	}

	public MemberEntity queryMember(String accountName) {

		MemberEntity member = jdbcTemplate.queryForObject("select * from member where account_name = ? ",
				new Object[] { accountName }, new BeanPropertyRowMapper<MemberEntity>(MemberEntity.class));
		return member;
	}

	public Map<String, Object> addMember(HttpServletRequest request) {

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		String account_name = params.get("account_name").toString();
		String member_name = params.get("member_name").toString();
		String password = params.get("password").toString();

		// 返回結果
		Map<String, Object> resultMap = new HashMap();

		// 資料驗證
		if (account_name.isEmpty()) {
			resultMap.put("errMsg", "帳號不得為空值");
			return resultMap;
		}

		if (member_name.isEmpty()) {
			resultMap.put("errMsg", "姓名不得為空值");
			return resultMap;
		}

		if (!checkPassword.checkPasswordRule(password)) {
			resultMap.put("errMsg", "密碼不得為空值且8碼以上的英數混合，且至少有一個大寫英文字母");
			return resultMap;
		}

		List existedAccountName = jdbcTemplate.queryForList("select * from member where account_name = ? ",
				account_name);
		List existedMemberName = jdbcTemplate.queryForList("select * from member where member_name = ?", member_name);

		if (!existedAccountName.isEmpty()) {
			resultMap.put("errMsg", "帳號已存在，請重新輸入");
			return resultMap;
		}

		if (!existedMemberName.isEmpty()) {
			resultMap.put("errMsg", "姓名已存在，請重新輸入");
			return resultMap;
		}

		// 密碼加密
		password = passwordEncoder.encode(password);
		params.put("password", password);

		params.put("add_time", new Date());

		int isAdmin = Integer.valueOf(params.get("isAdmin").toString());
		params.put("isAdmin", isAdmin);

		String author = AuthorEnum.getAuthor(isAdmin);
		
		int newMember = namedParameterJdbcTemplate.update(
				"insert into member (account_name , member_name, password, isAdmin , add_time) value (:account_name, :member_name, :password, :isAdmin, :add_time)",
				params);
		if (newMember != 1) {
			resultMap.put("errMsg", "新增失敗");
		}
		return params;
	}

	public Map<String, Object> editMember(HttpServletRequest request) {
		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		String member_name = params.get("member_name").toString();
		String password = params.get("password").toString();

		// 返回結果
		Map<String, Object> resultMap = new HashMap();

		// 資料驗證

		if (member_name.isEmpty()) {
			resultMap.put("errMsg", "姓名不得為空值");
			return resultMap;
		}

		if (!checkPassword.checkPasswordRule(password)) {
			resultMap.put("errMsg", "密碼不得為空值且8碼以上的英數混合，且至少有一個大寫英文字母");
			return resultMap;
		}

		List existedMemberName = jdbcTemplate.queryForList("select * from member where member_name = ?", member_name);

		if (!existedMemberName.isEmpty()) {
			resultMap.put("errMsg", "姓名已存在，請重新輸入");
			return resultMap;
		}

		// 密碼加密
		password = passwordEncoder.encode(password);
		params.put("password", password);

		int isAdmin = Integer.valueOf(params.get("isAdmin").toString());
		params.put("isAdmin", isAdmin);

		int newMember = jdbcTemplate.update(
				"update member set member_name = ?, password = ?, isAdmin = ?, update_time = ? where account_name = ? ",
				member_name, password, params.get("isAdmin"), new Date(), params.get("account_name"));
		if (newMember != 1) {
			resultMap.put("errMsg", "編輯失敗");
		}
		return params;
	}
}
