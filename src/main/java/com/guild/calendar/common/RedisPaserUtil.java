package com.guild.calendar.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisPaserUtil {
	
	private ObjectMapper mapper;
	
	public RedisPaserUtil(ObjectMapper mapper) {
		this.mapper = mapper;
		this.mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	}
	
	public <T> String ObjectToJSON(T object) {
		
		if(object == null) {
			throw new NullPointerException("object 값이 null 입니다.");
		}
		
		try {
			
			return  mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			
			return null;
		}
		
	}
	
	public <T> Object JsonToObject(String json,Class<T> classes) {
		
		if(json == null || classes == null) {
			throw new NullPointerException("null 입니다.");
		}
		
		
		
		try {
			
			return  mapper.readValue(json, classes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
