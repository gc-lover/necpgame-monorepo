package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterProgressionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterProgressionRepository extends JpaRepository<CharacterProgressionEntity, UUID> {
}



