package com.guild.calendar.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LoaClass {
	//전사
	DESTROYER("DESTROYER"),GUNLANCER("GUNLANCER"),BERSERKER("BERSERKER"),PALADIN("PALADIN"),
	//디스트로이어
	
	//무도가
	WARDANCER("WARDANCER"),SCRAPPER("SCRAPPER"),SOULFIST("SOULFIST"),GLAIVIER("GLAIVIER"),STRIKER("STRIKER"),
	//배틀마스터 , 인파이터						, 스트라이커
	
	//헌터
	DEADEYE("DEADEYE"),ARTILLERIST("ARTILLERIST"),SHARPSHOOTER("SHARPSHOOTER"),MACHINIST("MACHINIST"),GUNSLINGER("GUNSLINGER"),
	//마법사 
	BARD("BARD"),SUMMONER("SUMMONER"),ARCANIST("ARCANIST"),SORCERESS("SORCERESS"),
	//암살자
	SHADOWHUNTER("SHADOWHUNTER"),DEATHBLADE("DEATHBLADE"),REAPER("REAPER"),
	//스페셜리스트
	PAINTER("PAINTER"),AEROMANCER("AEROMANCER"),


	//
	NONE("NONE");
	
	
	
	private String classes;
	LoaClass(){}
	LoaClass(String classes) {
        this.classes = classes;
    }
	
	public String getclasses() {return this.classes;}
	
	@JsonCreator
	public static LoaClass Typefrom(String s) {
		
		for(LoaClass loaClass : LoaClass.values()){
            if(loaClass. getclasses().equals(s)) {
            	return loaClass;
            }
        }
		
		return NONE;
	}
	
}
