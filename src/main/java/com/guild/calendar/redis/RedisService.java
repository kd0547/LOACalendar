package com.guild.calendar.redis;

public interface RedisService {
	public void setData(String key, Object value);
	public <T> Object getData(String key, Class<T> classes);
	public boolean deleteData(String key);
	public String findData(String key);
}
