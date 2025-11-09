package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterMilestoneEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterMilestoneRepository extends JpaRepository<CharacterMilestoneEntity, UUID> {

    List<CharacterMilestoneEntity> findByCharacterId(UUID characterId);

    Optional<CharacterMilestoneEntity> findByCharacterIdAndMilestoneId(UUID characterId, UUID milestoneId);
}



