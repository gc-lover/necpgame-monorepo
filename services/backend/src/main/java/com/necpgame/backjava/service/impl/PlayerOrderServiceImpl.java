package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.PlayerOrderEntity;
import com.necpgame.backjava.entity.PlayerOrderReviewEntity;
import com.necpgame.backjava.entity.enums.PlayerOrderDifficulty;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import com.necpgame.backjava.model.AcceptPlayerOrderRequest;
import com.necpgame.backjava.model.CancelPlayerOrderRequest;
import com.necpgame.backjava.model.CompletePlayerOrderRequest;
import com.necpgame.backjava.model.CreateOrderRequest;
import com.necpgame.backjava.model.ExecuteOrderViaNPC200Response;
import com.necpgame.backjava.model.ExecuteOrderViaNPCRequest;
import com.necpgame.backjava.model.ExecutorReputation;
import com.necpgame.backjava.model.GetAvailableOrders200Response;
import com.necpgame.backjava.model.GetCreatedOrders200Response;
import com.necpgame.backjava.model.GetOrdersMarket200Response;
import com.necpgame.backjava.model.OrderCompletionResult;
import com.necpgame.backjava.model.OrderCompletionResultBonuses;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.PlayerOrder;
import com.necpgame.backjava.model.PlayerOrderDetailed;
import com.necpgame.backjava.model.PlayerOrderDetailedAllOfReviews;
import com.necpgame.backjava.model.PlayerOrderMarketTypeStats;
import com.necpgame.backjava.repository.PlayerOrderRepository;
import com.necpgame.backjava.repository.PlayerOrderReviewRepository;
import com.necpgame.backjava.repository.specification.PlayerOrderSpecifications;
import com.necpgame.backjava.service.PlayerOrderService;
import com.necpgame.backjava.service.mapper.PlayerOrderMapper;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PlayerOrderServiceImpl implements PlayerOrderService {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final PlayerOrderRepository playerOrderRepository;
    private final PlayerOrderReviewRepository playerOrderReviewRepository;
    private final PlayerOrderMapper playerOrderMapper;
    private final ObjectMapper objectMapper;

    public PlayerOrderServiceImpl(
        PlayerOrderRepository playerOrderRepository,
        PlayerOrderReviewRepository playerOrderReviewRepository,
        PlayerOrderMapper playerOrderMapper,
        ObjectMapper objectMapper
    ) {
        this.playerOrderRepository = playerOrderRepository;
        this.playerOrderReviewRepository = playerOrderReviewRepository;
        this.playerOrderMapper = playerOrderMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public GetAvailableOrders200Response getAvailableOrders(PlayerOrderType type, Integer minPayment, PlayerOrderDifficulty difficulty, int page, int pageSize) {
        int safePage = Math.max(page, 1) - 1;
        int safeSize = Math.min(Math.max(pageSize, 1), 100);
        Pageable pageable = PageRequest.of(safePage, safeSize);

        Specification<PlayerOrderEntity> specification = Specification
            .where(PlayerOrderSpecifications.statusIn(EnumSet.of(PlayerOrderStatus.OPEN, PlayerOrderStatus.IN_PROGRESS)))
            .and(PlayerOrderSpecifications.typeEquals(type))
            .and(PlayerOrderSpecifications.minPayment(minPayment))
            .and(PlayerOrderSpecifications.difficultyEquals(difficulty));

        Page<PlayerOrderEntity> ordersPage = playerOrderRepository.findAll(specification, pageable);
        List<PlayerOrder> data = ordersPage.getContent().stream()
            .map(playerOrderMapper::toPlayerOrder)
            .toList();

        PaginationMeta meta = new PaginationMeta(
            safePage + 1,
            safeSize,
            (int) ordersPage.getTotalElements(),
            ordersPage.getTotalPages()
        );
        meta.setHasNext(ordersPage.hasNext());
        meta.setHasPrev(ordersPage.hasPrevious());

        GetAvailableOrders200Response response = new GetAvailableOrders200Response();
        response.setData(data);
        response.setMeta(meta);
        return response;
    }

    @Override
    public PlayerOrder createPlayerOrder(CreateOrderRequest request) {
        validateCreateRequest(request);

        PlayerOrderEntity entity = PlayerOrderEntity.builder()
            .id(UUID.randomUUID())
            .creatorId(request.getCreatorId())
            .type(request.getType())
            .title(StringUtils.defaultIfBlank(request.getTitle(), buildDefaultTitle(request.getType())))
            .description(request.getDescription())
            .requirementsJson(playerOrderMapper.writeValue(request.getRequirements()))
            .payment(request.getPayment())
            .status(PlayerOrderStatus.OPEN)
            .difficulty(resolveDifficulty(request))
            .deadline(toInstant(request.getDeadline()))
            .recurring(Boolean.TRUE.equals(request.getRecurring()))
            .premium(Boolean.TRUE.equals(request.getPremium()))
            .views(0)
            .build();

        PlayerOrderEntity saved = playerOrderRepository.save(entity);
        return playerOrderMapper.toPlayerOrder(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerOrderDetailed getPlayerOrder(UUID orderId) {
        PlayerOrderEntity entity = findOrder(orderId);
        List<PlayerOrderDetailedAllOfReviews> reviews = playerOrderReviewRepository.findAllByOrder(entity).stream()
            .map(playerOrderMapper::toReview)
            .toList();
        return playerOrderMapper.toDetailed(entity, reviews);
    }

    @Override
    public void acceptPlayerOrder(UUID orderId, AcceptPlayerOrderRequest request) {
        PlayerOrderEntity entity = findOrder(orderId);
        if (entity.getStatus() != PlayerOrderStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already in progress or completed");
        }
        UUID executorId = request.getExecutorId();
        if (executorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Executor id is required");
        }
        if (executorId.equals(entity.getCreatorId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Executor cannot be order creator");
        }
        entity.setExecutorId(executorId);
        entity.setStatus(PlayerOrderStatus.IN_PROGRESS);
        entity.setAcceptedAt(Instant.now());
        entity.setEstimatedCompletion(toInstant(request.getEstimatedCompletionTime()));
        entity.setExecutorName(null);
        playerOrderRepository.save(entity);
    }

    @Override
    public OrderCompletionResult completePlayerOrder(UUID orderId, CompletePlayerOrderRequest request) {
        PlayerOrderEntity entity = findOrder(orderId);
        if (entity.getStatus() != PlayerOrderStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order is not in progress");
        }
        if (!Objects.equals(entity.getExecutorId(), request.getExecutorId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only assigned executor can complete the order");
        }
        entity.setStatus(PlayerOrderStatus.COMPLETED);
        entity.setCompletedAt(Instant.now());
        entity.setCompletionProofJson(playerOrderMapper.writeValue(request.getCompletionProof()));
        playerOrderRepository.save(entity);

        return buildCompletionResult(entity);
    }

    @Override
    public void cancelPlayerOrder(UUID orderId, CancelPlayerOrderRequest request) {
        PlayerOrderEntity entity = findOrder(orderId);
        if (entity.getStatus() == PlayerOrderStatus.COMPLETED || entity.getStatus() == PlayerOrderStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already completed or cancelled");
        }
        if (!Objects.equals(entity.getCreatorId(), request.getCharacterId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only order creator can cancel the order");
        }
        entity.setStatus(PlayerOrderStatus.CANCELLED);
        entity.setCancellationReason(StringUtils.abbreviate(request.getReason(), 1000));
        playerOrderRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCreatedOrders200Response getCreatedOrders(UUID characterId, PlayerOrderStatus status) {
        List<PlayerOrderEntity> entities = status == null
            ? playerOrderRepository.findAllByCreatorId(characterId)
            : playerOrderRepository.findAllByCreatorIdAndStatus(characterId, status);
        List<PlayerOrder> orders = entities.stream()
            .map(playerOrderMapper::toPlayerOrder)
            .toList();
        GetCreatedOrders200Response response = new GetCreatedOrders200Response();
        response.setOrders(orders);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public GetCreatedOrders200Response getExecutingOrders(UUID characterId) {
        List<PlayerOrderEntity> entities = playerOrderRepository.findAllByExecutorIdAndStatusIn(
            characterId,
            EnumSet.of(PlayerOrderStatus.IN_PROGRESS)
        );
        List<PlayerOrder> orders = entities.stream()
            .map(playerOrderMapper::toPlayerOrder)
            .toList();
        GetCreatedOrders200Response response = new GetCreatedOrders200Response();
        response.setOrders(orders);
        return response;
    }

    @Override
    public ExecuteOrderViaNPC200Response executeOrderViaNpc(UUID orderId, ExecuteOrderViaNPCRequest request) {
        PlayerOrderEntity entity = findOrder(orderId);
        if (entity.getStatus() != PlayerOrderStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already accepted");
        }
        UUID executorId = request.getExecutorId();
        if (executorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Executor id is required");
        }
        entity.setExecutorId(executorId);
        entity.setNpcExecutorId(request.getHiredNpcId());
        entity.setStatus(PlayerOrderStatus.IN_PROGRESS);
        entity.setAcceptedAt(Instant.now());
        entity.setEstimatedCompletion(entity.getDeadline() != null ? entity.getDeadline() : Instant.now().plusSeconds(86400));
        playerOrderRepository.save(entity);

        ExecuteOrderViaNPC200Response response = new ExecuteOrderViaNPC200Response();
        response.setEstimatedCompletion(toOffset(entity.getEstimatedCompletion()));
        response.setNpcEfficiency(entity.getNpcExecutorId() != null ? 0.85d : 1.0d);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ExecutorReputation getExecutorReputation(UUID characterId) {
        List<PlayerOrderEntity> assignedOrders = playerOrderRepository.findAllByExecutorId(characterId);
        long totalAssigned = assignedOrders.size();
        long completed = assignedOrders.stream().filter(order -> order.getStatus() == PlayerOrderStatus.COMPLETED).count();
        long failed = assignedOrders.stream().filter(order -> order.getStatus() == PlayerOrderStatus.CANCELLED).count();

        double successRate = totalAssigned == 0 ? 0 : (double) completed / totalAssigned;

        List<PlayerOrderReviewEntity> reviewEntities = assignedOrders.stream()
            .flatMap(order -> playerOrderReviewRepository.findAllByOrder(order).stream())
            .toList();
        double averageRating = reviewEntities.isEmpty()
            ? 0
            : reviewEntities.stream().mapToInt(PlayerOrderReviewEntity::getRating).average().orElse(0);

        int totalEarned = assignedOrders.stream()
            .filter(order -> order.getStatus() == PlayerOrderStatus.COMPLETED)
            .mapToInt(PlayerOrderEntity::getPayment)
            .sum();

        int reputationScore = (int) Math.round(completed * 10 + averageRating * 20 + successRate * 30);

        List<PlayerOrderDetailedAllOfReviews> reviews = reviewEntities.stream()
            .map(playerOrderMapper::toReview)
            .toList();

        List<String> specialisations = assignedOrders.stream()
            .filter(order -> order.getType() != null)
            .collect(Collectors.groupingBy(PlayerOrderEntity::getType, Collectors.counting()))
            .entrySet()
            .stream()
            .sorted(Map.Entry.<PlayerOrderType, Long>comparingByValue().reversed())
            .limit(3)
            .map(entry -> entry.getKey().name())
            .toList();

        ExecutorReputation reputation = new ExecutorReputation();
        reputation.setCharacterId(characterId);
        reputation.setReputationScore(reputationScore);
        reputation.setTier(resolveTier(reputationScore));
        reputation.setOrdersCompleted((int) completed);
        reputation.setSuccessRate(successRate);
        reputation.setAverageRating(averageRating);
        reputation.setTotalEarned(totalEarned);
        reputation.setSpecializations(specialisations);
        reputation.setReviews(reviews);
        return reputation;
    }

    @Override
    @Transactional(readOnly = true)
    public GetOrdersMarket200Response getOrdersMarket() {
        Collection<PlayerOrderStatus> activeStatuses = EnumSet.of(PlayerOrderStatus.OPEN, PlayerOrderStatus.IN_PROGRESS);

        int activeCount = (int) playerOrderRepository.countByStatusIn(activeStatuses);
        Double averagePayment = playerOrderRepository.findAveragePaymentByStatusIn(activeStatuses);

        List<PlayerOrderMarketTypeStats> popularTypes = playerOrderRepository.findPopularTypes(activeStatuses).stream()
            .map(tuple -> {
                PlayerOrderMarketTypeStats stats = new PlayerOrderMarketTypeStats();
                stats.setType(((PlayerOrderType) tuple[0]).name());
                stats.setCount((Long) tuple[1]);
                stats.setAveragePayment(tuple[2] != null ? ((Double) tuple[2]) : null);
                return stats;
            })
            .toList();

        List<String> highDemandSkills = collectHighDemandSkills(activeStatuses);

        GetOrdersMarket200Response response = new GetOrdersMarket200Response();
        response.setActiveOrdersCount(activeCount);
        response.setAveragePayment(averagePayment != null ? averagePayment.intValue() : null);
        response.setPopularTypes(popularTypes);
        response.setHighDemandSkills(highDemandSkills);
        return response;
    }

    private List<String> collectHighDemandSkills(Collection<PlayerOrderStatus> statuses) {
        Specification<PlayerOrderEntity> specification = PlayerOrderSpecifications.statusIn(statuses);
        return playerOrderRepository.findAll(specification).stream()
            .map(PlayerOrderEntity::getRequirementsJson)
            .filter(StringUtils::isNotBlank)
            .map(this::readRequirementsSkills)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)))
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .toList();
    }

    private Map<String, Integer> readRequirementsSkills(String json) {
        try {
            Map<String, Object> payload = objectMapper.readValue(json, MAP_TYPE);
            Object skillsRaw = payload.get("required_skills");
            if (skillsRaw instanceof Map<?, ?> skillMap) {
                Map<String, Integer> result = new HashMap<>();
                skillMap.forEach((key, value) -> {
                    if (key != null && value instanceof Number number) {
                        result.put(key.toString(), number.intValue());
                    }
                });
                return result;
            }
        } catch (IOException ex) {
            // ignore malformed payload
        }
        return Collections.emptyMap();
    }

    private OrderCompletionResult buildCompletionResult(PlayerOrderEntity entity) {
        OrderCompletionResult result = new OrderCompletionResult();
        result.setOrderId(entity.getId());
        result.setPaymentReleased(entity.getPayment());
        result.setReputationEarned(calculateReputationBonus(entity));

        OrderCompletionResultBonuses bonuses = new OrderCompletionResultBonuses();
        bonuses.setEarlyCompletion(calculateEarlyCompletionBonus(entity));
        bonuses.setQualityBonus(0);
        result.setBonuses(bonuses);
        result.setNextTierUnlocked(result.getReputationEarned() > 80);
        return result;
    }

    private int calculateReputationBonus(PlayerOrderEntity entity) {
        int base = Math.max(10, entity.getPayment() / 50);
        if (Boolean.TRUE.equals(entity.getPremium())) {
            base += 10;
        }
        if (entity.getDifficulty() != null) {
            base += switch (entity.getDifficulty()) {
                case EASY -> 0;
                case MEDIUM -> 10;
                case HARD -> 20;
                case EXPERT -> 30;
            };
        }
        return base;
    }

    private int calculateEarlyCompletionBonus(PlayerOrderEntity entity) {
        if (entity.getEstimatedCompletion() == null || entity.getCompletedAt() == null) {
            return 0;
        }
        return entity.getCompletedAt().isBefore(entity.getEstimatedCompletion()) ? 15 : 0;
    }

    private void validateCreateRequest(CreateOrderRequest request) {
        if (request.getCreatorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "creator_id is required");
        }
        if (request.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type is required");
        }
        if (StringUtils.isBlank(request.getDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description is required");
        }
        if (request.getPayment() == null || request.getPayment() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "payment must be positive");
        }
    }

    private PlayerOrderDifficulty resolveDifficulty(CreateOrderRequest request) {
        Integer minLevel = request.getRequirements() != null ? request.getRequirements().getMinLevel() : null;
        if (minLevel == null) {
            return PlayerOrderDifficulty.MEDIUM;
        }
        if (minLevel >= 70) {
            return PlayerOrderDifficulty.EXPERT;
        }
        if (minLevel >= 50) {
            return PlayerOrderDifficulty.HARD;
        }
        if (minLevel >= 30) {
            return PlayerOrderDifficulty.MEDIUM;
        }
        return PlayerOrderDifficulty.EASY;
    }

    private String buildDefaultTitle(PlayerOrderType type) {
        return switch (type) {
            case CRAFTING -> "Crafting order";
            case GATHERING -> "Gathering assignment";
            case COMBAT_ASSISTANCE -> "Combat assistance request";
            case TRANSPORTATION -> "Transportation contract";
            case SERVICE -> "Specialised service";
        };
    }

    private PlayerOrderEntity findOrder(UUID orderId) {
        return playerOrderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private Instant toInstant(OffsetDateTime dateTime) {
        return dateTime != null ? dateTime.toInstant() : null;
    }

    private OffsetDateTime toOffset(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }

    private String resolveTier(int reputationScore) {
        if (reputationScore >= 200) {
            return "LEGENDARY";
        }
        if (reputationScore >= 150) {
            return "MASTER";
        }
        if (reputationScore >= 100) {
            return "EXPERT";
        }
        if (reputationScore >= 60) {
            return "COMPETENT";
        }
        return "NOVICE";
    }
}

