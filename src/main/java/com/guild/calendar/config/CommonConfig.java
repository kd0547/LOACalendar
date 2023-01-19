package com.guild.calendar.config;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guild.calendar.security.RSAEncryption;

@Configuration
public class CommonConfig {
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		
		return objectMapper;
	}
	
	@Bean
	public Cipher cipher() {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance("RSA");
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cipher;
	}
	
	@Bean
	public RSAEncryption rsaEncryption() {
		
		return new RSAEncryption(cipher());
		
	}
	@Bean
	public KeyPair keyPair() {
		RSAEncryption rsaEncryption = rsaEncryption();
		
		KeyPair keyPair = rsaEncryption.genRSAkeyPair();
		
		return keyPair;
	}
	
	
}
