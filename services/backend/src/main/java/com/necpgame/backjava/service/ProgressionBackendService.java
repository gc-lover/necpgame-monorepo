package com.necpgame.backjava.service;

import com.necpgame.backjava.model.AddSkillExperienceRequest;
import com.necpgame.backjava.model.AwardExperienceRequest;
import com.necpgame.backjava.model.CharacterAttributes;
import com.necpgame.backjava.model.CharacterExperience;
import com.necpgame.backjava.model.CharacterSkills;
import com.necpgame.backjava.model.ExperienceAwardResult;
import com.necpgame.backjava.model.GetProgressionMilestones200Response;
import com.necpgame.backjava.model.LevelUpResult;
import com.necpgame.backjava.model.SkillExperienceResult;
import com.necpgame.backjava.model.SpendAttributePointsRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ProgressionBackendService {

    SkillExperienceResult addSkillExperience(UUID characterId, String skillId, AddSkillExperienceRequest request);

    ExperienceAwardResult awardExperience(UUID characterId, AwardExperienceRequest request);

    CharacterAttributes getCharacterAttributes(UUID characterId);

    CharacterExperience getCharacterExperience(UUID characterId);

    CharacterSkills getCharacterSkills(UUID characterId);

    GetProgressionMilestones200Response getProgressionMilestones(UUID characterId);

    LevelUpResult levelUp(UUID characterId);

    CharacterAttributes spendAttributePoints(UUID characterId, SpendAttributePointsRequest request);
}



