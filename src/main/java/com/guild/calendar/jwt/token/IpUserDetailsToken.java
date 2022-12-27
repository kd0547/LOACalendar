package com.guild.calendar.jwt.token;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import com.guild.calendar.dto.UserDetail;

public class IpUserDetailsToken extends UserDetailsToken{
	
	
	
	private List<String> ipAdressList;

	public IpUserDetailsToken() {
		super();
	}
	public IpUserDetailsToken(UserDetail userDetail, String accessToken, String refreshToken, String key) {
		super(userDetail, accessToken, refreshToken, key);
		
		this.ipAdressList = new ArrayList<String>();
	}
	
	public void addIpList(String ip) {
		ipAdressList.add(ip);
	}
	
	public boolean hasIpAdress(String ip) {
		for(String ipAdress : ipAdressList) {
			if(ip.equals(ipAdress)) {
				return true;
			}
		}
		return false;
	}
	public List<String> getIpAdressList() {
		return ipAdressList;
	}
	
	public void setIpAdressList(List<String> ipAdressList) {
		this.ipAdressList = ipAdressList;
	}
	
	
	
}
