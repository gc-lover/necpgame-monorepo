package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.LoreFactionEntity;
import com.necpgame.backjava.entity.enums.LoreFactionType;
import org.springframework.data.jpa.domain.Specification;

public final class LoreFactionSpecifications {

    private LoreFactionSpecifications() {
    }

    public static Specification<LoreFactionEntity> hasType(LoreFactionType type) {
        return (root, query, builder) -> builder.equal(root.get("type"), type);
    }

    public static Specification<LoreFactionEntity> hasRegion(String region) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("region")), region.toLowerCase());
    }
}


