package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CraftingRecipeEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftingRecipeRepository extends JpaRepository<CraftingRecipeEntity, String>, JpaSpecificationExecutor<CraftingRecipeEntity> {

    List<CraftingRecipeEntity> findByRecipeIdIn(Collection<String> recipeIds);
}

