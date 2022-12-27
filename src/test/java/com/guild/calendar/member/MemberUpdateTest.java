package com.guild.calendar.member;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.dto.MemberForm;
import com.guild.calendar.repository.MemberRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberUpdateTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	MemberForm createMember() {
		MemberForm memberForm = new MemberForm();
		
		memberForm.setEmail("test@naver.com");
		memberForm.setUsername("홍길동");
		memberForm.setPassword("test1234");
		
		return memberForm;
				
	}	
	
	
	@DisplayName("회원 정보 수정 테스트")
	@Test
	@Transactional
	void signupTest() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		MemberForm memberForm = createMember();
	
		String requestJson = objectMapper.writeValueAsString(memberForm);
		
		
		mockMvc.perform(
				post("/member/update").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
