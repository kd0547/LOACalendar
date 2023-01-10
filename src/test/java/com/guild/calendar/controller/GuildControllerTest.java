package com.guild.calendar.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.dto.GuildForm;

@SpringBootTest
@AutoConfigureMockMvc
public class GuildControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	
	@Test
	@Transactional
	void createGuild() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		GuildForm guildForm = new GuildForm();
		guildForm.setGuildName("죄송군단장");
		
		String guildRequest = objectMapper.writeValueAsString(guildForm);
		
		mockMvc.perform(post("/guild").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.contentType(MediaType.APPLICATION_JSON)
				.content(guildRequest))
		.andExpect(status().isCreated())
		.andDo(print());	
	}
	
	@Test
	@Transactional
	void duplicateCreateGuild() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		GuildForm guildForm = new GuildForm();
		guildForm.setGuildName("미안군단장");
		
		String guildRequest = objectMapper.writeValueAsString(guildForm);
		
		mockMvc.perform(post("/guild").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.contentType(MediaType.APPLICATION_JSON)
				.content(guildRequest))
		.andExpect(status().isCreated())
		.andDo(print());	
		
		mockMvc.perform(post("/guild").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.contentType(MediaType.APPLICATION_JSON)
				.content(guildRequest))
		.andExpect(status().isBadRequest())
		.andDo(print());	
	}
	@Test
	@Transactional
	void updateGuild() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		GuildForm guildForm = new GuildForm();
		guildForm.setId(16L);
		guildForm.setGuildName("안녕");
		
		String guildRequest = objectMapper.writeValueAsString(guildForm);
		
		mockMvc.perform(put("/guild/16").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.contentType(MediaType.APPLICATION_JSON)
				.content(guildRequest))
		.andExpect(status().isNoContent())
		.andDo(print());	
		
		
	}
}
