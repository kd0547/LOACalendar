package com.guild.calendar.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guild.calendar.constant.LegionRaid;
import com.guild.calendar.constant.LoaClass;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.RaidPlanRequestDto;

@SpringBootTest
@AutoConfigureMockMvc
public class RaidPlanControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void viewPlanTest() throws Exception {
		mockMvc.perform(get("/plan/2/calendar/3").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				
				)
				.andDo(print())
				.andExpect(status().isOk())
				;
	}
	
	
	
	@Test
	void createPlanTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		RaidPlanRequestDto raidPlanRequestDto = createPlan();
		String requestBody = objectMapper.writeValueAsString(raidPlanRequestDto);
		
		/*
		mockMvc.perform(post("/plan/calendar/3").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isCreated())
				;
		*/
	}
	
	@Test
	void PatchUpdatePlanFailTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		RaidPlanRequestDto raidPlanRequestDto = createPlan();
		List<GuildUserDto> dtos = createGuildUser2();
		
		raidPlanRequestDto.setGuildUser(dtos);
		
		String requestBody = objectMapper.writeValueAsString(raidPlanRequestDto);
		mockMvc.perform(patch("/plan/2/calendar/3").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	void deletePlanTest() throws Exception {
		mockMvc.perform(delete("/plan/2/calendar/4").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MjM4Mzg4NywiZXhwIjoxNjc0OTc1ODg3LCJqdGkiOiIzOTRkNGU4MS05ZjY0LTQ4ZmMtYjlkZS0zZGQwNzA1ZjNlNWQifQ.2lKWCF1RxTzwJAw7zk8CzbvbkQyqVCcIa92JYF3zld4")
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	
	
	
	public RaidPlanRequestDto createPlan() {
		RaidPlanRequestDto raidPlanRequestDto = new RaidPlanRequestDto();
		
		LocalDate date = LocalDate.of(2023, 1, 5);
		LocalTime time = LocalTime.of(10, 25);
		
		
		raidPlanRequestDto.setStartDate(date);
		raidPlanRequestDto.setStartTime(time);
		raidPlanRequestDto.setRaid(LegionRaid.KOUKUSATON);
		raidPlanRequestDto.setGuildUser(createGuildUser());
		
		return raidPlanRequestDto;
	}
	public List<GuildUserDto> createGuildUser() {
		List<GuildUserDto> dtos = new ArrayList<GuildUserDto>();
		
		GuildUserDto guildUserDto1 = new GuildUserDto();
		GuildUserDto guildUserDto2 = new GuildUserDto();
		GuildUserDto guildUserDto3 = new GuildUserDto();
		GuildUserDto guildUserDto4 = new GuildUserDto();
		
		guildUserDto1.setId(3L);
		guildUserDto1.setUsername("연어맛김");
		guildUserDto1.setLoaClass(LoaClass.BARD);
		guildUserDto1.setLevel(1560);
		
		guildUserDto2.setId(6L);
		guildUserDto2.setUsername("사보텐");
		guildUserDto2.setLoaClass(LoaClass.WARDANCER);
		guildUserDto2.setLevel(1596);
		
		guildUserDto3.setId(8L);
		guildUserDto3.setUsername("민트돈가스");
		guildUserDto3.setLoaClass(LoaClass.DESTROYER);
		guildUserDto3.setLevel(1601);
		
		guildUserDto4.setId(9L);
		guildUserDto4.setUsername("기상균");
		guildUserDto4.setLoaClass(LoaClass.AEROMANCER);
		guildUserDto4.setLevel(1580);
		
		
		
		
		dtos.add(guildUserDto1);
		dtos.add(guildUserDto2);
		dtos.add(guildUserDto3);
		dtos.add(guildUserDto4);
		
		return dtos;
	}
	public List<GuildUserDto> createGuildUser2() {
		List<GuildUserDto> dtos = new ArrayList<GuildUserDto>();
		
		GuildUserDto guildUserDto1 = new GuildUserDto();
		GuildUserDto guildUserDto2 = new GuildUserDto();
		GuildUserDto guildUserDto3 = new GuildUserDto();
		GuildUserDto guildUserDto4 = new GuildUserDto();
		
		guildUserDto1.setId(7L);
		guildUserDto1.setUsername("누군가는해야하잖아요");
		guildUserDto1.setLoaClass(LoaClass.PAINTER);
		guildUserDto1.setLevel(1580);
		
		guildUserDto2.setId(10L);
		guildUserDto2.setUsername("밤잔나비");
		guildUserDto2.setLoaClass(LoaClass.WARDANCER);
		guildUserDto2.setLevel(1520);
		
		guildUserDto3.setId(8L);
		guildUserDto3.setUsername("민트돈가스");
		guildUserDto3.setLoaClass(LoaClass.DESTROYER);
		guildUserDto3.setLevel(1601);
		/*
		guildUserDto4.setId(9L);
		guildUserDto4.setUsername("기상균");
		guildUserDto4.setLoaClass(LoaClass.AEROMANCER);
		guildUserDto4.setLevel(1580);
		*/
		
		
		
		dtos.add(guildUserDto1);
		dtos.add(guildUserDto2);
		dtos.add(guildUserDto3);
		dtos.add(guildUserDto4);
		
		return dtos;
	}
}
