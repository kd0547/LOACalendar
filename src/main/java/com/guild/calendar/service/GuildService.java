package com.guild.calendar.service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.dto.GuildFormDto;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.ResponseGuildUserDto;
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
	
	public Long saveGuild(GuildFormDto guildFormDto) {
		Member member = memberRepository.findByEmail(guildFormDto.getGuildOwner());
		Guild findGuild = guildRepository.findByMemberAndGuildName(member, guildFormDto.getGuildName());
		
		if(findGuild != null) {
		
			throw new DuplicateKeyException("중복된 길드명은 사용할 수 없습니다.");
		}
		
		Guild guild = Guild.createGuild(member, guildFormDto.getGuildOwner(), guildFormDto.getGuildName());
		Guild saveGuild = guildRepository.save(guild);
		
		return saveGuild.getId();
	}

	
	/**
	 * 길드를 수정합니다. 중복으로 길드명을 수정할 수 없습니다.
	 * @param username
	 * @param guildId
	 * @param guildFormDto
	 */
	@Transactional
	public void updateGuild(String username, Long guildId , GuildFormDto guildFormDto) {
		String isDuplicateGuildName = guildFormDto.getGuildName();
		
		Guild guild = guildRepository.findByGuildOwnerAndId(username,guildId);
		if(guild == null) {
			throw new IllegalArgumentException("-150");
		}
		
		Guild duplicateGuild = guildRepository.findByGuildOwnerAndGuildName(username,isDuplicateGuildName);
		if(duplicateGuild != null) {
			throw new IllegalStateException("-151");
		}
		
		String originalGuildName = guild.getGuildName();
		guild.setGuildName(guildFormDto.getGuildName());
		
		/*
		 * 2023-01-18 추가 
		 * GuildName이 GuildUser에 있으면 
		 * Guild 엔티티에서 길드명을 변경하면서 GuildUser도 변경해줘야하는 문제 
		 * 
		 * 처음 의도는 GuildUser를 조회할 때 길드명이 같이 필요해서 
		 * 1회 요청 당 2회의 SELECT가 비효율적이라고 판단해 GuildUser에 GuildName을 추가했지만 
		 * Guild 엔티티에서 GuildName을 변경하면 GuildUser에서 최대 50번의 UPDATE가 발생함 
		 * 
		 * 길드명을 자주 변경하는 일은 적다고 판단해서 이대로 놔두지만 엔티티에 다른 엔티티의 컬럼을 추가하는 경우는 
		 * 컬럼 값이 자주 변할 일이 없는 값을 넣어두는 것이 좋을 듯 싶다
		 * 
		 */
		List<GuildUser> guildUsers = guildUserRepository.findByGuildNameAndGuild(originalGuildName,guild.getId());
		
		/*
		 * 1. 트랜잭션이 종료되면 영속성 컨텍스트에 있는 데이터를 DB 에 UPDATE 하게되는데 여기서 UPDATE Bulk를 수행하는 코드 없이 리스트 상태로 놔두면 어떤 결과가 나타날까?
		 * 결과 : https://www.notion.so/2d9baa58e6f44709a9d3dace57024d3a
		 * UPDATE 쿼리가 수정된 만큼 발생한다. 하지만 실제 DB에서 발생한 쿼리문을 보면 단 1번의 쿼리문만 사용한 것을 확인할 수 있었습니다. 
		 * 
		 * 이유는 spring.jpa.properties.hibernate.jdbc.batch_size=20 설정을 추가했기 때문입니다. 
		 * 
		 *
		 */
		for(GuildUser guildUser : guildUsers) {
			guildUser.setGuildName(isDuplicateGuildName);
		}
		
		//
		//
		
	}
	
	

	public List<GuildFormDto> findAllGuild(String username) {
		List<GuildFormDto> guildFormDtos = new ArrayList<GuildFormDto>();
		
		List<Guild> guilds = guildRepository.findAllByGuildOwner(username);
		
		for(Guild guild : guilds) {
			GuildFormDto guildFormDto = new GuildFormDto();
			guildFormDto.setId(guild.getId());
			guildFormDto.setGuildName(guild.getGuildName());
			//나중에 닉네임이 생기면 아이디 대신 닉네임 저장 
			//guildForm.setGuildOwner(guild.getGuildOwner());
			guildFormDto.setAuth(guild.getDiscodeAuth());
			
			guildFormDtos.add(guildFormDto);
			
		}
		
		
		return guildFormDtos;
	}


	/**
	 * 길드를 삭제하는 메서드입니다. 길드원이 있을 경우 삭제할 수 없습니다. 
	 * 
	 * @param username
	 * @param guildId
	 */
	public void deleteGuild(String username, Long guildId) {
		
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		if(findGuild == null) {
			throw new IllegalStateException("-150");
		}
		Long count = guildUserRepository.countByGuild(findGuild);
		
		if(count != 0) {
			throw new IllegalStateException("-152");
		}
		
		
		guildRepository.delete(findGuild);
		
	}


	/**
	 * 길드 유저 저장에 사용하는 메서드 
	 * @param username
	 * @param guildId
	 * @param guildUserDto
	 * @return
	 */
	public Long saveGuildUser(String username, Long guildId, GuildUserDto guildUserDto) {
		
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		if(findGuild == null) {
			throw new IllegalStateException("-150");
		}
		
		GuildUser duplicateGuildUser = guildUserRepository.findByGuildNameAndUsername(findGuild.getGuildName(),guildUserDto.getUsername());
		
		if(duplicateGuildUser != null) {
			throw new IllegalStateException("-151");
		}
		
		
		GuildUser guildUser = GuildUser.createGuildUser(guildUserDto);
		guildUser.setGuild(findGuild);
		guildUser.setGuildName(findGuild.getGuildName());
		
		GuildUser saveGuildUser = guildUserRepository.save(guildUser);
		
		return saveGuildUser.getId();
	}


	/**
	 * 길드 유저 조회에 사용하는 메서드 
	 * @param username
	 * @param guildId
	 * @return
	 */
	public ResponseGuildUserDto findAllGuildUser(String username, Long guildId) {
		
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		
		if(findGuild == null) {
			throw new IllegalStateException("-150");
		}
		List<GuildUser> guildUsers = guildUserRepository.findByGuild(findGuild);
		List<GuildUserDto> guildUserDtos = new ArrayList<GuildUserDto>();
		ResponseGuildUserDto responseGuildUserDto = new ResponseGuildUserDto();
		
		for(GuildUser guildUser : guildUsers) {
			GuildUserDto guildUserDto =  GuildUserDto.createGuildUserDto(guildUser);
			
			guildUserDtos.add(guildUserDto);
		}
		responseGuildUserDto.setId(findGuild.getId());
		responseGuildUserDto.setGuildName(findGuild.getGuildName());
		responseGuildUserDto.setGuildUserDtos(guildUserDtos);
		
		return responseGuildUserDto;
	}


	@Transactional
	public Long updateGuildUser(String username, Long guildId, Long guildUserId, GuildUserDto guildUserDto) {
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		
		if(findGuild == null) {
			throw new IllegalStateException("-150");
		}
		GuildUser findGuildUser = guildUserRepository.findByGuildAndId(findGuild, guildUserId);
		GuildUser duplicateGuildUser = guildUserRepository.findByGuildNameAndUsername(findGuild.getGuildName(), guildUserDto.getUsername());
		
		if(duplicateGuildUser != null) {
			throw new IllegalStateException("-151");
		}
		
		if(findGuildUser == null) {
			throw new IllegalStateException("-150");
		}
		
		findGuildUser.updateGuildUser(guildUserDto);
		
		return findGuildUser.getId();
	}



	public Long deleteGuildUser(String username,Long guildId, Long guildUserId) {
		Guild findGuild = guildRepository.findByIdAndGuildOwner(guildId, username);
		
		if(findGuild == null) {
			throw new IllegalStateException("-150");
		}
		
		GuildUser findGuildUser = guildUserRepository.findByGuildAndId(findGuild, guildUserId);
		
		if(findGuildUser == null) {
			throw new IllegalStateException("-150");
		}
		
		guildUserRepository.delete(findGuildUser);
		
		return guildUserId;
	}
	
}
