package com.guild.calendar.service;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.dto.GuildForm;
import com.guild.calendar.entity.Guild;
import com.guild.calendar.repository.GuildRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuildService {
	
	private final GuildRepository guildRepository;
	
	public void saveGuild(GuildForm guildForm) {
	
		Guild guild = guildRepository.findByGuildOwnerAndGuildName(guildForm.getGuildOwner(),guildForm.getGuildName());
		
		
		if(guild != null) {
		
			throw new DuplicateKeyException("중복된 길드명은 사용할 수 없습니다.");
		}
		

		Guild saveGuild = Guild.createGuild(guildForm.getGuildOwner(),guildForm.getGuildName());
		
		guildRepository.save(saveGuild);
	}

	
	
	@Transactional
	public void updateGuild(GuildForm guildForm) {
		Long id = guildForm.getId();
		String guildOwner = guildForm.getGuildOwner();
		
		
		Guild guild = guildRepository.findByIdAndGuildOwner(id,guildOwner);
		
		
		if(guild == null) {
			throw new EntityNotFoundException("요청한 "+guildForm.getGuildName()+" 이 없습니다.");
		}
		
		guild.updateGuild(guildForm);
		
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



	public void deleteGuild(GuildForm guildForm) {
		Long id = guildForm.getId();
		
		
	}
	
}
