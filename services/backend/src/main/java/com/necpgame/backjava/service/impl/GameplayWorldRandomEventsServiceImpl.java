package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.CharacterActiveEventEntity;
import com.necpgame.backjava.entity.CharacterEventHistoryEntity;
import com.necpgame.backjava.entity.RandomEventEntity;
import com.necpgame.backjava.model.ActiveEventInstance;
import com.necpgame.backjava.model.EventChoice;
import com.necpgame.backjava.model.EventChoiceSkillCheck;
import com.necpgame.backjava.model.EventOutcome;
import com.necpgame.backjava.model.EventOutcomeConsequences;
import com.necpgame.backjava.model.EventResolutionResult;
import com.necpgame.backjava.model.EventResolutionResultSkillCheckResult;
import com.necpgame.backjava.model.GenerateEventForLocation200Response;
import com.necpgame.backjava.model.GenerateEventForLocationRequest;
import com.necpgame.backjava.model.GetActiveEvents200Response;
import com.necpgame.backjava.model.GetEventHistory200Response;
import com.necpgame.backjava.model.ListRandomEvents200Response;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.RandomEvent;
import com.necpgame.backjava.model.RandomEventDetailed;
import com.necpgame.backjava.model.RandomEventDetailedAllOfNpcsInvolved;
import com.necpgame.backjava.model.RandomEventDetailedAllOfTimeRestrictions;
import com.necpgame.backjava.model.ResolveEventRequest;
import com.necpgame.backjava.model.TriggerConditions;
import com.necpgame.backjava.model.TriggerEventRequest;
import com.necpgame.backjava.model.TriggeredEventInstance;
import com.necpgame.backjava.repository.CharacterActiveEventRepository;
import com.necpgame.backjava.repository.CharacterEventHistoryRepository;
import com.necpgame.backjava.repository.RandomEventRepository;
import com.necpgame.backjava.repository.specification.RandomEventSpecifications;
import com.necpgame.backjava.service.GameplayWorldRandomEventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameplayWorldRandomEventsServiceImpl implements GameplayWorldRandomEventsService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_ACTIVE_EVENTS = 5;
    private static final Duration DEFAULT_EVENT_TTL = Duration.ofHours(4);

    private final RandomEventRepository randomEventRepository;
    private final CharacterActiveEventRepository characterActiveEventRepository;
    private final CharacterEventHistoryRepository characterEventHistoryRepository;
    private final ObjectMapper objectMapper;

    private final Random random = new Random();

    @Override
    @Transactional(readOnly = true)
    public ListRandomEvents200Response listRandomEvents(String period, String category, String locationType, Integer page, Integer pageSize) {
        int pageNumber = (page == null || page < 1) ? DEFAULT_PAGE : page;
        int effectivePageSize = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;

        Specification<RandomEventEntity> specification = Specification.where(RandomEventSpecifications.activeOnly());

        Specification<RandomEventEntity> periodSpec = RandomEventSpecifications.withPeriod(period);
        if (periodSpec != null) {
            specification = specification.and(periodSpec);
        }

        Specification<RandomEventEntity> categorySpec = RandomEventSpecifications.withCategory(category);
        if (categorySpec != null) {
            specification = specification.and(categorySpec);
        }

        Specification<RandomEventEntity> locationSpec = RandomEventSpecifications.withLocationType(locationType);
        if (locationSpec != null) {
            specification = specification.and(locationSpec);
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, effectivePageSize);
        Page<RandomEventEntity> eventsPage = randomEventRepository.findAll(specification, pageable);

        List<RandomEvent> events = eventsPage.stream()
            .map(this::toRandomEvent)
            .toList();

        PaginationMeta meta = new PaginationMeta()
            .page(pageNumber)
            .pageSize(effectivePageSize)
            .total((int) eventsPage.getTotalElements())
            .totalPages(eventsPage.getTotalPages())
            .hasNext(eventsPage.hasNext())
            .hasPrev(eventsPage.hasPrevious());

        return new ListRandomEvents200Response(events, meta);
    }

    @Override
    @Transactional(readOnly = true)
    public RandomEventDetailed getRandomEvent(String eventId) {
        RandomEventEntity entity = randomEventRepository.findById(eventId)
            .filter(RandomEventEntity::getActive)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Random event not found"));
        return toRandomEventDetailed(entity);
    }

    @Override
    @Transactional
    public TriggeredEventInstance triggerRandomEvent(TriggerEventRequest triggerEventRequest) {
        RandomEventEntity eventEntity = randomEventRepository.findById(triggerEventRequest.getEventId())
            .filter(RandomEventEntity::getActive)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Random event not found"));

        UUID characterId = triggerEventRequest.getCharacterId();
        ensureActiveCapacity(characterId);

        return createActiveEvent(characterId,
            eventEntity,
            triggerEventRequest.getLocationId(),
            null,
            null,
            Boolean.TRUE.equals(triggerEventRequest.getOverrideChance()) ? 1.0d : eventEntity.getBaseTriggerChance());
    }

    @Override
    @Transactional(readOnly = true)
    public GetActiveEvents200Response getActiveEvents(UUID characterId) {
        List<CharacterActiveEventEntity> entities = characterActiveEventRepository
            .findByCharacterIdAndStatusOrderByTriggeredAtDesc(characterId, CharacterActiveEventEntity.EventStatus.ACTIVE);

        List<ActiveEventInstance> active = entities.stream()
            .map(this::toActiveEventInstance)
            .toList();

        return new GetActiveEvents200Response()
            .activeEvents(active)
            .maxActiveEvents(MAX_ACTIVE_EVENTS);
    }

    @Override
    @Transactional(readOnly = true)
    public GetEventHistory200Response getEventHistory(UUID characterId, String period, Integer page, Integer pageSize) {
        int pageNumber = (page == null || page < 1) ? DEFAULT_PAGE : page;
        int effectivePageSize = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, effectivePageSize);

        Page<CharacterEventHistoryEntity> historyPage;
        if (StringUtils.hasText(period)) {
            historyPage = characterEventHistoryRepository
                .findByCharacterIdAndPeriodIgnoreCaseOrderByResolvedAtDesc(characterId, period, pageable);
        } else {
            historyPage = characterEventHistoryRepository
                .findByCharacterIdOrderByResolvedAtDesc(characterId, pageable);
        }

        List<com.necpgame.backjava.model.EventHistoryEntry> entries = historyPage.stream()
            .map(this::toEventHistoryEntry)
            .toList();

        PaginationMeta meta = new PaginationMeta()
            .page(pageNumber)
            .pageSize(effectivePageSize)
            .total((int) historyPage.getTotalElements())
            .totalPages(historyPage.getTotalPages())
            .hasNext(historyPage.hasNext())
            .hasPrev(historyPage.hasPrevious());

        return new GetEventHistory200Response(entries, meta);
    }

    @Override
    @Transactional
    public EventResolutionResult resolveEvent(UUID characterId, ResolveEventRequest resolveEventRequest) {
        CharacterActiveEventEntity activeEvent = characterActiveEventRepository.findById(resolveEventRequest.getInstanceId())
            .filter(entity -> characterId.equals(entity.getCharacterId()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active event not found"));

        if (activeEvent.getStatus() != CharacterActiveEventEntity.EventStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event already resolved");
        }

        RandomEventDetailed eventDetailed = readEventSnapshot(activeEvent.getEventSnapshot());
        if (eventDetailed == null) {
            RandomEventEntity eventEntity = randomEventRepository.findById(activeEvent.getEventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Event template missing"));
            eventDetailed = toRandomEventDetailed(eventEntity);
        }

        final RandomEventDetailed effectiveEvent = eventDetailed;

        EventChoice choice = findChoice(effectiveEvent, resolveEventRequest.getChoiceId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Choice not found"));

        EventOutcome outcome = findOutcome(effectiveEvent, choice.getLeadsToOutcome())
            .orElseGet(() -> effectiveEvent.getOutcomes().isEmpty() ? null : effectiveEvent.getOutcomes().get(0));

        EventResolutionResultSkillCheckResult skillCheckResult = evaluateSkillCheck(choice);

        Map<String, Object> appliedConsequences = outcome != null && outcome.getConsequences() != null
            ? objectMapper.convertValue(outcome.getConsequences(), new TypeReference<Map<String, Object>>() {})
            : Collections.emptyMap();

        EventResolutionResult resolutionResult = new EventResolutionResult()
            .instanceId(activeEvent.getId())
            .choiceMade(choice.getChoiceId())
            .skillCheckResult(skillCheckResult)
            .outcome(outcome)
            .consequencesApplied(appliedConsequences);

        activeEvent.setStatus(CharacterActiveEventEntity.EventStatus.COMPLETED);
        activeEvent.setChoiceId(choice.getChoiceId());
        activeEvent.setOutcomeId(outcome != null ? outcome.getOutcomeId() : null);
        activeEvent.setConsequencesSnapshot(writeValue(outcome != null ? outcome.getConsequences() : null));
        characterActiveEventRepository.save(activeEvent);

        CharacterEventHistoryEntity historyEntity = CharacterEventHistoryEntity.builder()
            .characterId(characterId)
            .eventId(activeEvent.getEventId())
            .eventName(effectiveEvent.getName())
            .period(effectiveEvent.getPeriod())
            .instanceId(activeEvent.getId())
            .choiceId(choice.getChoiceId())
            .outcomeId(outcome != null ? outcome.getOutcomeId() : null)
            .triggeredAt(activeEvent.getTriggeredAt())
            .resolvedAt(LocalDateTime.now(ZoneOffset.UTC))
            .consequencesSummary(summarizeConsequences(outcome))
            .eventSnapshot(writeValue(effectiveEvent))
            .outcomeSnapshot(writeValue(outcome))
            .skillCheckSnapshot(writeValue(skillCheckResult))
            .build();

        characterEventHistoryRepository.save(historyEntity);

        return resolutionResult;
    }

    @Override
    @Transactional
    public GenerateEventForLocation200Response generateEventForLocation(GenerateEventForLocationRequest request) {
        UUID characterId = request.getCharacterId();
        ensureActiveCapacity(characterId);

        Specification<RandomEventEntity> specification = Specification.where(RandomEventSpecifications.activeOnly());

        Specification<RandomEventEntity> locationSpec = RandomEventSpecifications.withLocationType(request.getLocationType());
        if (locationSpec != null) {
            specification = specification.and(locationSpec);
        }

        List<RandomEventEntity> candidates = randomEventRepository.findAll(specification);
        if (candidates.isEmpty()) {
            return new GenerateEventForLocation200Response()
                .eventGenerated(false)
                .generationChanceWas(0F);
        }

        GenerateEventForLocationRequest.TimeOfDayEnum timeOfDay = request.getTimeOfDay();

        Optional<RandomEventEntity> selected = candidates.stream()
            .filter(entity -> matchesTimeOfDay(entity, timeOfDay))
            .filter(entity -> !characterActiveEventRepository.existsByCharacterIdAndEventIdAndStatus(characterId, entity.getId(), CharacterActiveEventEntity.EventStatus.ACTIVE))
            .findFirst();

        if (selected.isEmpty()) {
            return new GenerateEventForLocation200Response()
                .eventGenerated(false)
                .generationChanceWas(0F);
        }

        RandomEventEntity eventEntity = selected.get();
        double chance = eventEntity.getBaseTriggerChance() != null ? eventEntity.getBaseTriggerChance() : 0.5d;
        double roll = random.nextDouble();

        if (roll > chance) {
            return new GenerateEventForLocation200Response()
                .eventGenerated(false)
                .generationChanceWas((float) chance);
        }

        TriggeredEventInstance instance = createActiveEvent(
            characterId,
            eventEntity,
            request.getLocationId(),
            request.getLocationType(),
            timeOfDay != null ? timeOfDay.getValue() : null,
            chance);

        return new GenerateEventForLocation200Response()
            .eventGenerated(true)
            .event(instance)
            .generationChanceWas((float) chance);
    }

    private void ensureActiveCapacity(UUID characterId) {
        List<CharacterActiveEventEntity> activeEvents = characterActiveEventRepository
            .findByCharacterIdAndStatusOrderByTriggeredAtDesc(characterId, CharacterActiveEventEntity.EventStatus.ACTIVE);
        if (activeEvents.size() >= MAX_ACTIVE_EVENTS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Maximum active events reached");
        }
    }

    private TriggeredEventInstance createActiveEvent(UUID characterId,
                                                     RandomEventEntity eventEntity,
                                                     String locationId,
                                                     String locationType,
                                                     String timeOfDay,
                                                     Double generationChance) {
        RandomEventDetailed detailed = toRandomEventDetailed(eventEntity);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiresAt = eventEntity.getPossibleOutcomesCount() != null && eventEntity.getPossibleOutcomesCount() > 0
            ? now.plus(DEFAULT_EVENT_TTL)
            : null;

        CharacterActiveEventEntity activeEntity = CharacterActiveEventEntity.builder()
            .characterId(characterId)
            .eventId(eventEntity.getId())
            .status(CharacterActiveEventEntity.EventStatus.ACTIVE)
            .triggeredAt(now)
            .expiresAt(expiresAt)
            .locationId(locationId)
            .locationType(locationType)
            .timeOfDay(timeOfDay)
            .generationChance(generationChance)
            .eventSnapshot(writeValue(detailed))
            .build();

        CharacterActiveEventEntity saved = characterActiveEventRepository.save(activeEntity);

        return new TriggeredEventInstance()
            .instanceId(saved.getId())
            .event(detailed)
            .triggeredAt(toOffset(saved.getTriggeredAt()))
            .expiresAt(toOffset(saved.getExpiresAt()))
            .location(locationId);
    }

    private ActiveEventInstance toActiveEventInstance(CharacterActiveEventEntity entity) {
        RandomEventDetailed detailed = readEventSnapshot(entity.getEventSnapshot());
        RandomEvent summary = detailed != null ? toRandomEvent(detailed) : null;
        Integer timeRemaining = entity.getExpiresAt() != null ? calculateTimeRemainingSeconds(entity.getExpiresAt()) : null;

        return new ActiveEventInstance()
            .instanceId(entity.getId())
            .event(summary)
            .triggeredAt(toOffset(entity.getTriggeredAt()))
            .timeRemainingSeconds(timeRemaining);
    }

    private com.necpgame.backjava.model.EventHistoryEntry toEventHistoryEntry(CharacterEventHistoryEntity entity) {
        return new com.necpgame.backjava.model.EventHistoryEntry()
            .instanceId(entity.getInstanceId())
            .eventId(entity.getEventId())
            .eventName(entity.getEventName())
            .choiceMade(entity.getChoiceId())
            .outcomeAchieved(entity.getOutcomeId())
            .triggeredAt(toOffset(entity.getTriggeredAt()))
            .resolvedAt(toOffset(entity.getResolvedAt()))
            .consequencesSummary(entity.getConsequencesSummary());
    }

    private RandomEvent toRandomEvent(RandomEventEntity entity) {
        TriggerConditions triggerConditions = readValue(entity.getTriggerConditionsJson(), TriggerConditions.class);
        RandomEvent randomEvent = new RandomEvent()
            .eventId(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .period(entity.getPeriod())
            .triggerConditions(triggerConditions)
            .possibleOutcomesCount(entity.getPossibleOutcomesCount());

        if (entity.getBaseTriggerChance() != null) {
            randomEvent.baseTriggerChance(entity.getBaseTriggerChance().floatValue());
        }

        String categoryValue = normalizeCategory(entity.getCategory());
        if (categoryValue != null) {
            try {
                randomEvent.category(RandomEvent.CategoryEnum.fromValue(categoryValue));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown random event category: {}", categoryValue);
            }
        }

        return randomEvent;
    }

    private RandomEvent toRandomEvent(RandomEventDetailed detailed) {
        RandomEvent randomEvent = new RandomEvent()
            .eventId(detailed.getEventId())
            .name(detailed.getName())
            .description(detailed.getDescription())
            .period(detailed.getPeriod())
            .triggerConditions(detailed.getTriggerConditions())
            .possibleOutcomesCount(detailed.getPossibleOutcomesCount());

        if (detailed.getBaseTriggerChance() != null) {
            randomEvent.baseTriggerChance(detailed.getBaseTriggerChance());
        }

        if (detailed.getCategory() != null) {
            try {
                randomEvent.category(RandomEvent.CategoryEnum.fromValue(detailed.getCategory().getValue()));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown detailed random event category: {}", detailed.getCategory().getValue());
            }
        }

        return randomEvent;
    }

    private RandomEventDetailed toRandomEventDetailed(RandomEventEntity entity) {
        RandomEventDetailed detailed = new RandomEventDetailed()
            .eventId(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .period(entity.getPeriod())
            .fullDescription(entity.getFullDescription())
            .triggerConditions(readValue(entity.getTriggerConditionsJson(), TriggerConditions.class))
            .triggerLocations(readValue(entity.getTriggerLocationsJson(), new TypeReference<List<String>>() {}, Collections::emptyList))
            .timeRestrictions(readValue(entity.getTimeRestrictionsJson(), RandomEventDetailedAllOfTimeRestrictions.class))
            .npcsInvolved(readValue(entity.getNpcsInvolvedJson(), new TypeReference<List<RandomEventDetailedAllOfNpcsInvolved>>() {}, Collections::emptyList))
            .choices(readValue(entity.getChoicesJson(), new TypeReference<List<EventChoice>>() {}, Collections::emptyList))
            .outcomes(readValue(entity.getOutcomesJson(), new TypeReference<List<EventOutcome>>() {}, Collections::emptyList))
            .possibleOutcomesCount(entity.getPossibleOutcomesCount());

        if (entity.getBaseTriggerChance() != null) {
            detailed.baseTriggerChance(entity.getBaseTriggerChance().floatValue());
        }

        String categoryValue = normalizeCategory(entity.getCategory());
        if (categoryValue != null) {
            try {
                detailed.category(RandomEventDetailed.CategoryEnum.fromValue(categoryValue));
            } catch (IllegalArgumentException ex) {
                log.warn("Unknown random event category: {}", categoryValue);
            }
        }

        return detailed;
    }

    private RandomEventDetailed readEventSnapshot(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return readValue(json, RandomEventDetailed.class);
    }

    private boolean matchesTimeOfDay(RandomEventEntity entity, GenerateEventForLocationRequest.TimeOfDayEnum timeOfDay) {
        if (timeOfDay == null) {
            return true;
        }
        RandomEventDetailedAllOfTimeRestrictions restrictions = readValue(entity.getTimeRestrictionsJson(), RandomEventDetailedAllOfTimeRestrictions.class);
        if (restrictions == null || restrictions.getTimeOfDay() == null || restrictions.getTimeOfDay().isEmpty()) {
            return true;
        }
        return restrictions.getTimeOfDay().stream()
            .map(RandomEventDetailedAllOfTimeRestrictions.TimeOfDayEnum::getValue)
            .anyMatch(value -> timeOfDay.getValue().equalsIgnoreCase(value));
    }

    private EventResolutionResultSkillCheckResult evaluateSkillCheck(EventChoice choice) {
        if (choice.getSkillCheck() == null || !choice.getSkillCheck().isPresent()) {
            return null;
        }
        EventChoiceSkillCheck skillCheck = choice.getSkillCheck().get();
        if (skillCheck == null || skillCheck.getDifficulty() == null) {
            return null;
        }
        int roll = random.nextInt(20) + 1;
        int modifier = 0;
        boolean success = roll + modifier >= skillCheck.getDifficulty();
        return new EventResolutionResultSkillCheckResult()
            .success(success)
            .roll(roll)
            .modifier(modifier);
    }

    private Optional<EventChoice> findChoice(RandomEventDetailed detailed, String choiceId) {
        if (!StringUtils.hasText(choiceId) || detailed.getChoices() == null) {
            return Optional.empty();
        }
        return detailed.getChoices().stream()
            .filter(choice -> choiceId.equals(choice.getChoiceId()))
            .findFirst();
    }

    private Optional<EventOutcome> findOutcome(RandomEventDetailed detailed, String outcomeId) {
        if (!StringUtils.hasText(outcomeId) || detailed.getOutcomes() == null) {
            return Optional.empty();
        }
        return detailed.getOutcomes().stream()
            .filter(outcome -> outcomeId.equals(outcome.getOutcomeId()))
            .findFirst();
    }

    private String normalizeCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        return category.trim().toUpperCase();
    }

    private Integer calculateTimeRemainingSeconds(LocalDateTime expiresAt) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        if (expiresAt.isBefore(now)) {
            return 0;
        }
        long seconds = Duration.between(now, expiresAt).getSeconds();
        return (int) seconds;
    }

    private OffsetDateTime toOffset(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(ZoneOffset.UTC);
    }

    private String summarizeConsequences(EventOutcome outcome) {
        if (outcome == null || outcome.getConsequences() == null) {
            return null;
        }

        EventOutcomeConsequences consequences = outcome.getConsequences();
        List<String> parts = new ArrayList<>();

        if (consequences.getExperienceGained() != null) {
            parts.add("xp+" + consequences.getExperienceGained());
        }
        if (consequences.getCurrencyChange() != null && consequences.getCurrencyChange().getEddies() != null) {
            parts.add("eddies " + consequences.getCurrencyChange().getEddies());
        }
        if (consequences.getItemsGained() != null && !consequences.getItemsGained().isEmpty()) {
            parts.add("items+" + consequences.getItemsGained().size());
        }
        if (consequences.getItemsLost() != null && !consequences.getItemsLost().isEmpty()) {
            parts.add("items-" + consequences.getItemsLost().size());
        }
        if (consequences.getReputationChanges() != null && !consequences.getReputationChanges().isEmpty()) {
            parts.add("reputation");
        }
        if (consequences.getFollowUpEvents() != null && !consequences.getFollowUpEvents().isEmpty()) {
            parts.add("follow-up" + consequences.getFollowUpEvents().size());
        }
        return parts.isEmpty() ? null : String.join(", ", parts);
    }

    private <T> T readValue(String json, Class<T> type) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.error("Failed to deserialize json to {}", type, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to parse event payload");
        }
    }

    private <T> T readValue(String json, TypeReference<T> type, java.util.function.Supplier<T> fallback) {
        if (!StringUtils.hasText(json)) {
            return fallback.get();
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.error("Failed to deserialize json to {}", type.getType(), ex);
            return fallback.get();
        }
    }

    private String writeValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize value", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialize value");
        }
    }
}

