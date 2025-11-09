package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.CharacterActivityLogEntity;
import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterRestoreQueueEntity;
import com.necpgame.backjava.entity.CharacterSlotPaymentEntity;
import com.necpgame.backjava.entity.CharacterSlotStateEntity;
import com.necpgame.backjava.entity.CharacterSnapshotEntity;
import com.necpgame.backjava.entity.PlayerProfileEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
<<<<<<< HEAD
import com.necpgame.backjava.mapper.CharacterAppearanceMapperMS;
import com.necpgame.backjava.mapper.CharacterMapperMS;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.repository.*;
import com.necpgame.backjava.repository.LoreCharacterCategoryRepository;
import com.necpgame.backjava.service.CharactersService;
import com.necpgame.backjava.service.mapper.LoreMapper;
import com.necpgame.backjava.util.SecurityUtil;
=======
import com.necpgame.backjava.model.CharacterActivityEntry;
import com.necpgame.backjava.model.CharacterActivityEntryActor;
import com.necpgame.backjava.model.CharacterActivityListResponse;
import com.necpgame.backjava.model.CharacterAppearance;
import com.necpgame.backjava.model.CharacterAppearancePatch;
import com.necpgame.backjava.model.CharacterAppearanceResponse;
import com.necpgame.backjava.model.CharacterCreateRequest;
import com.necpgame.backjava.model.CharacterCreateResponse;
import com.necpgame.backjava.model.CharacterCreatedEvent;
import com.necpgame.backjava.model.CharacterCreatedEventPayload;
import com.necpgame.backjava.model.CharacterDeleteResponse;
import com.necpgame.backjava.model.CharacterListResponse;
import com.necpgame.backjava.model.CharacterRecalculateResponse;
import com.necpgame.backjava.model.CharacterRestoreRequest;
import com.necpgame.backjava.model.CharacterRestoreResponse;
import com.necpgame.backjava.model.CharacterRestoredEvent;
import com.necpgame.backjava.model.CharacterRestoredEventPayload;
import com.necpgame.backjava.model.CharacterSlotPurchaseRequest;
import com.necpgame.backjava.model.CharacterSlotPurchaseResponse;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.model.CharacterSlotStateNextTierCost;
import com.necpgame.backjava.model.CharacterSlotStateResponse;
import com.necpgame.backjava.model.CharacterSlotStateResponsePendingPaymentsInner;
import com.necpgame.backjava.model.CharacterStatsRecalculateRequest;
import com.necpgame.backjava.model.CharacterStatsUpdatedEvent;
import com.necpgame.backjava.model.CharacterStatsUpdatedEventPayload;
import com.necpgame.backjava.model.CharacterSummary;
import com.necpgame.backjava.model.CharacterSwitchedEvent;
import com.necpgame.backjava.model.CharacterSwitchedEventPayload;
import com.necpgame.backjava.model.CharacterSwitchLockedResponse;
import com.necpgame.backjava.model.CharacterSwitchRequest;
import com.necpgame.backjava.model.CharacterSwitchResponse;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.PlayerProfile;
import com.necpgame.backjava.model.PlayerProfileSettings;
import com.necpgame.backjava.model.PlayerProfileSocial;
import com.necpgame.backjava.model.RestoreQueueEntry;
import com.necpgame.backjava.model.StateSnapshotRef;
import com.necpgame.backjava.repository.AccountRepository;
import com.necpgame.backjava.repository.CharacterActivityLogRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterRestoreQueueRepository;
import com.necpgame.backjava.repository.CharacterSlotPaymentRepository;
import com.necpgame.backjava.repository.CharacterSlotStateRepository;
import com.necpgame.backjava.repository.CharacterSnapshotRepository;
import com.necpgame.backjava.repository.PlayerProfileRepository;
import com.necpgame.backjava.service.CharactersService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CharactersServiceImpl implements CharactersService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_TOTAL_SLOTS = 3;
    private static final int DEFAULT_MAX_SLOTS = 10;
    private static final Duration RESTORE_GRACE_PERIOD = Duration.ofDays(14);
    private static final String PRODUCER = "character-service";
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final CharacterRepository characterRepository;
    private final AccountRepository accountRepository;
<<<<<<< HEAD
    private final CharacterClassRepository characterClassRepository;
    private final CharacterSubclassRepository characterSubclassRepository;
    private final CharacterOriginRepository characterOriginRepository;
    private final FactionRepository factionRepository;
    private final CityRepository cityRepository;
    private final CharacterMapperMS characterMapper;
    private final CharacterAppearanceMapperMS appearanceMapper;
    private final LoreCharacterCategoryRepository loreCharacterCategoryRepository;
    private final LoreMapper loreMapper;
    @Override
    @Transactional(readOnly = true)
    public GetCharacterCategories200Response getCharacterCategories() {
        var categories = loreCharacterCategoryRepository.findAll().stream()
            .map(loreMapper::toCharacterCategory)
            .collect(Collectors.toList());

        GetCharacterCategories200Response response = new GetCharacterCategories200Response();
        response.setCategories(categories);
        return response;
    }

    
    /**
     * РЎРїРёСЃРѕРє РїРµСЂСЃРѕРЅР°Р¶РµР№ С‚РµРєСѓС‰РµРіРѕ РёРіСЂРѕРєР°
     */
=======
    private final CharacterSlotStateRepository slotStateRepository;
    private final CharacterSlotPaymentRepository slotPaymentRepository;
    private final CharacterRestoreQueueRepository restoreQueueRepository;
    private final CharacterActivityLogRepository activityLogRepository;
    private final CharacterSnapshotRepository snapshotRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final ObjectMapper objectMapper;

>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
    @Override
    @Transactional(readOnly = true)
    public CharacterActivityListResponse charactersPlayersAccountsAccountIdActivityGet(UUID accountId,
                                                                                       String activityType,
                                                                                       OffsetDateTime dateFrom,
                                                                                       OffsetDateTime dateTo,
                                                                                       Integer page,
                                                                                       Integer pageSize) {
        AccountEntity account = fetchAccount(accountId);
        int pageNumber = page == null || page < 1 ? DEFAULT_PAGE : page;
        int size = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        OffsetDateTime from = dateFrom != null ? dateFrom : OffsetDateTime.now().minusDays(30);
        OffsetDateTime to = dateTo != null ? dateTo : OffsetDateTime.now();
        Page<CharacterActivityLogEntity> result;
        if (activityType != null && !activityType.isBlank()) {
            CharacterActivityLogEntity.ActivityType type = mapActivityType(activityType);
            result = activityLogRepository.findByAccountIdAndActivityTypeInAndOccurredAtBetween(account.getId(),
                List.of(type), from, to, pageable);
        } else {
            result = activityLogRepository.findByAccountIdAndOccurredAtBetween(account.getId(), from, to, pageable);
        }
        return mapActivityPage(result);
    }

    @Override
    public CharacterAppearanceResponse charactersPlayersAccountsAccountIdCharactersCharacterIdAppearancePatch(UUID accountId,
                                                                                                               UUID characterId,
                                                                                                               CharacterAppearancePatch patch) {
        AccountEntity account = fetchAccount(accountId);
        CharacterEntity character = fetchCharacter(account, characterId);
        if (patch == null || patch.getAppearance() == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "appearance is required");
        }
        CharacterAppearanceEntity appearance = character.getAppearance();
        if (appearance == null) {
            appearance = createAppearance(patch.getAppearance());
            character.setAppearance(appearance);
        } else {
            applyAppearance(appearance, patch.getAppearance());
        }
        character.setLastActiveAt(OffsetDateTime.now());
        characterRepository.save(character);
        recordActivity(account, character, CharacterActivityLogEntity.ActivityType.appearance,
            Map.of("loadoutPreview", Optional.ofNullable(patch.getLoadoutPreview()).orElse(false)));
        CharacterAppearanceResponse response = new CharacterAppearanceResponse();
        response.setCharacterId(character.getId());
        response.setAppearance(toAppearanceDto(character.getAppearance()));
        response.setEvents(List.of("appearance.updated"));
        return response;
    }

    @Override
    public CharacterDeleteResponse charactersPlayersAccountsAccountIdCharactersCharacterIdDelete(UUID accountId,
                                                                                                 UUID characterId) {
        AccountEntity account = fetchAccount(accountId);
        CharacterEntity character = fetchCharacter(account, characterId);
        if (character.isDeleted()) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "character already deleted");
        }
        OffsetDateTime now = OffsetDateTime.now();
        character.setDeleted(true);
        character.setStatus(CharacterEntity.LifecycleStatus.DELETED);
        character.setDeletedAt(now);
        character.setRestoreUntil(now.plus(RESTORE_GRACE_PERIOD));
        characterRepository.save(character);
        CharacterRestoreQueueEntity queueEntity = restoreQueueRepository
            .findByCharacterIdAndStatus(character.getId(), CharacterRestoreQueueEntity.RestoreStatus.pending)
            .orElseGet(() -> {
                CharacterRestoreQueueEntity created = new CharacterRestoreQueueEntity();
                created.setAccount(account);
                created.setCharacter(character);
                return created;
            });
        queueEntity.setQueuedAt(now);
        queueEntity.setExpiresAt(character.getRestoreUntil());
        queueEntity.setStatus(CharacterRestoreQueueEntity.RestoreStatus.pending);
        queueEntity.setReason(null);
        restoreQueueRepository.save(queueEntity);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        long activeCount = characterRepository.countByAccountIdAndDeletedFalse(account.getId());
        slotState.setUsedSlots(Math.toIntExact(activeCount));
        slotStateRepository.save(slotState);
        recordActivity(account, character, CharacterActivityLogEntity.ActivityType.deletion,
            Map.of("restoreUntil", character.getRestoreUntil()));
        CharacterDeleteResponse response = new CharacterDeleteResponse();
        response.setCharacterId(character.getId());
        response.setDeletedAt(character.getDeletedAt());
        response.setCanRestoreUntil(character.getRestoreUntil());
        response.setSlots(toSlotStateDto(slotState, Math.toIntExact(activeCount)));
        return response;
    }

    @Override
    public CharacterRecalculateResponse charactersPlayersAccountsAccountIdCharactersCharacterIdRecalculatePost(UUID accountId,
                                                                                                              UUID characterId,
                                                                                                              CharacterStatsRecalculateRequest request) {
        AccountEntity account = fetchAccount(accountId);
        CharacterEntity character = fetchCharacter(account, characterId);
        UUID jobId = UUID.randomUUID();
        recordActivity(account, character, CharacterActivityLogEntity.ActivityType.stats,
            Map.of("jobId", jobId.toString()));
        CharacterRecalculateResponse response = new CharacterRecalculateResponse();
        response.setCharacterId(character.getId());
        response.setJobId(jobId);
        CharacterStatsUpdatedEventPayload payload = new CharacterStatsUpdatedEventPayload();
        payload.setAccountId(account.getId());
        payload.setCharacterId(character.getId());
        payload.setRecalculatedAt(OffsetDateTime.now());
        payload.setDelta(Collections.emptyMap());
        CharacterStatsUpdatedEvent event = new CharacterStatsUpdatedEvent();
        event.setTopic("characters.stats.recalculated");
        event.setProducer(PRODUCER);
        event.setPayload(payload);
        event.setConsumers(List.of(CharacterStatsUpdatedEvent.ConsumersEnum.GAMEPLAY_SERVICE,
            CharacterStatsUpdatedEvent.ConsumersEnum.TELEMETRY));
        response.setEvents(List.of(event));
        return response;
    }

    @Override
    public CharacterRestoreResponse charactersPlayersAccountsAccountIdCharactersCharacterIdRestorePost(UUID accountId,
                                                                                                       UUID characterId,
                                                                                                       CharacterRestoreRequest request) {
        AccountEntity account = fetchAccount(accountId);
        CharacterEntity character = fetchCharacter(account, characterId);
        if (!character.isDeleted()) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "character is not deleted");
        }
        if (character.getRestoreUntil() != null && character.getRestoreUntil().isBefore(OffsetDateTime.now())) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "restore window expired");
        }
        CharacterRestoreQueueEntity queueEntity = restoreQueueRepository
            .findByCharacterIdAndStatus(character.getId(), CharacterRestoreQueueEntity.RestoreStatus.pending)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "restore queue entry not found"));
        character.setDeleted(false);
        character.setStatus(CharacterEntity.LifecycleStatus.ACTIVE);
        character.setRestoreUntil(null);
        character.setLastActiveAt(OffsetDateTime.now());
        characterRepository.save(character);
        queueEntity.setStatus(CharacterRestoreQueueEntity.RestoreStatus.completed);
        queueEntity.setReason(Optional.ofNullable(request).map(CharacterRestoreRequest::getReason).orElse(null));
        restoreQueueRepository.save(queueEntity);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        long activeCount = characterRepository.countByAccountIdAndDeletedFalse(account.getId());
        slotState.setUsedSlots(Math.toIntExact(activeCount));
        slotStateRepository.save(slotState);
        recordActivity(account, character, CharacterActivityLogEntity.ActivityType.restoration,
            Map.of("paymentReference", Optional.ofNullable(request).map(CharacterRestoreRequest::getPaymentReference).orElse(null)));
        CharacterRestoreResponse response = new CharacterRestoreResponse();
        response.setCharacter(toSummary(character, slotState));
        response.setRestoreQueueEntry(toRestoreEntry(queueEntity));
        CharacterRestoredEventPayload payload = new CharacterRestoredEventPayload();
        payload.setCharacterId(character.getId());
        payload.setAccountId(account.getId());
        payload.setRestoredAt(OffsetDateTime.now());
        CharacterRestoredEvent event = new CharacterRestoredEvent();
        event.setTopic("characters.lifecycle.restored");
        event.setProducer(PRODUCER);
        event.setPayload(payload);
        event.setConsumers(List.of(CharacterRestoredEvent.ConsumersEnum.GAMEPLAY_SERVICE,
            CharacterRestoredEvent.ConsumersEnum.NOTIFICATION_SERVICE));
        response.setEvents(List.of(event));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterListResponse charactersPlayersAccountsAccountIdCharactersGet(UUID accountId,
                                                                                 Boolean includeDeleted,
                                                                                 Boolean includeSnapshots) {
        AccountEntity account = fetchAccount(accountId);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        PlayerProfileEntity profile = ensureProfile(account);
        List<CharacterEntity> allCharacters = characterRepository.findAllByAccountId(account.getId());
        boolean showDeleted = Optional.ofNullable(includeDeleted).orElse(false);
        List<CharacterEntity> filtered = allCharacters.stream()
            .filter(entity -> showDeleted || !entity.isDeleted())
            .collect(Collectors.toList());
        long activeCount = allCharacters.stream().filter(entity -> !entity.isDeleted()).count();
        if (slotState.getUsedSlots() == null || slotState.getUsedSlots() != (int) activeCount) {
            slotState.setUsedSlots((int) activeCount);
            slotStateRepository.save(slotState);
        }
        CharacterSlotState slotStateDto = toSlotStateDto(slotState, (int) activeCount);
        List<CharacterSummary> summaries = filtered.stream()
            .map(entity -> toSummary(entity, slotState))
            .collect(Collectors.toList());
        List<CharacterRestoreQueueEntity> queue = restoreQueueRepository.findByAccountIdAndStatus(account.getId(),
            CharacterRestoreQueueEntity.RestoreStatus.pending);
        List<RestoreQueueEntry> restoreEntries = queue.stream()
            .map(this::toRestoreEntry)
            .collect(Collectors.toList());
        List<StateSnapshotRef> snapshots = new ArrayList<>();
        if (Boolean.TRUE.equals(includeSnapshots)) {
            filtered.forEach(entity -> snapshots.addAll(snapshotRepository
                .findTop10ByCharacterIdOrderByTakenAtDesc(entity.getId()).stream()
                .map(this::toSnapshotRef)
                .collect(Collectors.toList())));
        }
        CharacterListResponse response = new CharacterListResponse();
        response.setData(summaries);
        response.setSlots(slotStateDto);
        response.setPlayer(toProfileDto(profile));
        response.setRestoreQueue(restoreEntries);
        response.setSnapshots(snapshots);
        return response;
    }

    @Override
    public CharacterCreateResponse charactersPlayersAccountsAccountIdCharactersPost(UUID accountId,
                                                                                    CharacterCreateRequest request) {
        AccountEntity account = fetchAccount(accountId);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        long activeCount = characterRepository.countByAccountIdAndDeletedFalse(account.getId());
        if (slotState.getUsedSlots() != null && slotState.getUsedSlots() >= slotState.getTotalSlots()) {
            throw new BusinessException(ErrorCode.LIMIT_EXCEEDED, "slot limit reached");
        }
        if (characterRepository.existsByNameAndAccountIdAndDeletedFalse(request.getName(), account.getId())) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS, "character name already used");
        }
        if (request.getAppearance() == null) {
            throw new BusinessException(ErrorCode.MISSING_REQUIRED_FIELD, "appearance is required");
        }
        CharacterEntity character = new CharacterEntity();
        character.setAccount(account);
        character.setName(request.getName());
        character.setClassCode(request.getCharacterClass().getValue());
        character.setGender(CharacterEntity.Gender.other);
        character.setOriginCode(request.getOrigin().getValue());
        character.setSubclassCode(null);
        character.setFaction(null);
        character.setCity(null);
        character.setAppearance(createAppearance(request.getAppearance()));
        character.setLevel(1);
        character.setStatus(CharacterEntity.LifecycleStatus.ACTIVE);
        OffsetDateTime now = OffsetDateTime.now();
        character.setLastActiveAt(now);
        character.setLastLogin(now);
        character = characterRepository.save(character);
        long updatedActiveCount = activeCount + 1;
        slotState.setUsedSlots((int) updatedActiveCount);
        slotStateRepository.save(slotState);
        recordActivity(account, character, CharacterActivityLogEntity.ActivityType.creation,
            Map.of("seed", request.getSeed()));
        CharacterCreateResponse response = new CharacterCreateResponse();
        response.setCharacter(toSummary(character, slotState));
        response.setSlots(toSlotStateDto(slotState, (int) updatedActiveCount));
        CharacterCreatedEventPayload payload = new CharacterCreatedEventPayload();
        payload.setCharacterId(character.getId());
        payload.setAccountId(account.getId());
        payload.setName(character.getName());
        payload.setOrigin(character.getOriginCode());
        payload.setCharacterClass(character.getClassCode());
        payload.setCreatedAt(now);
        CharacterCreatedEvent event = new CharacterCreatedEvent();
        event.setTopic("characters.lifecycle.created");
        event.setProducer(PRODUCER);
        event.setPayload(payload);
        event.setConsumers(List.of(CharacterCreatedEvent.ConsumersEnum.GAMEPLAY_SERVICE,
            CharacterCreatedEvent.ConsumersEnum.ECONOMY_SERVICE,
            CharacterCreatedEvent.ConsumersEnum.TELEMETRY));
        response.setEvents(List.of(event));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterSlotStateResponse charactersPlayersAccountsAccountIdSlotsGet(UUID accountId) {
        AccountEntity account = fetchAccount(accountId);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        long activeCount = characterRepository.countByAccountIdAndDeletedFalse(account.getId());
        slotState.setUsedSlots((int) activeCount);
        slotStateRepository.save(slotState);
        List<CharacterSlotPaymentEntity> pending = slotPaymentRepository.findByAccountIdAndStatusIn(account.getId(),
            List.of(CharacterSlotPaymentEntity.PaymentStatus.pending));
        CharacterSlotStateResponse response = new CharacterSlotStateResponse();
        response.setSlots(toSlotStateDto(slotState, (int) activeCount));
        List<CharacterSlotStateResponsePendingPaymentsInner> payments = pending.stream()
            .map(this::toPendingPayment)
            .collect(Collectors.toList());
        response.setPendingPayments(payments);
        return response;
    }

    @Override
    public CharacterSlotPurchaseResponse charactersPlayersAccountsAccountIdSlotsPurchasePost(UUID accountId,
                                                                                             CharacterSlotPurchaseRequest request) {
        AccountEntity account = fetchAccount(accountId);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        long activeCount = characterRepository.countByAccountIdAndDeletedFalse(account.getId());
        slotState.setUsedSlots((int) activeCount);
        boolean immediate = request.getPaymentMethod() == CharacterSlotPurchaseRequest.PaymentMethodEnum.WALLET;
        CharacterSlotPurchaseResponse response = new CharacterSlotPurchaseResponse();
        if (immediate) {
            slotState.setPremiumSlotsPurchased(slotState.getPremiumSlotsPurchased() + 1);
            slotState.setTotalSlots(slotState.getTotalSlots() + 1);
            slotStateRepository.save(slotState);
            CharacterSlotPaymentEntity payment = new CharacterSlotPaymentEntity();
            payment.setAccount(account);
            payment.setReferenceId(UUID.randomUUID().toString());
            payment.setAmount(request.getAmount());
            payment.setStatus(CharacterSlotPaymentEntity.PaymentStatus.completed);
            slotPaymentRepository.save(payment);
            response.setTransactionId(payment.getId().toString());
            response.setStatus(CharacterSlotPurchaseResponse.StatusEnum.COMPLETED);
        } else {
            CharacterSlotPaymentEntity payment = new CharacterSlotPaymentEntity();
            payment.setAccount(account);
            payment.setReferenceId(UUID.randomUUID().toString());
            payment.setAmount(request.getAmount());
            payment.setStatus(CharacterSlotPaymentEntity.PaymentStatus.pending);
            slotPaymentRepository.save(payment);
            response.setTransactionId(payment.getId().toString());
            response.setStatus(CharacterSlotPurchaseResponse.StatusEnum.PENDING);
            response.setEconomyLink(JsonNullable.of(URI.create("https://pay.necp.game/transactions/" + payment.getId())));
        }
        recordActivity(account, null, CharacterActivityLogEntity.ActivityType.slot,
            Map.of("paymentMethod", request.getPaymentMethod().getValue()));
        response.setSlots(toSlotStateDto(slotState, slotState.getUsedSlots()));
        return response;
    }

    @Override
    public CharacterSwitchResponse charactersPlayersAccountsAccountIdSwitchPost(UUID accountId,
                                                                                CharacterSwitchRequest request) {
        AccountEntity account = fetchAccount(accountId);
        CharacterSlotStateEntity slotState = ensureSlotState(account);
        CharacterEntity target = fetchCharacter(account, request.getCharacterId());
        CharacterEntity previous = null;
        if (slotState.getActiveCharacterId() != null) {
            previous = characterRepository.findById(slotState.getActiveCharacterId()).orElse(null);
        }
        slotState.setActiveCharacterId(target.getId());
        slotStateRepository.save(slotState);
        CharacterSnapshotEntity snapshot = new CharacterSnapshotEntity();
        snapshot.setCharacter(target);
        snapshot.setReason("switch");
        snapshotRepository.save(snapshot);
        recordActivity(account, target, CharacterActivityLogEntity.ActivityType.switch_action,
            Map.of("suppressNotifications", Optional.ofNullable(request.getSuppressNotifications()).orElse(false)));
        CharacterSwitchResponse response = new CharacterSwitchResponse();
        response.setActiveCharacterId(target.getId());
        response.setSnapshot(toSnapshotRef(snapshot));
        CharacterSwitchedEventPayload payload = new CharacterSwitchedEventPayload();
        payload.setAccountId(account.getId());
        payload.setNewCharacterId(target.getId());
        payload.setPreviousCharacterId(previous != null ? previous.getId() : null);
        payload.setSwitchedAt(OffsetDateTime.now());
        CharacterSwitchedEvent event = new CharacterSwitchedEvent();
        event.setTopic("characters.lifecycle.switched");
        event.setProducer(PRODUCER);
        event.setPayload(payload);
        event.setConsumers(List.of(CharacterSwitchedEvent.ConsumersEnum.GAMEPLAY_SERVICE,
            CharacterSwitchedEvent.ConsumersEnum.TELEMETRY));
        response.setEvents(List.of(event));
        return response;
    }

    private AccountEntity fetchAccount(UUID accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "account not found"));
    }

    private CharacterEntity fetchCharacter(AccountEntity account, UUID characterId) {
        return characterRepository.findByIdAndAccountId(characterId, account.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "character not found"));
    }

    private CharacterSlotStateEntity ensureSlotState(AccountEntity account) {
        return slotStateRepository.findByAccountId(account.getId())
            .orElseGet(() -> {
                CharacterSlotStateEntity entity = new CharacterSlotStateEntity();
                entity.setAccount(account);
                entity.setAccountId(account.getId());
                entity.setTotalSlots(DEFAULT_TOTAL_SLOTS);
                entity.setUsedSlots(0);
                entity.setPremiumSlotsPurchased(0);
                entity.setMaxSlots(DEFAULT_MAX_SLOTS);
                return slotStateRepository.save(entity);
            });
    }

    private PlayerProfileEntity ensureProfile(AccountEntity account) {
        return playerProfileRepository.findByAccountId(account.getId())
            .orElseGet(() -> {
                PlayerProfileEntity profile = new PlayerProfileEntity();
                profile.setAccount(account);
                profile.setAccountId(account.getId());
                profile.setPremiumCurrency(0);
                profile.setTotalPlaytimeSeconds(0);
                profile.setLanguage("ru");
                profile.setTimezone("Europe/Moscow");
                profile.setFriendsJson(writeJson(Collections.emptyList()));
                profile.setBlockedJson(writeJson(Collections.emptyList()));
                return playerProfileRepository.save(profile);
            });
    }

    private CharacterSlotState toSlotStateDto(CharacterSlotStateEntity state, int used) {
        CharacterSlotState dto = new CharacterSlotState();
        dto.setTotalSlots(state.getTotalSlots());
        dto.setUsedSlots(used);
        dto.setPremiumSlotsPurchased(state.getPremiumSlotsPurchased());
        dto.setMaxSlots(state.getMaxSlots());
        CharacterSlotStateNextTierCost cost = toNextTierCost(state);
        if (cost != null) {
            dto.setNextTierCost(JsonNullable.of(cost));
        } else {
            dto.setNextTierCost(JsonNullable.undefined());
        }
        dto.setRestrictions(readStringList(state.getRestrictionsJson()));
        return dto;
    }

    private CharacterSlotStateNextTierCost toNextTierCost(CharacterSlotStateEntity state) {
        if (state.getNextTierCurrency() == null && state.getNextTierAmount() == null && state.getNextTierRequiresApproval() == null) {
            return null;
        }
        CharacterSlotStateNextTierCost cost = new CharacterSlotStateNextTierCost();
        cost.setCurrency(state.getNextTierCurrency());
        cost.setAmount(state.getNextTierAmount());
        cost.setRequiresApproval(state.getNextTierRequiresApproval());
        return cost;
    }

    private CharacterActivityListResponse mapActivityPage(Page<CharacterActivityLogEntity> page) {
        CharacterActivityListResponse response = new CharacterActivityListResponse();
        List<CharacterActivityEntry> entries = page.getContent().stream()
            .map(this::toActivityEntry)
            .collect(Collectors.toList());
        response.setData(entries);
        PaginationMeta meta = new PaginationMeta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setTotal(Math.toIntExact(page.getTotalElements()));
        meta.setTotalPages(page.getTotalPages());
        meta.setHasNext(page.hasNext());
        meta.setHasPrev(page.hasPrevious());
        response.setPagination(meta);
        return response;
    }

    private CharacterActivityEntry toActivityEntry(CharacterActivityLogEntity entity) {
        CharacterActivityEntry dto = new CharacterActivityEntry();
        dto.setActivityId(entity.getId());
        dto.setActivityType(mapActivityTypeDto(entity.getActivityType()));
        dto.setOccurredAt(entity.getOccurredAt());
        CharacterActivityEntryActor actor = new CharacterActivityEntryActor();
        actor.setActorType(mapActorTypeDto(entity.getActorType()));
        actor.setActorId(entity.getActorId());
        dto.setActor(actor);
        dto.setIpAddress(entity.getIpAddress() == null ? JsonNullable.undefined() : JsonNullable.of(entity.getIpAddress()));
        dto.setLocation(entity.getLocation());
        dto.setMetadata(readMetadata(entity.getMetadataJson()));
        return dto;
    }

    private CharacterActivityLogEntity.ActivityType mapActivityType(String raw) {
        String normalized = raw.trim().toLowerCase();
        switch (normalized) {
            case "creation":
                return CharacterActivityLogEntity.ActivityType.creation;
            case "deletion":
                return CharacterActivityLogEntity.ActivityType.deletion;
            case "restoration":
                return CharacterActivityLogEntity.ActivityType.restoration;
            case "switch":
                return CharacterActivityLogEntity.ActivityType.switch_action;
            case "appearance":
                return CharacterActivityLogEntity.ActivityType.appearance;
            case "stats":
                return CharacterActivityLogEntity.ActivityType.stats;
            case "slot":
                return CharacterActivityLogEntity.ActivityType.slot;
            case "moderator":
                return CharacterActivityLogEntity.ActivityType.moderator;
            default:
                throw new BusinessException(ErrorCode.INVALID_INPUT, "unknown activity type: " + raw);
        }
    }

    private CharacterActivityEntry.ActivityTypeEnum mapActivityTypeDto(CharacterActivityLogEntity.ActivityType type) {
        return switch (type) {
            case creation -> CharacterActivityEntry.ActivityTypeEnum.CREATION;
            case deletion -> CharacterActivityEntry.ActivityTypeEnum.DELETION;
            case restoration -> CharacterActivityEntry.ActivityTypeEnum.RESTORATION;
            case switch_action -> CharacterActivityEntry.ActivityTypeEnum.SWITCH;
            case appearance -> CharacterActivityEntry.ActivityTypeEnum.APPEARANCE;
            case stats -> CharacterActivityEntry.ActivityTypeEnum.STATS;
            case slot -> CharacterActivityEntry.ActivityTypeEnum.SLOT;
            case moderator -> CharacterActivityEntry.ActivityTypeEnum.MODERATOR;
        };
    }

    private CharacterActivityEntryActor.ActorTypeEnum mapActorTypeDto(CharacterActivityLogEntity.ActorType type) {
        return switch (type) {
            case player -> CharacterActivityEntryActor.ActorTypeEnum.PLAYER;
            case moderator -> CharacterActivityEntryActor.ActorTypeEnum.MODERATOR;
            case system -> CharacterActivityEntryActor.ActorTypeEnum.SYSTEM;
        };
    }

    private CharacterSummary toSummary(CharacterEntity entity, CharacterSlotStateEntity slotState) {
        CharacterSummary summary = new CharacterSummary();
        summary.setCharacterId(entity.getId());
        summary.setName(entity.getName());
        summary.setOrigin(mapOrigin(entity.getOriginCode()));
        summary.setCharacterClass(mapClass(entity.getClassCode()));
        summary.setLevel(entity.getLevel());
        summary.setStatus(mapStatus(entity.getStatus()));
        summary.setCurrentLocation(null);
        summary.setLastActiveAt(Optional.ofNullable(entity.getLastActiveAt()).orElse(entity.getCreatedAt()));
        summary.setDeleted(entity.isDeleted());
        if (entity.getDeletedAt() != null) {
            summary.setDeletedAt(JsonNullable.of(entity.getDeletedAt()));
        } else {
            summary.setDeletedAt(JsonNullable.undefined());
        }
        if (entity.getRestoreUntil() != null) {
            summary.setCanRestoreUntil(JsonNullable.of(entity.getRestoreUntil()));
        } else {
            summary.setCanRestoreUntil(JsonNullable.undefined());
        }
        summary.setSlotState(toSlotStateDto(slotState, slotState.getUsedSlots()));
        summary.setTags(Collections.emptyList());
        return summary;
    }

    private CharacterSummary.OriginEnum mapOrigin(String origin) {
        String normalized = origin == null ? "custom" : origin.replace("-", "_").replace(" ", "_").toUpperCase();
        try {
            return CharacterSummary.OriginEnum.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return CharacterSummary.OriginEnum.CUSTOM;
        }
    }

    private CharacterSummary.CharacterClassEnum mapClass(String value) {
        String normalized = value == null ? "SOLO" : value.toUpperCase();
        try {
            return CharacterSummary.CharacterClassEnum.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return CharacterSummary.CharacterClassEnum.SOLO;
        }
    }

    private CharacterSummary.StatusEnum mapStatus(CharacterEntity.LifecycleStatus status) {
        return switch (status) {
            case ACTIVE -> CharacterSummary.StatusEnum.ACTIVE;
            case IN_COMBAT -> CharacterSummary.StatusEnum.IN_COMBAT;
            case AFK -> CharacterSummary.StatusEnum.AFK;
            case DEAD -> CharacterSummary.StatusEnum.DEAD;
            case DELETED -> CharacterSummary.StatusEnum.DELETED;
        };
    }

    private CharacterAppearanceEntity createAppearance(CharacterAppearance source) {
        CharacterAppearanceEntity entity = new CharacterAppearanceEntity();
        entity.setHeight(180);
        entity.setBodyType(mapBodyType(source.getBodyType().getValue()));
        entity.setHairColor(source.getHairColor() != null ? source.getHairColor() : "#000000");
        entity.setHairStyle(source.getHairStyle());
        entity.setEyeColor(source.getEyeColor());
        entity.setSkinColor(source.getSkinTone());
        entity.setDistinctiveFeatures(null);
        entity.setTattoosJson(writeJson(Optional.ofNullable(source.getTattoos()).orElse(Collections.emptyList())));
        entity.setScarsJson(writeJson(Optional.ofNullable(source.getScars()).orElse(Collections.emptyList())));
        entity.setImplantsVisibleJson(writeJson(Optional.ofNullable(source.getImplantsVisible()).orElse(Collections.emptyList())));
        entity.setMakeupPreset(source.getMakeupPreset().isPresent() ? source.getMakeupPreset().get() : null);
        return entity;
    }

    private void applyAppearance(CharacterAppearanceEntity target, CharacterAppearance source) {
        target.setBodyType(mapBodyType(source.getBodyType().getValue()));
        if (source.getHairColor() != null) {
            target.setHairColor(source.getHairColor());
        }
        target.setHairStyle(source.getHairStyle());
        target.setEyeColor(source.getEyeColor());
        target.setSkinColor(source.getSkinTone());
        target.setTattoosJson(writeJson(Optional.ofNullable(source.getTattoos()).orElse(Collections.emptyList())));
        target.setScarsJson(writeJson(Optional.ofNullable(source.getScars()).orElse(Collections.emptyList())));
        target.setImplantsVisibleJson(writeJson(Optional.ofNullable(source.getImplantsVisible()).orElse(Collections.emptyList())));
        target.setMakeupPreset(source.getMakeupPreset().isPresent() ? source.getMakeupPreset().get() : null);
    }

    private CharacterAppearance toAppearanceDto(CharacterAppearanceEntity entity) {
        CharacterAppearance dto = new CharacterAppearance();
        dto.setBodyType(CharacterAppearance.BodyTypeEnum.fromValue(entity.getBodyType().name().toLowerCase()));
        dto.setSkinTone(entity.getSkinColor());
        dto.setHairStyle(entity.getHairStyle());
        dto.setHairColor(entity.getHairColor());
        dto.setEyeColor(entity.getEyeColor());
        dto.setTattoos(readStringList(entity.getTattoosJson()));
        dto.setScars(readStringList(entity.getScarsJson()));
        dto.setImplantsVisible(readStringList(entity.getImplantsVisibleJson()));
        if (entity.getMakeupPreset() != null) {
            dto.setMakeupPreset(JsonNullable.of(entity.getMakeupPreset()));
        }
        return dto;
    }

    private CharacterSlotStateResponsePendingPaymentsInner toPendingPayment(CharacterSlotPaymentEntity payment) {
        CharacterSlotStateResponsePendingPaymentsInner dto = new CharacterSlotStateResponsePendingPaymentsInner();
        dto.setReferenceId(payment.getReferenceId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus() == CharacterSlotPaymentEntity.PaymentStatus.pending ?
            CharacterSlotStateResponsePendingPaymentsInner.StatusEnum.PENDING :
            CharacterSlotStateResponsePendingPaymentsInner.StatusEnum.COMPLETED);
        return dto;
    }

    private RestoreQueueEntry toRestoreEntry(CharacterRestoreQueueEntity entity) {
        RestoreQueueEntry dto = new RestoreQueueEntry();
        dto.setCharacterId(entity.getCharacter().getId());
        dto.setQueuedAt(entity.getQueuedAt());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setReason(entity.getReason());
        return dto;
    }

    private StateSnapshotRef toSnapshotRef(CharacterSnapshotEntity entity) {
        StateSnapshotRef dto = new StateSnapshotRef();
        dto.setSnapshotId(entity.getId());
        dto.setTakenAt(entity.getTakenAt());
        dto.setReason(entity.getReason());
        return dto;
    }

    private PlayerProfile toProfileDto(PlayerProfileEntity entity) {
        PlayerProfile dto = new PlayerProfile();
        dto.setAccountId(entity.getAccountId());
        dto.setPremiumCurrency(entity.getPremiumCurrency());
        dto.setTotalPlaytimeSeconds(entity.getTotalPlaytimeSeconds());
        dto.setLanguage(entity.getLanguage());
        dto.setTimezone(entity.getTimezone());
        PlayerProfileSettings settings = new PlayerProfileSettings();
        settings.setUi(readMetadata(entity.getSettingsUiJson()));
        settings.setAudio(readMetadata(entity.getSettingsAudioJson()));
        settings.setGraphics(readMetadata(entity.getSettingsGraphicsJson()));
        dto.setSettings(settings);
        PlayerProfileSocial social = new PlayerProfileSocial();
        social.setFriends(readUuidList(entity.getFriendsJson()));
        social.setBlocked(readUuidList(entity.getBlockedJson()));
        dto.setSocial(social);
        return dto;
    }

    private void recordActivity(AccountEntity account, CharacterEntity character,
                                CharacterActivityLogEntity.ActivityType type,
                                Map<String, Object> metadata) {
        CharacterActivityLogEntity entry = new CharacterActivityLogEntity();
        entry.setAccount(account);
        entry.setCharacter(character);
        entry.setActivityType(type);
        entry.setActorType(CharacterActivityLogEntity.ActorType.system);
        entry.setActorId(null);
        entry.setIpAddress(null);
        entry.setLocation(null);
        entry.setMetadataJson(writeJson(metadata));
        activityLogRepository.save(entry);
    }

    private String writeJson(Object source) {
        try {
            Object value = source;
            if (value == null) {
                value = Collections.emptyMap();
            }
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("failed to serialize payload", ex);
        }
    }

    private Map<String, Object> readMetadata(String source) {
        if (source == null || source.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(source, MAP_TYPE);
        } catch (Exception ex) {
            throw new IllegalStateException("failed to read metadata", ex);
        }
    }

    private List<String> readStringList(String source) {
        if (source == null || source.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(source, STRING_LIST_TYPE);
        } catch (Exception ex) {
            throw new IllegalStateException("failed to read list", ex);
        }
    }

    private List<UUID> readUuidList(String source) {
        return readStringList(source).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    private CharacterAppearanceEntity.BodyType mapBodyType(String raw) {
        String normalized = raw == null ? "slim" : raw.toLowerCase();
        try {
            return CharacterAppearanceEntity.BodyType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return CharacterAppearanceEntity.BodyType.slim;
        }
    }
}

