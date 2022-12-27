package com.guild.calendar;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class LoaGuildCalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoaGuildCalendarApplication.class, args);
	}
}
