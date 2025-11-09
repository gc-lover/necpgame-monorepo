package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.CraftingRecipeEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class CraftingRecipeSpecifications {

    private CraftingRecipeSpecifications() {
    }

    public static Specification<CraftingRecipeEntity> hasCategory(String category) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(category)) {
                return null;
            }
            return builder.equal(builder.upper(root.get("category")), category.trim().toUpperCase());
        };
    }

    public static Specification<CraftingRecipeEntity> hasTier(String tier) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(tier)) {
                return null;
            }
            return builder.equal(builder.upper(root.get("tier")), tier.trim().toUpperCase());
        };
    }

    public static Specification<CraftingRecipeEntity> skillLevelAtLeast(Integer minSkillLevel) {
        return (root, query, builder) -> {
            if (minSkillLevel == null) {
                return null;
            }
            return builder.greaterThanOrEqualTo(root.get("requiredSkillLevel"), minSkillLevel);
        };
    }
}

