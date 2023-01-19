package com.guild.calendar.constant;

public enum LegionRaid {

	//발탄 
	VALTAN(1415,8),
	VALTAN_HARD(1445,8),
	VALTAN_HELL(1445,8),
	
	//비아키스
	BIACKISS(1430,8),
	BIACKISS_HARD(1460,8),
	BIACKISS_HELL(1460,8),
	
	//쿠크세이튼
	KOUKUSATON_REHEARSAL(1385,4),
	KOUKUSATON(1475,4),
	KOUKUSATON_HELL(1475,4),
	
	
	//아브렐슈드 노말 
	ABRELSHUDST_ALL_STAGE(1520,8),
	
	ABRELSHUDST_STAGE1_2(1490,8),
	ABRELSHUDST_STAGE3_4(1500,8),
	ABRELSHUDST_STAGE5_6(1520,8),
	
	//아브렐슈드 하드
	ABRELSHUD_DEJAVU(1430,8),
	ABRELSHUD_HARD_ALL_STAGE(1560,8),
	
	ABRELSHUD_HARD_STAGE1_2(1540,8),
	ABRELSHUD_HARD_STAGE3_4(1550,8),
	ABRELSHUD_HARD_STAGE5_6(1560,8),
	
	//아브렐슈드 헬
	ABRELSHUD_HELL(),
	
	ILLIAKAN_EPIDEMIC(1500,8),
	ILLIAKAN(1580,8),
	ILLIAKAN_HARD(1600,8),
	
	//일정 없음
	KAMEN();
	
	LegionRaid() {
		// TODO Auto-generated constructor stub
	}
	
	LegionRaid(int level) {
		this.level = level;
	}
	
	LegionRaid(int level,int headCount) {
		this.level = level;
		this.headCount = headCount;
	}
	private int level;
	private int headCount;
	
	public int getLevel() {
		return level;
	}

	public int getHeadCount() {
		return headCount;
	}
	
	
	
}
