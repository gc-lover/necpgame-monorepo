package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.LoreLocationEntity;
import com.necpgame.backjava.entity.enums.LoreLocationType;
import org.springframework.data.jpa.domain.Specification;

public final class LocationSpecifications {

    private LocationSpecifications() {
    }

    public static Specification<LoreLocationEntity> hasType(LoreLocationType type) {
        return (root, query, builder) -> builder.equal(root.get("type"), type);
    }

    public static Specification<LoreLocationEntity> hasRegion(String region) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("region")), region.toLowerCase());
    }
}


