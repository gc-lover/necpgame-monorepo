package com.necpgame.backjava.repository.specification;

import com.necpgame.backjava.entity.RandomEventEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class RandomEventSpecifications {

    private RandomEventSpecifications() {
    }

    public static Specification<RandomEventEntity> activeOnly() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<RandomEventEntity> withPeriod(String period) {
        if (!StringUtils.hasText(period)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(cb.lower(root.get("period")), period.toLowerCase());
    }

    public static Specification<RandomEventEntity> withCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<RandomEventEntity> withLocationType(String locationType) {
        if (!StringUtils.hasText(locationType)) {
            return null;
        }
        return (root, query, cb) -> {
            Expression<String> field = root.get("locationTypesJson");
            String pattern = "%" + locationType.toLowerCase() + "%";
            return cb.like(cb.lower(field), pattern);
        };
    }
}


