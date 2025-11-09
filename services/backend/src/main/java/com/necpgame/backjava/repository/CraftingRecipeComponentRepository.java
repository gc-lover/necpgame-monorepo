package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CraftingRecipeComponentEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftingRecipeComponentRepository extends JpaRepository<CraftingRecipeComponentEntity, UUID> {

    List<CraftingRecipeComponentEntity> findByRecipeIdOrderByComponentNameAsc(String recipeId);
}

