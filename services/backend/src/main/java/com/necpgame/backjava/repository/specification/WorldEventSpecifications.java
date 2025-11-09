package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.WorldEventEntity;
import com.necpgame.backjava.entity.enums.WorldEventType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public final class WorldEventSpecifications {

    private WorldEventSpecifications() {
    }

    public static Specification<WorldEventEntity> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<WorldEventEntity> byEra(String era) {
        if (Objects.isNull(era) || era.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("era"), era);
    }

    public static Specification<WorldEventEntity> byType(WorldEventType type) {
        if (type == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }
}

