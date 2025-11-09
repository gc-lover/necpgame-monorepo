package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.TradingGuildEntity;
import com.necpgame.backjava.entity.enums.TradingGuildType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class TradingGuildSpecifications {

    private TradingGuildSpecifications() {
    }

    public static Specification<TradingGuildEntity> hasType(String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        try {
            TradingGuildType guildType = TradingGuildType.valueOf(type.trim().toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("type"), guildType);
        } catch (IllegalArgumentException ex) {
            return (root, query, builder) -> builder.disjunction();
        }
    }

    public static Specification<TradingGuildEntity> hasMinLevel(Integer minLevel) {
        if (minLevel == null) {
            return null;
        }
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("level"), minLevel);
    }

    public static Specification<TradingGuildEntity> locatedInRegion(String region) {
        if (!StringUtils.hasText(region)) {
            return null;
        }
        String pattern = "%" + region.trim().toLowerCase() + "%";
        return (root, query, builder) -> builder.like(builder.lower(root.get("headquartersLocation")), pattern);
    }
}

