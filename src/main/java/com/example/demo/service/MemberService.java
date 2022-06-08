package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.BentoDto;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberEditDto;
import com.example.demo.entitiy.AuthorityEntity;
import com.example.demo.entitiy.BentoEntity;
import com.example.demo.entitiy.MemberEntity;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.AuthorEnum;
import com.example.demo.util.CheckPassword;
import com.example.demo.util.HandleParamToMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MemberService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private HandleParamToMap handleParam;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private MemberRepository memberRepository;
	
	@PersistenceContext
    EntityManager em;

	@Resource
	private CheckPassword checkPassword;

	private BCryptPasswordEncoder passwordEncoder;

	public MemberService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public Map<String, Object> queryMember(HttpServletRequest request) {

//		List<MemberEntity> menu = jdbcTemplate.query("select * from member order by add_time desc limit ?,?",
//				new Object[] { (page-1)*pageSize, pageSize }, new BeanPropertyRowMapper<MemberEntity>(MemberEntity.class));

		// request參數組合
		Map<String, Object> params = handleParam.handleParamToMap(request);
		int page = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("rows").toString());

		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "addTime");
		Page<MemberEntity> pageList = memberRepository.findAll(pageable);

		// 取得數據總量
		Integer total = (int) pageList.getTotalElements();

		// 將數據轉為List
		List<MemberEntity> dataList = pageList.getContent();

		// 將entity轉為DTO
		List<MemberDto> memberDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());

		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", memberDto);
		return resultMap;
	}

	public MemberEntity queryMember(String accountName) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<MemberEntity> query = cb.createQuery(MemberEntity.class);
	    
	    //from member
	    Root<MemberEntity> memberEntityRoot = query.from(MemberEntity.class);
	    
	    //where accountName = :accountName
	    Predicate predName = cb.equal(memberEntityRoot.get("accountName"), accountName);
        query.where(predName);
        
        TypedQuery<MemberEntity> memberEntity = em.createQuery(query);
        
		return memberEntity.getSingleResult();
	}

	public Map<String, Object> addMember(@RequestParam Map<String, Object> param) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        MemberEditDto dto = mapper.readValue(jsonString, MemberEditDto.class);
        
		// request參數組合
		String account_name = dto.getAccountName();
		String member_name = dto.getMemberName();
		String password = dto.getPassword();
		String author = dto.getAuthor();
		
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

//		List existedAccountName = jdbcTemplate.queryForList("select * from member where account_name = ? ",account_name);
//		List existedMemberName = jdbcTemplate.queryForList("select * from member where member_name = ?", member_name);
		
		List existedAccountName = findMember("accountName",account_name);
		if (!existedAccountName.isEmpty()) {
			resultMap.put("errMsg", "帳號已存在，請重新輸入");
			return resultMap;
		}

		List existedMemberName = findMember("memberName",member_name);
		if (!existedMemberName.isEmpty()) {
			resultMap.put("errMsg", "姓名已存在，請重新輸入");
			return resultMap;
		}

		// 密碼加密
		password = passwordEncoder.encode(password);
		

		try {
			MemberEntity entity = new MemberEntity();
			entity.setAccountName(account_name);
			entity.setMemberName(member_name);
			entity.setPassword(password);
			entity.setAddTime(new Date());
			entity.setAuthorId(getAuthorId(author));
			memberRepository.save(entity);
		}catch(Exception e) {
			resultMap.put("errMsg", "新增失敗");
		}
		
//		params.put("password", password);
//
//		params.put("add_time", new Date());
//
//		int isAdmin = Integer.valueOf(params.get("isAdmin").toString());
//		params.put("isAdmin", isAdmin);
//
//		String author = AuthorEnum.getAuthor(isAdmin);
//
//		int newMember = namedParameterJdbcTemplate.update(
//				"insert into member (account_name , member_name, password, isAdmin , add_time) value (:account_name, :member_name, :password, :isAdmin, :add_time)",
//				params);
//		if (newMember != 1) {
//			resultMap.put("errMsg", "新增失敗");
//		}
		return resultMap;
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

	public int getAuthorId(String author) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select 
	    CriteriaQuery<AuthorityEntity> query = cb.createQuery(AuthorityEntity.class);
	    
	    //from 
	    Root<AuthorityEntity> authorEntityRoot = query.from(AuthorityEntity.class);
	    
	    //where 
	    Predicate predName = cb.equal(authorEntityRoot.get("name"), author);
        query.where(predName);
        
        AuthorityEntity authorEntity = em.createQuery(query).getSingleResult();
        
        return authorEntity.getId();
	}
	
	public List findMember(String column,String value) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<MemberEntity> query = cb.createQuery(MemberEntity.class);
	    
	    //from member
	    Root<MemberEntity> memberEntityRoot = query.from(MemberEntity.class);
	    
	    //where accountName = :accountName
	    Predicate predName = cb.equal(memberEntityRoot.get(column), value);
        query.where(predName);
        
        TypedQuery<MemberEntity> memberEntity = em.createQuery(query);
        return memberEntity.getResultList();
	}
	
	public MemberDto convertToDto(MemberEntity entity) {
		MemberDto dto = new MemberDto();
		dto.setAccountName(entity.getAccountName());
		dto.setMemberName(entity.getMemberName());
		dto.setAddTime(entity.getAddTime());
		return dto;
	}
}
