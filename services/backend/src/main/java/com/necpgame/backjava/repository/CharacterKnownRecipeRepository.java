package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterKnownRecipeEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterKnownRecipeRepository extends JpaRepository<CharacterKnownRecipeEntity, UUID> {

    boolean existsByCharacterIdAndRecipeId(UUID characterId, String recipeId);

    Optional<CharacterKnownRecipeEntity> findByCharacterIdAndRecipeId(UUID characterId, String recipeId);

    List<CharacterKnownRecipeEntity> findByCharacterId(UUID characterId);
}

