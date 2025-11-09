package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.SkillExperienceEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillExperienceRepository extends JpaRepository<SkillExperienceEntity, UUID> {

    List<SkillExperienceEntity> findByCharacterIdOrderByCurrentLevelDescExperienceDesc(UUID characterId);

    Optional<SkillExperienceEntity> findByCharacterIdAndSkillId(UUID characterId, String skillId);
}



