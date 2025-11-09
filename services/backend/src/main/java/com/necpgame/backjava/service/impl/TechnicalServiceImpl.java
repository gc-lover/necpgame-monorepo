package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.NotificationEntity;
import com.necpgame.backjava.entity.PlayerResetStatusEntity;
import com.necpgame.backjava.entity.ResetHistoryEntity;
import com.necpgame.backjava.entity.ResetScheduleEntity;
import com.necpgame.backjava.entity.ResetTypeStatusEntity;
import com.necpgame.backjava.model.GetNotifications200Response;
import com.necpgame.backjava.model.GetResetHistory200Response;
import com.necpgame.backjava.model.MarkAllNotificationsReadRequest;
import com.necpgame.backjava.model.Notification;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.PlayerResetItems;
import com.necpgame.backjava.model.PlayerResetItemsBonuses;
import com.necpgame.backjava.model.PlayerResetItemsInstancesInner;
import com.necpgame.backjava.model.PlayerResetItemsLimits;
import com.necpgame.backjava.model.PlayerResetItemsLimitsAuctionPosts;
import com.necpgame.backjava.model.PlayerResetItemsQuests;
import com.necpgame.backjava.model.PlayerResetStatus;
import com.necpgame.backjava.model.ResetExecutionResult;
import com.necpgame.backjava.model.ResetHistoryEntry;
import com.necpgame.backjava.model.ResetSchedule;
import com.necpgame.backjava.model.ResetStatusResponse;
import com.necpgame.backjava.model.ResetTypeStatus;
import com.necpgame.backjava.model.SendNotification200Response;
import com.necpgame.backjava.model.SendNotificationRequest;
import com.necpgame.backjava.model.TriggerResetRequest;
import com.necpgame.backjava.model.UpdateScheduleRequest;
import com.necpgame.backjava.model.ScheduleConfig;
import com.necpgame.backjava.repository.NotificationRepository;
import com.necpgame.backjava.repository.PlayerResetStatusRepository;
import com.necpgame.backjava.repository.ResetHistoryRepository;
import com.necpgame.backjava.repository.ResetScheduleRepository;
import com.necpgame.backjava.repository.ResetTypeStatusRepository;
import com.necpgame.backjava.service.TechnicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TechnicalServiceImpl implements TechnicalService {

    private final NotificationRepository notificationRepository;
    private final ResetTypeStatusRepository resetTypeStatusRepository;
    private final ResetScheduleRepository resetScheduleRepository;
    private final ResetHistoryRepository resetHistoryRepository;
    private final PlayerResetStatusRepository playerResetStatusRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public GetNotifications200Response getNotifications(String playerId, @Nullable Boolean unreadOnly, @Nullable String type, @Nullable Integer page, @Nullable Integer limit) {
        UUID playerUuid = parseUuid(playerId);
        if (playerUuid == null) {
            log.warn("getNotifications called with invalid player_id: {}", playerId);
            return emptyNotificationsResponse();
        }
        int pageNumber = page == null || page < 1 ? 0 : page - 1;
        int pageSize = limit == null || limit < 1 ? 50 : limit;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        boolean onlyUnread = Boolean.TRUE.equals(unreadOnly);
        String normalizedType = type == null ? null : type.trim().toLowerCase(Locale.ROOT);
        boolean hasType = normalizedType != null && !normalizedType.isBlank();

        Page<NotificationEntity> pageResult;
        if (hasType && normalizedType != null && onlyUnread) {
            pageResult = notificationRepository.findByPlayerIdAndTypeIgnoreCaseAndIsReadFalse(playerUuid, normalizedType, pageable);
        } else if (hasType && normalizedType != null) {
            pageResult = notificationRepository.findByPlayerIdAndTypeIgnoreCase(playerUuid, normalizedType, pageable);
        } else if (onlyUnread) {
            pageResult = notificationRepository.findByPlayerIdAndIsReadFalse(playerUuid, pageable);
        } else {
            pageResult = notificationRepository.findByPlayerId(playerUuid, pageable);
        }

        List<Notification> models = new ArrayList<>();
        for (NotificationEntity entity : pageResult.getContent()) {
            models.add(toModel(entity));
        }

        PaginationMeta pagination = new PaginationMeta(
            pageResult.getNumber() + 1,
            pageResult.getSize(),
            toInt(pageResult.getTotalElements()),
            pageResult.getTotalPages()
        );
        pagination.setHasNext(pageResult.hasNext());
        pagination.setHasPrev(pageResult.hasPrevious());

        return new GetNotifications200Response()
            .notifications(models)
            .pagination(pagination);
    }

    @Override
    public Object markAllNotificationsRead(MarkAllNotificationsReadRequest markAllNotificationsReadRequest) {
        UUID playerUuid = markAllNotificationsReadRequest != null ? parseUuid(markAllNotificationsReadRequest.getPlayerId()) : null;
        if (playerUuid == null) {
            log.warn("markAllNotificationsRead called with invalid player_id");
            return Map.of("updated", 0);
        }
        List<NotificationEntity> unread = notificationRepository.findByPlayerIdAndIsReadFalse(playerUuid);
        int updated = 0;
        for (NotificationEntity entity : unread) {
            if (!entity.isRead()) {
                entity.setRead(true);
                updated++;
            }
        }
        if (!unread.isEmpty()) {
            notificationRepository.saveAll(unread);
        }
        return Map.of("updated", updated);
    }

    @Override
    public Object markNotificationRead(String notificationId) {
        UUID id = parseUuid(notificationId);
        if (id == null) {
            log.warn("markNotificationRead called with invalid notification_id: {}", notificationId);
            return Map.of("updated", false);
        }
        Optional<NotificationEntity> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            return Map.of("updated", false);
        }
        NotificationEntity entity = notificationOpt.get();
        if (!entity.isRead()) {
            entity.setRead(true);
            notificationRepository.save(entity);
            return Map.of("updated", true);
        }
        return Map.of("updated", false);
    }

    @Override
    @SuppressWarnings({"DataFlowIssue", "null"})
    public SendNotification200Response sendNotification(SendNotificationRequest sendNotificationRequest) {
        UUID playerUuid = parseUuid(sendNotificationRequest.getPlayerId());
        if (playerUuid == null) {
            throw new IllegalArgumentException("Invalid player_id supplied");
        }
        NotificationEntity entity = NotificationEntity.builder()
            .playerId(playerUuid)
            .type(sendNotificationRequest.getType() != null ? sendNotificationRequest.getType().getValue() : null)
            .priority(sendNotificationRequest.getPriority() != null ? sendNotificationRequest.getPriority().getValue() : SendNotificationRequest.PriorityEnum.NORMAL.getValue())
            .message(sendNotificationRequest.getMessage())
            .data(serializeData(sendNotificationRequest.getData()))
            .sendEmail(Boolean.TRUE.equals(sendNotificationRequest.getSendEmail()))
            .read(false)
            .build();
        notificationRepository.save(entity);
        String notificationId = entity.getId() != null ? entity.getId().toString() : null;
        return new SendNotification200Response().notificationId(notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResetStatus getPlayerResetStatus(UUID playerId) {
        if (playerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "player_id is required");
        }
        return playerResetStatusRepository.findByPlayerId(playerId)
            .map(this::mapPlayerResetStatus)
            .orElseGet(() -> buildDefaultPlayerResetStatus(playerId));
    }

    @Override
    @Transactional(readOnly = true)
    public GetResetHistory200Response getResetHistory(@Nullable String resetType, @Nullable Integer days) {
        int window = (days == null || days < 1) ? 7 : Math.min(days, 90);
        OffsetDateTime threshold = OffsetDateTime.now().minusDays(window);
        List<ResetHistoryEntity> entities;
        if (StringUtils.hasText(resetType)) {
            String normalized = resetType.trim().toUpperCase(Locale.ROOT);
            entities = resetHistoryRepository.findByResetTypeAndExecutionTimeGreaterThanEqualOrderByExecutionTimeDesc(normalized, threshold);
        } else {
            entities = resetHistoryRepository.findByExecutionTimeGreaterThanEqualOrderByExecutionTimeDesc(threshold);
        }
        List<com.necpgame.backjava.model.ResetHistoryEntry> history = entities.stream()
            .map(this::mapResetHistoryEntry)
            .collect(Collectors.toList());
        return new GetResetHistory200Response().history(history);
    }

    @Override
    @Transactional(readOnly = true)
    public ResetSchedule getResetSchedule() {
        Map<String, ResetScheduleEntity> byType = resetScheduleRepository.findAll().stream()
            .collect(Collectors.toMap(ResetScheduleEntity::getResetType, entity -> entity, (a, b) -> a));
        return buildResetSchedule(byType);
    }

    @Override
    @Transactional(readOnly = true)
    public ResetStatusResponse getResetStatus() {
        OffsetDateTime now = OffsetDateTime.now();
        Map<String, ResetTypeStatusEntity> byType = resetTypeStatusRepository.findAll().stream()
            .collect(Collectors.toMap(ResetTypeStatusEntity::getResetType, entity -> entity, (a, b) -> a));
        return new ResetStatusResponse()
            .daily(buildResetTypeStatus("DAILY", byType.get("DAILY"), now))
            .weekly(buildResetTypeStatus("WEEKLY", byType.get("WEEKLY"), now))
            .monthly(buildResetTypeStatus("MONTHLY", byType.get("MONTHLY"), now))
            .serverTime(now);
    }

    @Override
    @Transactional(readOnly = true)
    public ResetTypeStatus getResetTypeStatus(String resetType) {
        if (!StringUtils.hasText(resetType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reset_type is required");
        }
        String normalized = resetType.trim().toUpperCase(Locale.ROOT);
        ResetTypeStatusEntity entity = resetTypeStatusRepository.findById(normalized)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reset type not found"));
        return buildResetTypeStatus(normalized, entity, OffsetDateTime.now());
    }

    @Override
    public ResetExecutionResult triggerReset(TriggerResetRequest triggerResetRequest) {
        if (triggerResetRequest == null || triggerResetRequest.getResetType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reset_type is required");
        }
        String resetType = triggerResetRequest.getResetType().getValue();
        TriggerResetRequest.TargetEnum target = triggerResetRequest.getTarget() != null
            ? triggerResetRequest.getTarget()
            : TriggerResetRequest.TargetEnum.ALL_PLAYERS;
        if (target == TriggerResetRequest.TargetEnum.SINGLE_PLAYER && triggerResetRequest.getPlayerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "player_id is required for SINGLE_PLAYER target");
        }
        OffsetDateTime now = OffsetDateTime.now();
        List<String> items = triggerResetRequest.getItems() != null && !triggerResetRequest.getItems().isEmpty()
            ? new ArrayList<>(triggerResetRequest.getItems())
            : defaultItemsForReset(resetType);
        ResetHistoryEntity history = ResetHistoryEntity.builder()
            .id(UUID.randomUUID())
            .resetType(resetType)
            .executionTime(now)
            .triggeredBy("MANUAL_ADMIN")
            .affectedPlayers(target == TriggerResetRequest.TargetEnum.SINGLE_PLAYER ? 1 : null)
            .success(true)
            .executionDurationMs(0)
            .itemsReset(writeStringList(items))
            .errors(null)
            .build();
        resetHistoryRepository.save(history);
        updateTypeStatusAfterReset(resetType, items, now);
        if (target == TriggerResetRequest.TargetEnum.SINGLE_PLAYER && triggerResetRequest.getPlayerId() != null) {
            updatePlayerResetStatus(triggerResetRequest.getPlayerId(), resetType, now);
        }
        ResetExecutionResult result = new ResetExecutionResult()
            .resetId(history.getId())
            .resetType(resetType)
            .executionTime(now)
            .affectedPlayers(history.getAffectedPlayers())
            .executionDurationMs(history.getExecutionDurationMs())
            .itemsReset(items);
        result.setErrors(Collections.emptyList());
        return result;
    }

    @Override
    public Void updateResetSchedule(UpdateScheduleRequest updateScheduleRequest) {
        if (updateScheduleRequest == null || updateScheduleRequest.getResetType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reset_type is required");
        }
        String resetType = updateScheduleRequest.getResetType().getValue();
        ResetScheduleEntity entity = resetScheduleRepository.findById(resetType)
            .orElse(ResetScheduleEntity.builder().resetType(resetType).enabled(true).build());
        if (updateScheduleRequest.getCronExpression() != null) {
            entity.setCronExpression(updateScheduleRequest.getCronExpression());
        }
        if (updateScheduleRequest.getTimezone() != null) {
            entity.setTimezone(updateScheduleRequest.getTimezone());
        }
        if (updateScheduleRequest.getEnabled() != null) {
            entity.setEnabled(updateScheduleRequest.getEnabled());
        }
        if (updateScheduleRequest.getItemsToReset() != null) {
            entity.setItemsToReset(writeStringList(updateScheduleRequest.getItemsToReset()));
        }
        resetScheduleRepository.save(entity);
        updateNextResetFromSchedule(resetType, entity);
        return null;
    }

    private PlayerResetStatus mapPlayerResetStatus(PlayerResetStatusEntity entity) {
        PlayerResetItems daily = readPlayerResetItems(entity.getDailyDetails());
        PlayerResetItems weekly = readPlayerResetItems(entity.getWeeklyDetails());
        return new PlayerResetStatus()
            .playerId(entity.getPlayerId())
            .daily(daily)
            .weekly(weekly)
            .lastDailyReset(entity.getLastDailyReset())
            .lastWeeklyReset(entity.getLastWeeklyReset());
    }

    private PlayerResetStatus buildDefaultPlayerResetStatus(UUID playerId) {
        OffsetDateTime now = OffsetDateTime.now();
        PlayerResetItems daily = createSamplePlayerItems(now.plusHours(12));
        PlayerResetItems weekly = createSamplePlayerItems(now.plusDays(3));
        return new PlayerResetStatus()
            .playerId(playerId)
            .daily(daily)
            .weekly(weekly)
            .lastDailyReset(now.minusHours(12))
            .lastWeeklyReset(now.minusDays(3));
    }

    private PlayerResetItems createSamplePlayerItems(OffsetDateTime nextInstanceReset) {
        PlayerResetItemsQuests quests = new PlayerResetItemsQuests()
            .availableSlots(5)
            .completedToday(0);
        PlayerResetItemsLimitsAuctionPosts auctionPosts = new PlayerResetItemsLimitsAuctionPosts()
            .used(0)
            .max(6);
        PlayerResetItemsLimitsAuctionPosts craftingSlots = new PlayerResetItemsLimitsAuctionPosts()
            .used(1)
            .max(3);
        PlayerResetItemsLimits limits = new PlayerResetItemsLimits()
            .auctionPosts(auctionPosts)
            .craftingSlots(craftingSlots);
        PlayerResetItemsBonuses bonuses = new PlayerResetItemsBonuses()
            .dailyLoginClaimed(false)
            .firstWinClaimed(false);
        PlayerResetItemsInstancesInner instance = new PlayerResetItemsInstancesInner()
            .instanceId("blackwall-expedition")
            .name("Blackwall Expedition")
            .attemptsUsed(0)
            .maxAttempts(1)
            .resetsAt(nextInstanceReset);
        return new PlayerResetItems()
            .quests(quests)
            .limits(limits)
            .bonuses(bonuses)
            .instances(List.of(instance));
    }

    private ResetHistoryEntry mapResetHistoryEntry(ResetHistoryEntity entity) {
        ResetHistoryEntry entry = new ResetHistoryEntry()
            .resetId(entity.getId())
            .executionTime(entity.getExecutionTime())
            .affectedPlayers(entity.getAffectedPlayers())
            .success(entity.getSuccess())
            .executionDurationMs(entity.getExecutionDurationMs());
        if (StringUtils.hasText(entity.getResetType())) {
            try {
                entry.setResetType(ResetHistoryEntry.ResetTypeEnum.fromValue(entity.getResetType()));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown reset_type stored: {}", entity.getResetType());
            }
        }
        if (StringUtils.hasText(entity.getTriggeredBy())) {
            try {
                entry.setTriggeredBy(ResetHistoryEntry.TriggeredByEnum.fromValue(entity.getTriggeredBy()));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown triggered_by stored: {}", entity.getTriggeredBy());
            }
        }
        return entry;
    }

    private ResetSchedule buildResetSchedule(Map<String, ResetScheduleEntity> byType) {
        return new ResetSchedule()
            .daily(toScheduleConfig("DAILY", byType.get("DAILY")))
            .weekly(toScheduleConfig("WEEKLY", byType.get("WEEKLY")))
            .monthly(toScheduleConfig("MONTHLY", byType.get("MONTHLY")));
    }

    private ScheduleConfig toScheduleConfig(String resetType, ResetScheduleEntity entity) {
        ScheduleConfig config = new ScheduleConfig();
        if (entity != null) {
            config.cronExpression(entity.getCronExpression())
                .timezone(entity.getTimezone())
                .enabled(entity.isEnabled())
                .itemsToReset(readStringList(entity.getItemsToReset()));
        } else {
            config.enabled(true)
                .timezone("UTC")
                .itemsToReset(defaultItemsForReset(resetType));
            switch (resetType) {
                case "WEEKLY" -> config.cronExpression("0 0 * * MON");
                case "MONTHLY" -> config.cronExpression("0 0 1 * *");
                default -> config.cronExpression("0 0 * * *");
            }
        }
        return config;
    }

    private ResetTypeStatus buildResetTypeStatus(String resetType, ResetTypeStatusEntity entity, OffsetDateTime now) {
        ResetTypeStatus status = new ResetTypeStatus();
        if (StringUtils.hasText(resetType)) {
            try {
                status.setResetType(ResetTypeStatus.ResetTypeEnum.fromValue(resetType));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown reset type requested: {}", resetType);
            }
        }
        OffsetDateTime lastReset = entity != null ? entity.getLastResetAt() : defaultLastReset(resetType, now);
        ResetScheduleEntity schedule = resetScheduleRepository.findById(resetType).orElse(null);
        OffsetDateTime nextReset = entity != null ? entity.getNextResetAt() : calculateNextReset(resetType, schedule, now);
        if (nextReset == null || nextReset.isBefore(now)) {
            nextReset = calculateNextReset(resetType, schedule, now);
        }
        status.setLastReset(lastReset);
        status.setNextReset(nextReset);
        if (nextReset != null) {
            Duration duration = Duration.between(now, nextReset);
            if (duration.isNegative()) {
                duration = Duration.ZERO;
            }
            status.setTimeUntilReset(formatDuration(duration));
            status.setTimeUntilResetSeconds((int) Math.max(0, duration.getSeconds()));
        }
        List<String> items = readStringList(entity != null ? entity.getResetItems() : null);
        if (items.isEmpty()) {
            if (schedule != null && StringUtils.hasText(schedule.getItemsToReset())) {
                items = readStringList(schedule.getItemsToReset());
            } else {
                items = defaultItemsForReset(resetType);
            }
        }
        status.setResetItems(items);
        return status;
    }

    private OffsetDateTime defaultLastReset(String resetType, OffsetDateTime now) {
        return switch (resetType) {
            case "WEEKLY" -> now.minusDays(3);
            case "MONTHLY" -> now.minusDays(10);
            default -> now.minusHours(12);
        };
    }

    private List<String> defaultItemsForReset(String resetType) {
        return switch (resetType) {
            case "WEEKLY" -> List.of("weekly_quests", "raid_lockouts", "guild_progress");
            case "MONTHLY" -> List.of("monthly_rewards", "season_points");
            default -> List.of("daily_quests", "daily_limits", "vendor_inventory", "login_rewards");
        };
    }

    private void updateTypeStatusAfterReset(String resetType, List<String> items, OffsetDateTime now) {
        ResetTypeStatusEntity entity = resetTypeStatusRepository.findById(resetType)
            .orElse(ResetTypeStatusEntity.builder().resetType(resetType).build());
        entity.setLastResetAt(now);
        ResetScheduleEntity schedule = resetScheduleRepository.findById(resetType).orElse(null);
        entity.setNextResetAt(calculateNextReset(resetType, schedule, now));
        if (items != null && !items.isEmpty()) {
            entity.setResetItems(writeStringList(items));
        } else if (entity.getResetItems() == null) {
            entity.setResetItems(writeStringList(defaultItemsForReset(resetType)));
        }
        resetTypeStatusRepository.save(entity);
    }

    private void updatePlayerResetStatus(UUID playerId, String resetType, OffsetDateTime now) {
        PlayerResetStatusEntity entity = playerResetStatusRepository.findByPlayerId(playerId)
            .orElse(PlayerResetStatusEntity.builder().playerId(playerId).build());
        if ("WEEKLY".equals(resetType) || "MONTHLY".equals(resetType)) {
            entity.setLastWeeklyReset(now);
        } else {
            entity.setLastDailyReset(now);
        }
        if (entity.getDailyDetails() == null) {
            entity.setDailyDetails(writePlayerResetItems(createSamplePlayerItems(now.plusHours(12))));
        }
        if (entity.getWeeklyDetails() == null) {
            entity.setWeeklyDetails(writePlayerResetItems(createSamplePlayerItems(now.plusDays(3))));
        }
        playerResetStatusRepository.save(entity);
    }

    private void updateNextResetFromSchedule(String resetType, ResetScheduleEntity scheduleEntity) {
        ResetTypeStatusEntity entity = resetTypeStatusRepository.findById(resetType)
            .orElse(ResetTypeStatusEntity.builder().resetType(resetType).build());
        entity.setNextResetAt(calculateNextReset(resetType, scheduleEntity, OffsetDateTime.now()));
        if (entity.getResetItems() == null && scheduleEntity != null) {
            entity.setResetItems(scheduleEntity.getItemsToReset());
        }
        resetTypeStatusRepository.save(entity);
    }

    private OffsetDateTime calculateNextReset(String resetType, ResetScheduleEntity scheduleEntity, OffsetDateTime from) {
        OffsetDateTime reference = from != null ? from : OffsetDateTime.now();
        ResetScheduleEntity schedule = scheduleEntity != null ? scheduleEntity : resetScheduleRepository.findById(resetType).orElse(null);
        if (schedule != null && schedule.isEnabled() && StringUtils.hasText(schedule.getCronExpression())) {
            String zoneId = StringUtils.hasText(schedule.getTimezone()) ? schedule.getTimezone() : "UTC";
            String cronExpression = schedule.getCronExpression().trim();
            if (cronExpression.split("\\s+").length == 5) {
                cronExpression = "0 " + cronExpression;
            }
            try {
                CronExpression cron = CronExpression.parse(cronExpression);
                ZonedDateTime next = cron.next(reference.atZoneSameInstant(ZoneId.of(zoneId)));
                if (next != null) {
                    return next.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
                }
            } catch (Exception ex) {
                log.warn("Failed to compute next reset for {}: {}", resetType, ex.getMessage());
            }
        }
        return switch (resetType) {
            case "WEEKLY" -> reference.plusDays(7);
            case "MONTHLY" -> reference.plusDays(30);
            default -> reference.plusDays(1);
        };
    }

    private String formatDuration(Duration duration) {
        if (duration.isZero()) {
            return "0s";
        }
        long totalSeconds = Math.max(0, duration.getSeconds());
        long days = totalSeconds / 86_400;
        long hours = (totalSeconds % 86_400) / 3_600;
        long minutes = (totalSeconds % 3_600) / 60;
        long seconds = totalSeconds % 60;
        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("d ");
        }
        if (hours > 0) {
            builder.append(hours).append("h ");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }
        if (seconds > 0 || builder.length() == 0) {
            builder.append(seconds).append("s");
        }
        return builder.toString().trim();
    }

    private List<String> readStringList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse list JSON: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    private String writeStringList(List<String> items) {
        if (items == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to serialize list: {}", ex.getMessage());
            return null;
        }
    }

    private PlayerResetItems readPlayerResetItems(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, PlayerResetItems.class);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to deserialize player reset items: {}", ex.getMessage());
            return null;
        }
    }

    private String writePlayerResetItems(PlayerResetItems items) {
        if (items == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to serialize player reset items: {}", ex.getMessage());
            return null;
        }
    }

    private Notification toModel(NotificationEntity entity) {
        Notification notification = new Notification()
            .notificationId(entity.getId() != null ? entity.getId().toString() : null)
            .playerId(entity.getPlayerId() != null ? entity.getPlayerId().toString() : null)
            .message(entity.getMessage())
            .isRead(entity.isRead())
            .createdAt(entity.getCreatedAt());

        if (entity.getType() != null) {
            try {
                notification.setType(Notification.TypeEnum.fromValue(entity.getType().toLowerCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown notification type stored: {}", entity.getType());
            }
        }
        if (entity.getPriority() != null) {
            try {
                notification.setPriority(Notification.PriorityEnum.fromValue(entity.getPriority().toLowerCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown notification priority stored: {}", entity.getPriority());
            }
        }
        if (entity.getData() != null && !entity.getData().isBlank()) {
            notification.setData(deserializeData(entity.getData()));
        }
        return notification;
    }

    private String serializeData(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to serialize notification data, storing as string: {}", ex.getMessage());
            return data.toString();
        }
    }

    private Object deserializeData(String data) {
        try {
            return objectMapper.readValue(data, Object.class);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to deserialize notification data, returning raw string: {}", ex.getMessage());
            return data;
        }
    }

    private UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private int toInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    private GetNotifications200Response emptyNotificationsResponse() {
        PaginationMeta pagination = new PaginationMeta(1, 0, 0, 0);
        pagination.setHasNext(false);
        pagination.setHasPrev(false);
        return new GetNotifications200Response()
            .notifications(new ArrayList<>())
            .pagination(pagination);
    }
}

