package com.guild.calendar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class JWTokenRedisService implements RedisService{
	
	
	private final RedisTemplate<String, Object> redisTemplate;

	@Autowired
	JWTokenRedisService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	public void setData(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
		
	}
	
	
	@Override
	public boolean deleteData(String key) {
		return redisTemplate.delete(key);
	}


	@Override
	public <T> Object getData(String key, Class<T> classes) {
		
		return (String) redisTemplate.opsForValue().get(key);
	}

	@Override
	public String findData(String key) {
		
		return (String) redisTemplate.opsForValue().get(key);
	}
}
