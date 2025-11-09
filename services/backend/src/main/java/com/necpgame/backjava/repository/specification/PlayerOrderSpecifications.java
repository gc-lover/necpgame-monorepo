package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.PlayerOrderEntity;
import com.necpgame.backjava.entity.enums.PlayerOrderDifficulty;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public final class PlayerOrderSpecifications {

    private PlayerOrderSpecifications() {
    }

    public static Specification<PlayerOrderEntity> statusIn(Collection<PlayerOrderStatus> statuses) {
        return (root, query, builder) -> statuses == null || statuses.isEmpty()
            ? builder.conjunction()
            : root.get("status").in(statuses);
    }

    public static Specification<PlayerOrderEntity> typeEquals(PlayerOrderType type) {
        return (root, query, builder) -> type == null ? builder.conjunction() : builder.equal(root.get("type"), type);
    }

    public static Specification<PlayerOrderEntity> difficultyEquals(PlayerOrderDifficulty difficulty) {
        return (root, query, builder) -> difficulty == null ? builder.conjunction() : builder.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<PlayerOrderEntity> minPayment(Integer minPayment) {
        return (root, query, builder) -> minPayment == null ? builder.conjunction() : builder.greaterThanOrEqualTo(root.get("payment"), minPayment);
    }

    public static Specification<PlayerOrderEntity> creator(UUID creatorId) {
        return (root, query, builder) -> creatorId == null ? builder.conjunction() : builder.equal(root.get("creatorId"), creatorId);
    }

    public static Specification<PlayerOrderEntity> executor(UUID executorId) {
        return (root, query, builder) -> executorId == null ? builder.conjunction() : builder.equal(root.get("executorId"), executorId);
    }
}

