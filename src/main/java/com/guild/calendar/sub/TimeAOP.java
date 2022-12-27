package com.guild.calendar.sub;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeAOP {
	
	@Around("@annotation(com.guild.calendar.Annotation.ServiceTimeLogger)")
	public Object executionAspect(ProceedingJoinPoint joinPoint ) throws Throwable {
		Long start = System.currentTimeMillis();
		
		Object result = joinPoint.proceed();
		Long end = System.currentTimeMillis();
		
		System.out.println("경과 시간 : "+((end-start))+"ms");
		
		return result;
		
	}
}
