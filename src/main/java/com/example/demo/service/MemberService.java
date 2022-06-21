package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AuthorityEntity;
import com.example.demo.model.MemberAuthorEntity;
import com.example.demo.model.MemberEntity;
import com.example.demo.repos.AuthorityRepository;
import com.example.demo.repos.MemberAuthorRepository;
import com.example.demo.repos.MemberRepository;
import com.example.demo.util.AuthorEnum;
import com.example.demo.util.CheckPassword;
import com.example.demo.util.HandleParamToMap;
import com.example.demo.vo.MemberVo;
import com.example.demo.vo.MemberEditVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MemberService {

	@Autowired
	private HandleParamToMap handleParam;

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberAuthorRepository memberAuthorRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@PersistenceContext
    EntityManager em;

	@Resource
	private CheckPassword checkPassword;

	private BCryptPasswordEncoder passwordEncoder;

	public MemberService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	/**
	 * 
	 * @查詢帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * @apiNote 查詢全部帳號資料
	 * 
	 */
	public Map<String, Object> queryMember(HttpServletRequest request) {

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
		List<MemberVo> memberDto = dataList.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());

		// 返回結果
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("total", total);
		resultMap.put("rows", memberDto);
		return resultMap;
	}

	/**
	 * 
	 * @新增帳號
	 * @Date 2022/05/27
	 * @author sharz
	 * @throws JsonProcessingException 
	 * 
	 */
	@Transactional
	public Map<String, Object> addMember(@RequestParam Map<String, Object> param) throws JsonProcessingException  {

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        MemberEditVo dto = mapper.readValue(jsonString, MemberEditVo.class);
        
		// params參數組合
		String account_name = dto.getAccountName();
		String member_name = dto.getMemberName();
		String password = dto.getPassword();
		AuthorityEntity authority = authorityRepository.findById(getAuthorId(dto.getAuthor())).get();
//		int author = getAuthorId(dto.getAuthor());
		
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
			entity.setAuthorityEntity(authority);
			
			MemberAuthorEntity memberAuthor = new MemberAuthorEntity();
			memberAuthor.setAuthorityEntity(authority);
			
			entity.addMemberAuthor(memberAuthor);
			memberRepository.save(entity);
			
		}catch(RuntimeException e) {
			resultMap.put("errMsg", "新增失敗");
		}
		
		return resultMap;
	}

	@Transactional
	public Map<String, Object> editMember(@RequestParam Map<String, Object> param) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(param);
        MemberEditVo dto = mapper.readValue(jsonString, MemberEditVo.class);
        
        MemberEntity entity = findMember("accountName",dto.getAccountName()).get(0);
        
		// params參數組合
		String member_name = dto.getMemberName();
		String password = dto.getPassword();
		AuthorityEntity authority = authorityRepository.findById(getAuthorId(dto.getAuthor())).get();
//		int author = getAuthorId(dto.getAuthor());

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

		List<MemberEntity> existedMemberName = findMember("memberName",member_name);
		
		if(!existedMemberName.isEmpty()) {
			if (entity.getId() != existedMemberName.get(0).getId()) {
				resultMap.put("errMsg", "姓名已存在，請重新輸入");
				return resultMap;
			}
		}

		// 密碼加密
		password = passwordEncoder.encode(password);

		entity.setUpdateTime(new Date());
		entity.setAuthorityEntity(authority);
		entity.setMemberName(member_name);
		entity.setPassword(password);
		
		MemberAuthorEntity memberAuthor = findMemberAuthor("memberId",entity.getId()).get(0);
		memberAuthor.setAuthorityEntity(authority);
		memberAuthor.setMemberEntity(entity);
		memberRepository.save(entity);
		
		return resultMap;
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
	
	public List<MemberEntity> findMember(String column,String value) {
		
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
	
	public List<MemberAuthorEntity> findMemberAuthor(String column,int value) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
	    //select Member
	    CriteriaQuery<MemberAuthorEntity> query = cb.createQuery(MemberAuthorEntity.class);
	    
	    //from member
	    Root<MemberAuthorEntity> memberEntityRoot = query.from(MemberAuthorEntity.class);
	    
	    //where accountName = :accountName
	    Predicate predName = cb.equal(memberEntityRoot.get(column), value);
        query.where(predName);
        
        TypedQuery<MemberAuthorEntity> memberEntity = em.createQuery(query);
        return memberEntity.getResultList();
	}
	
	public MemberVo convertToDto(MemberEntity entity) {
		MemberVo dto = new MemberVo();
		dto.setAccountName(entity.getAccountName());
		dto.setMemberName(entity.getMemberName());
		dto.setAddTime(entity.getAddTime());
		dto.setPassword(entity.getPassword());
		dto.setAuthor(AuthorEnum.getAuthor(entity.getAuthorityEntity().getId()));
		return dto;
	}
}
