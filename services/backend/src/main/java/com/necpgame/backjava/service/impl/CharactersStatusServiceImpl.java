package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.*;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.repository.*;
import com.necpgame.backjava.service.CharactersStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Р РµР°Р»РёР·Р°С†РёСЏ СЃРµСЂРІРёСЃР° РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃС‚Р°С‚СѓСЃРѕРј Рё С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєР°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CharactersStatusServiceImpl implements CharactersStatusService {
    
    private final CharacterStatusRepository statusRepository;
    private final CharacterStatsRepository statsRepository;
    private final SkillRepository skillRepository;
    private final CharacterSkillRepository characterSkillRepository;
    
    @Override
    @Transactional(readOnly = true)
    public CharacterStatus getCharacterStatus(UUID characterId) {
        log.info("Getting status for character: {}", characterId);
        
        CharacterStatusEntity entity = statusRepository.findByCharacterId(characterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Character status not found"));
        
        CharacterStatus dto = new CharacterStatus();
        dto.setCharacterId(entity.getCharacterId());
        dto.setHealth(entity.getHealth());
        dto.setMaxHealth(entity.getMaxHealth());
        dto.setEnergy(entity.getEnergy());
        dto.setMaxEnergy(entity.getMaxEnergy());
        dto.setHumanity(entity.getHumanity());
        dto.setMaxHumanity(entity.getMaxHumanity());
        dto.setLevel(entity.getLevel());
        dto.setExperience(entity.getExperience());
        dto.setNextLevelExperience(entity.getNextLevelExperience());
        
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public CharacterStats getCharacterStats(UUID characterId) {
        log.info("Getting stats for character: {}", characterId);
        
        CharacterStatsEntity entity = statsRepository.findByCharacterId(characterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Character stats not found"));
        
        CharacterStats dto = new CharacterStats();
        dto.setCharacterId(entity.getCharacterId());
        dto.setStrength(entity.getStrength());
        dto.setReflexes(entity.getReflexes());
        dto.setIntelligence(entity.getIntelligence());
        dto.setTechnical(entity.getTechnical());
        dto.setCool(entity.getCool());
        
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public GetCharacterSkills200Response getCharacterSkills(UUID characterId) {
        log.info("Getting skills for character: {}", characterId);
        
        List<CharacterSkillEntity> characterSkills = characterSkillRepository.findByCharacterIdOrderByLevelDesc(characterId);
        
        List<Skill> skills = new ArrayList<>();
        for (CharacterSkillEntity cs : characterSkills) {
            Skill skill = new Skill()
                .skillId(cs.getSkillId())
                .level(cs.getLevel())
                .experience(cs.getExperience());

            SkillEntity skillEntity = skillRepository.findById(cs.getSkillId()).orElse(null);
            if (skillEntity != null) {
                skill.setName(skillEntity.getName());
                skill.setAttributeDependency(resolveAttributeDependency(skillEntity.getCategory()));
            } else {
                skill.setName(cs.getSkillId());
                skill.setAttributeDependency("BODY");
            }

            skills.add(skill);
        }
        
        GetCharacterSkills200Response response = new GetCharacterSkills200Response();
        response.setSkills(skills);
        
        return response;
    }
    
    @Override
    @Transactional
    public CharacterStatus updateCharacterStatus(UUID characterId, UpdateCharacterStatusRequest request) {
        log.info("Updating status for character: {}", characterId);
        
        CharacterStatusEntity entity = statusRepository.findByCharacterId(characterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Character status not found"));
        
        // Apply deltas
        if (request.getHealthDelta() != null && request.getHealthDelta().isPresent()) {
            int newHealth = Math.max(0, Math.min(entity.getMaxHealth(), entity.getHealth() + request.getHealthDelta().get()));
            entity.setHealth(newHealth);
        }
        
        if (request.getEnergyDelta() != null && request.getEnergyDelta().isPresent()) {
            int newEnergy = Math.max(0, Math.min(entity.getMaxEnergy(), entity.getEnergy() + request.getEnergyDelta().get()));
            entity.setEnergy(newEnergy);
        }
        
        if (request.getHumanityDelta() != null && request.getHumanityDelta().isPresent()) {
            int newHumanity = Math.max(0, Math.min(entity.getMaxHumanity(), entity.getHumanity() + request.getHumanityDelta().get()));
            entity.setHumanity(newHumanity);
        }
        
        if (request.getExperienceDelta() != null && request.getExperienceDelta().isPresent()) {
            entity.setExperience(entity.getExperience() + request.getExperienceDelta().get());
            
            // Check for level up
            while (entity.getExperience() >= entity.getNextLevelExperience()) {
                entity.setLevel(entity.getLevel() + 1);
                entity.setExperience(entity.getExperience() - entity.getNextLevelExperience());
                entity.setNextLevelExperience((int) (entity.getNextLevelExperience() * 1.5)); // +50% per level
                log.info("Character {} leveled up to {}", characterId, entity.getLevel());
            }
        }
        
        entity = statusRepository.save(entity);
        
        // Return updated status
        return getCharacterStatus(characterId);
    }

    private String resolveAttributeDependency(String category) {
        if (!StringUtils.hasText(category)) {
            return "BODY";
        }
        return switch (category.toLowerCase()) {
            case "technical" -> "TECHNICAL_ABILITY";
            case "intelligence" -> "INTELLIGENCE";
            case "stealth" -> "COOL";
            case "reflex" -> "REFLEXES";
            default -> "BODY";
        };
    }
}

