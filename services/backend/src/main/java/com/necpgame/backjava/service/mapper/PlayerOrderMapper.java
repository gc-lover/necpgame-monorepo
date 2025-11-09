package com.necpgame.backjava.service.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.PlayerOrderEntity;
import com.necpgame.backjava.entity.PlayerOrderReviewEntity;
import com.necpgame.backjava.model.PlayerOrder;
import com.necpgame.backjava.model.PlayerOrderDetailed;
import com.necpgame.backjava.model.PlayerOrderDetailedAllOfEscrow;
import com.necpgame.backjava.model.PlayerOrderDetailedAllOfReviews;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PlayerOrderMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<PlayerOrderDetailedAllOfReviews>> REVIEWS_TYPE = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public PlayerOrderMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PlayerOrder toPlayerOrder(PlayerOrderEntity entity) {
        PlayerOrder dto = new PlayerOrder();
        dto.setOrderId(entity.getId());
        dto.setCreatorId(entity.getCreatorId());
        dto.setCreatorName(entity.getCreatorName());
        dto.setType(entity.getType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPayment(entity.getPayment());
        dto.setStatus(entity.getStatus());
        dto.setDifficulty(entity.getDifficulty() != null ? entity.getDifficulty().name() : null);
        dto.setExecutorId(entity.getExecutorId());
        dto.setExecutorName(entity.getExecutorName());
        dto.setCreatedAt(toOffset(entity.getCreatedAt()));
        dto.setDeadline(toOffset(entity.getDeadline()));
        dto.setViews(entity.getViews());
        return dto;
    }

    public PlayerOrderDetailed toDetailed(PlayerOrderEntity entity, List<PlayerOrderDetailedAllOfReviews> reviews) {
        PlayerOrderDetailed dto = new PlayerOrderDetailed();
        PlayerOrder base = toPlayerOrder(entity);
        copyBase(dto, base);
        dto.setRequirements(readMap(entity.getRequirementsJson()));
        dto.setDeliverables(readMap(entity.getDeliverablesJson()));
        dto.setEscrow(readEscrow(entity.getEscrowJson()));
        dto.setReviews(reviews != null ? reviews : Collections.emptyList());
        return dto;
    }

    public PlayerOrderDetailedAllOfReviews toReview(PlayerOrderReviewEntity reviewEntity) {
        PlayerOrderDetailedAllOfReviews dto = new PlayerOrderDetailedAllOfReviews();
        dto.setReviewerId(reviewEntity.getReviewerId());
        dto.setRating(reviewEntity.getRating());
        dto.setComment(reviewEntity.getComment());
        dto.setCreatedAt(toOffset(reviewEntity.getCreatedAt()));
        return dto;
    }

    private void copyBase(PlayerOrder target, PlayerOrder source) {
        target.setOrderId(source.getOrderId());
        target.setCreatorId(source.getCreatorId());
        target.setCreatorName(source.getCreatorName());
        target.setType(source.getType());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setPayment(source.getPayment());
        target.setStatus(source.getStatus());
        target.setDifficulty(source.getDifficulty());
        target.setExecutorId(source.getExecutorId());
        target.setExecutorName(source.getExecutorName());
        target.setCreatedAt(source.getCreatedAt());
        target.setDeadline(source.getDeadline());
        target.setViews(source.getViews());
    }

    private Map<String, Object> readMap(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, MAP_TYPE);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to deserialize map payload", ex);
        }
    }

    private PlayerOrderDetailedAllOfEscrow readEscrow(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        return readValue(json, PlayerOrderDetailedAllOfEscrow.class);
    }

    private <T> T readValue(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to deserialize payload to " + type.getSimpleName(), ex);
        }
    }

    public String writeValue(Object payload) {
        if (payload == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to serialize payload", ex);
        }
    }

    private OffsetDateTime toOffset(java.time.Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }

    private OffsetDateTime toOffset(OffsetDateTime offsetDateTime) {
        return offsetDateTime;
    }

    public List<PlayerOrderDetailedAllOfReviews> cloneReviews(List<PlayerOrderDetailedAllOfReviews> reviews) {
        if (reviews == null) {
            return Collections.emptyList();
        }
        try {
            String raw = objectMapper.writeValueAsString(reviews);
            return objectMapper.readValue(raw, REVIEWS_TYPE);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to clone reviews", ex);
        }
    }
}


