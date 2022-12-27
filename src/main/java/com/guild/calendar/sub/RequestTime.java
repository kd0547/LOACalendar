package com.guild.calendar.sub;


import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestTime {
	
	private String uuid;
	
	private Long startTime;
	private Long endTime;
	
	
	public void logTest(String message) {
		System.out.println("["+uuid+"]"+message);
	}
	
	
	@PostConstruct
	public void startTime() {
		this.uuid = UUID.randomUUID().toString();
		startTime = System.currentTimeMillis();
		
		System.out.println("시작시간 : "+startTime);
	}
	@PreDestroy
	public void endTime() {
		endTime = System.currentTimeMillis();
		
		Long result = (endTime - startTime);
		System.out.println("시간 : "+ result+" ms");
	}
	
}
