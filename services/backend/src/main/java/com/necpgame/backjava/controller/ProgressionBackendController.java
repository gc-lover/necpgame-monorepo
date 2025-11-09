package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.ProgressionBackendApi;
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
import com.necpgame.backjava.service.ProgressionBackendService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProgressionBackendController implements ProgressionBackendApi {

    private final ProgressionBackendService progressionBackendService;

    @Override
    public ResponseEntity<SkillExperienceResult> addSkillExperience(UUID characterId, String skillId, AddSkillExperienceRequest addSkillExperienceRequest) {
        return ResponseEntity.ok(progressionBackendService.addSkillExperience(characterId, skillId, addSkillExperienceRequest));
    }

    @Override
    public ResponseEntity<ExperienceAwardResult> awardExperience(UUID characterId, AwardExperienceRequest awardExperienceRequest) {
        return ResponseEntity.ok(progressionBackendService.awardExperience(characterId, awardExperienceRequest));
    }

    @Override
    public ResponseEntity<CharacterAttributes> getCharacterAttributes(UUID characterId) {
        return ResponseEntity.ok(progressionBackendService.getCharacterAttributes(characterId));
    }

    @Override
    public ResponseEntity<CharacterExperience> getCharacterExperience(UUID characterId) {
        return ResponseEntity.ok(progressionBackendService.getCharacterExperience(characterId));
    }

    @Override
    public ResponseEntity<CharacterSkills> getCharacterSkills(UUID characterId) {
        return ResponseEntity.ok(progressionBackendService.getCharacterSkills(characterId));
    }

    @Override
    public ResponseEntity<GetProgressionMilestones200Response> getProgressionMilestones(UUID characterId) {
        return ResponseEntity.ok(progressionBackendService.getProgressionMilestones(characterId));
    }

    @Override
    public ResponseEntity<LevelUpResult> levelUp(UUID characterId) {
        return ResponseEntity.ok(progressionBackendService.levelUp(characterId));
    }

    @Override
    public ResponseEntity<CharacterAttributes> spendAttributePoints(UUID characterId, SpendAttributePointsRequest spendAttributePointsRequest) {
        return ResponseEntity.ok(progressionBackendService.spendAttributePoints(characterId, spendAttributePointsRequest));
    }
}



