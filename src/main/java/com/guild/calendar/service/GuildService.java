package com.guild.calendar.service;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.Exception.DeleteFailedException;
import com.guild.calendar.dto.GuildForm;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.entity.Guild;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.Member;
import com.guild.calendar.repository.GuildRepository;
import com.guild.calendar.repository.GuildUserRepository;
import com.guild.calendar.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuildService {
	
	private final GuildRepository guildRepository;
	private final MemberRepository memberRepository;
	private final GuildUserRepository guildUserRepository;
	
	public Long saveGuild(GuildForm guildForm) {
		Member member = memberRepository.findByEmail(guildForm.getGuildOwner());
		Guild findGuild = guildRepository.findByMemberAndGuildName(member,guildForm.getGuildName());
		
		if(findGuild != null) {
		
			throw new DuplicateKeyException("중복된 길드명은 사용할 수 없습니다.");
		}
		
		Guild guild = Guild.createGuild(member,guildForm.getGuildOwner(),guildForm.getGuildName());
		Guild saveGuild = guildRepository.save(guild);
		
		return saveGuild.getId();
	}

	
	
	@Transactional
	public void updateGuild(String username, GuildForm guildForm) {
		Long GuildId = guildForm.getId();
		
		Member member = memberRepository.findByEmail(username);
		Guild findGuild = guildRepository.findByMemberAndGuildName(member,guildForm.getGuildName());
		Guild guild = guildRepository.findByMemberAndId(member,GuildId);
		
		
		if(guild == null) {
			throw new EntityNotFoundException("요청한 "+guildForm.getGuildName()+" 이 없습니다.");
		}
		
		if(findGuild != null) {
			
			throw new DuplicateKeyException("중복된 길드명은 사용할 수 없습니다.");
		}
		
		guild.setGuildName(guildForm.getGuildName());
	}
	
	

	public List<GuildForm> findAllGuild(String username) {
		List<GuildForm> guildForms = new ArrayList<GuildForm>();
		
		List<Guild> guilds = guildRepository.findAllByGuildOwner(username);
		
		for(Guild guild : guilds) {
			GuildForm guildForm = new GuildForm();
			guildForm.setId(guild.getId());
			guildForm.setGuildName(guild.getGuildName());
			//나중에 닉네임이 생기면 아이디 대신 닉네임 저장 
			guildForm.setGuildOwner(guild.getGuildOwner());
			guildForm.setAuth(guild.getDiscodeAuth());
			
			guildForms.add(guildForm);
			
		}
		
		
		return guildForms;
	}



	public void deleteGuild(String username, Long guildId) {
		Member member = memberRepository.findByEmail(username);
		
		Guild findGuild = guildRepository.findByMemberAndId(member, guildId);
		
		if(findGuild == null) {
			throw new IllegalArgumentException("잘못된 요청입니다.");
		}
		Long count = guildUserRepository.countByGuild(findGuild);
		
		if(count != 0) {
			throw new IllegalArgumentException("길드원이 있으면 삭제할 수 없습니다.");
		}
		
		
		guildRepository.delete(findGuild);
		
	}



	public Long saveGuildUser(String username, Long guildId, GuildUserDto guildUserDto) {
		
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		
		if(findGuild == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		GuildUser guildUser = GuildUser.createGuildUser(guildUserDto);
		guildUser.setGuild(findGuild);
		guildUser.setGuildName(findGuild.getGuildName());
		
		GuildUser saveGuildUser = guildUserRepository.save(guildUser);
		
		return saveGuildUser.getId();
	}



	public List<GuildUserDto> findAllGuildUser(String username, Long guildId) {
		
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		
		if(findGuild == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		List<GuildUser> guildUsers = guildUserRepository.findByGuild(findGuild);
		List<GuildUserDto> guildUserDtos = new ArrayList<GuildUserDto>();
		
		for(GuildUser guildUser : guildUsers) {
			GuildUserDto guildUserDto =  GuildUserDto.createGuildUserDto(guildUser);
			
			guildUserDtos.add(guildUserDto);
		}
		
		
		return guildUserDtos;
	}


	@Transactional
	public Long updateGuildUser(String username, Long guildId, Long guildUserId, GuildUserDto guildUserDto) {
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		
		if(findGuild == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		GuildUser findGuildUser = guildUserRepository.findByGuildAndId(findGuild, guildUserId);
		
		if(findGuildUser == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		
		findGuildUser.updateGuildUser(guildUserDto);
		
		return findGuildUser.getId();
	}



	public void deleteGuildUser(String username,Long guildId, Long guildUserId) {
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		if(findGuild == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		
		GuildUser findGuildUser = guildUserRepository.findByGuildAndId(findGuild, guildUserId);
		
		if(findGuildUser == null) {
			throw new NullPointerException("잘못된 요청입니다.");
		}
		
		guildUserRepository.delete(findGuildUser);
		
	}
	
}
