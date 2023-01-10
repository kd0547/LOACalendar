package com.guild.calendar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.common.RedisPaserUtil;
import com.guild.calendar.jwt.token.JWToken;

@Primary
@Component
public class RedisServiceImpl implements RedisService{

	
	@Autowired
	private JWTokenRedisService jwTokenRedisService;
	
	private RedisPaserUtil paserUtil = new RedisPaserUtil(new ObjectMapper());
	
	
	
	@Override
	public void setData(String key, Object value) {
		if(key == null || value == null) {
			throw new NullPointerException();
		}
		
		String valueString = paserUtil.ObjectToJSON(value);
		
		jwTokenRedisService.setData(key, valueString);
		
	}

	@Override
	public <T> Object getData(String key,Class<T> classes) {
		String object = (String) jwTokenRedisService.getData(key, classes);
		
		if(object == null) {
			throw new IllegalStateException("-102");
		}
		
		
		return paserUtil.JsonToObject(object, classes);
	}

	@Override
	public boolean deleteData(String key) {
		boolean isDelete = jwTokenRedisService.deleteData(key);
		
		if(!isDelete) {
			throw new IllegalStateException("-102");
		}
		
		return  isDelete;
	}

}
