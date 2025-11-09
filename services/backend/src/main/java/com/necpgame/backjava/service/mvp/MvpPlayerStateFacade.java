package com.necpgame.backjava.service.mvp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterInventoryEntity;
import com.necpgame.backjava.entity.CharacterInventoryEntity.StorageType;
import com.necpgame.backjava.entity.CharacterLocationEntity;
import com.necpgame.backjava.entity.CharacterStatsEntity;
import com.necpgame.backjava.entity.CharacterStatusEntity;
import com.necpgame.backjava.entity.NotificationEntity;
import com.necpgame.backjava.entity.PartyEntity;
import com.necpgame.backjava.entity.QuestEntity;
import com.necpgame.backjava.entity.QuestProgressEntity;
import com.necpgame.backjava.entity.mvp.MvpWorldStateEntity;
import com.necpgame.backjava.mapper.MvpContentMapper;
import com.necpgame.backjava.model.MainGameUIData;
import com.necpgame.backjava.model.MainGameUIDataActiveQuestsInner;
import com.necpgame.backjava.model.MainGameUIDataCharacter;
import com.necpgame.backjava.model.MainGameUIDataInventory;
import com.necpgame.backjava.model.MainGameUIDataInventorySlotsInner;
import com.necpgame.backjava.model.MainGameUIDataNotificationsInner;
import com.necpgame.backjava.model.MainGameUIDataParty;
import com.necpgame.backjava.model.MainGameUIDataPartyMembersInner;
import com.necpgame.backjava.model.MainGameUIDataWorldState;
import com.necpgame.backjava.model.MainGameUIDataWorldStateActiveEventsInner;
import com.necpgame.backjava.model.TextVersionState;
import com.necpgame.backjava.model.TextVersionStateAvailableActionsInner;
import com.necpgame.backjava.model.TextVersionStateCharacter;
import com.necpgame.backjava.model.TextVersionStateCurrentQuest;
import com.necpgame.backjava.model.TextVersionStateInventorySummary;
import com.necpgame.backjava.model.TextVersionStateNearbyNpcsInner;
import com.necpgame.backjava.repository.CharacterInventoryRepository;
import com.necpgame.backjava.repository.CharacterLocationRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterStatsRepository;
import com.necpgame.backjava.repository.CharacterStatusRepository;
import com.necpgame.backjava.repository.GameLocationRepository;
import com.necpgame.backjava.repository.NotificationRepository;
import com.necpgame.backjava.repository.PartyRepository;
import com.necpgame.backjava.repository.QuestProgressRepository;
import com.necpgame.backjava.repository.QuestRepository;
import com.necpgame.backjava.repository.mvp.MvpTextActionRepository;
import com.necpgame.backjava.repository.mvp.MvpTextNearbyNpcRepository;
import com.necpgame.backjava.repository.mvp.MvpWorldStateRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MvpPlayerStateFacade {

    private static final int MAX_NOTIFICATIONS = 5;

    private final CharacterRepository characterRepository;
    private final CharacterStatusRepository characterStatusRepository;
    private final CharacterStatsRepository characterStatsRepository;
    private final CharacterInventoryRepository characterInventoryRepository;
    private final CharacterLocationRepository characterLocationRepository;
    private final QuestProgressRepository questProgressRepository;
    private final QuestRepository questRepository;
    private final NotificationRepository notificationRepository;
    private final PartyRepository partyRepository;
    private final GameLocationRepository gameLocationRepository;
    private final MvpTextActionRepository textActionRepository;
    private final MvpTextNearbyNpcRepository textNearbyNpcRepository;
    private final MvpWorldStateRepository worldStateRepository;
    private final MvpContentMapper mapper;
    private final ObjectMapper objectMapper;

    public TextVersionState getTextVersionState(UUID characterId) {
        CharacterEntity character = fetchActiveCharacter(characterId);
        CharacterStatusEntity status = characterStatusRepository.findByCharacterId(characterId).orElse(null);
        CharacterLocationEntity location = characterLocationRepository.findByCharacterId(characterId).orElse(null);

        TextVersionStateCharacter characterBlock = new TextVersionStateCharacter()
            .name(character.getName())
            .level(character.getLevel())
            .location(resolveLocationName(location == null ? null : location.getCurrentLocationId()));

        if (status != null) {
            characterBlock
                .hp(status.getHealth())
                .hpMax(status.getMaxHealth());
        }

        List<TextVersionStateAvailableActionsInner> actions = textActionRepository.findAllByOrderByActionAsc().stream()
            .map(mapper::toTextVersionAction)
            .toList();

        List<TextVersionStateNearbyNpcsInner> nearbyNpcs = location == null
            ? List.of()
            : textNearbyNpcRepository.findByLocationIdOrderByNpcNameAsc(location.getCurrentLocationId()).stream()
                .map(mapper::toTextVersionNearbyNpc)
                .toList();

        TextVersionStateInventorySummary inventorySummary = buildInventorySummary(characterId);

        TextVersionState state = new TextVersionState()
            .character(characterBlock)
            .availableActions(actions)
            .inventorySummary(inventorySummary)
            .nearbyNpcs(nearbyNpcs);
        state.setCurrentQuest(buildCurrentQuestBlock(characterId));
        return state;
    }

    public MainGameUIData getMainGameUi(UUID characterId) {
        CharacterEntity character = fetchActiveCharacter(characterId);
        CharacterStatusEntity status = characterStatusRepository.findByCharacterId(characterId).orElse(null);
        CharacterStatsEntity stats = characterStatsRepository.findByCharacterId(characterId).orElse(null);

        Map<String, Integer> attributes = new HashMap<>();
        if (stats != null) {
            attributes.put("strength", stats.getStrength());
            attributes.put("reflexes", stats.getReflexes());
            attributes.put("intelligence", stats.getIntelligence());
            attributes.put("technical", stats.getTechnical());
            attributes.put("cool", stats.getCool());
        }

        MainGameUIDataCharacter characterBlock = new MainGameUIDataCharacter()
            .name(character.getName())
            .level(character.getLevel())
            .experience(status == null ? null : status.getExperience())
            .hp(status == null ? null : status.getHealth())
            .attributes(attributes);

        MainGameUIData data = new MainGameUIData()
            .character(characterBlock)
            .activeQuests(buildActiveQuestSummary(characterId))
            .inventory(buildInventoryBlock(characterId))
            .notifications(buildNotificationSummary(character.getPlayer().getId()))
            .worldState(buildWorldState());

        data.setParty(buildPartyInfo(characterId));
        return data;
    }

    private CharacterEntity fetchActiveCharacter(UUID characterId) {
        return characterRepository.findById(characterId)
            .filter(character -> !character.isDeleted())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Персонаж не найден или удален"));
    }

    private String resolveLocationName(String locationId) {
        if (locationId == null) {
            return null;
        }
        return gameLocationRepository.findById(locationId)
            .map(location -> location.getName())
            .orElse(locationId);
    }

    private TextVersionStateInventorySummary buildInventorySummary(UUID characterId) {
        List<CharacterInventoryEntity> backpackItems =
            characterInventoryRepository.findByCharacterIdAndStorageTypeOrderBySlotPosition(characterId, StorageType.BACKPACK);

        int itemsCount = backpackItems.stream()
            .map(CharacterInventoryEntity::getQuantity)
            .filter(quantity -> quantity != null && quantity > 0)
            .mapToInt(Integer::intValue)
            .sum();

        BigDecimal weight = characterInventoryRepository.calculateTotalWeight(
            characterId,
            List.of(StorageType.BACKPACK, StorageType.EQUIPPED)
        );

        return new TextVersionStateInventorySummary()
            .itemsCount(itemsCount)
            .weight(weight);
    }

    private JsonNullable<TextVersionStateCurrentQuest> buildCurrentQuestBlock(UUID characterId) {
        List<QuestProgressEntity> activeQuests = new ArrayList<>(questProgressRepository.findActiveQuestsByCharacterId(characterId));
        activeQuests.sort(Comparator.comparing(QuestProgressEntity::getStartedAt, Comparator.nullsLast(Comparator.naturalOrder())));

        if (activeQuests.isEmpty()) {
            return JsonNullable.undefined();
        }

        QuestProgressEntity progress = activeQuests.getFirst();
        String questName = questRepository.findById(progress.getQuestId())
            .map(QuestEntity::getName)
            .orElse(progress.getQuestId());

        TextVersionStateCurrentQuest quest = new TextVersionStateCurrentQuest()
            .questName(questName)
            .objectives(List.of("Прогресс: " + progress.getProgress() + "%"));
        return JsonNullable.of(quest);
    }

    private MainGameUIDataInventory buildInventoryBlock(UUID characterId) {
        List<CharacterInventoryEntity> backpackItems =
            characterInventoryRepository.findByCharacterIdAndStorageTypeOrderBySlotPosition(characterId, StorageType.BACKPACK);

        List<MainGameUIDataInventorySlotsInner> slots = backpackItems.stream()
            .sorted(Comparator.comparing(
                item -> Optional.ofNullable(item.getSlotPosition()).orElse(Integer.MAX_VALUE),
                Comparator.nullsLast(Comparator.naturalOrder())
            ))
            .map(item -> {
                String slotLabel = Optional.ofNullable(item.getSlotPosition())
                    .map(Object::toString)
                    .orElse(null);
                return new MainGameUIDataInventorySlotsInner()
                    .slotId(slotLabel)
                    .itemName(item.getItemId())
                    .quantity(item.getQuantity());
            })
            .toList();

        BigDecimal weight = characterInventoryRepository.calculateTotalWeight(
            characterId,
            List.of(StorageType.BACKPACK, StorageType.EQUIPPED)
        );

        MainGameUIDataInventory inventory = new MainGameUIDataInventory().slots(slots);
        if (weight != null) {
            inventory.weight(weight.floatValue());
        }
        return inventory;
    }

    private List<MainGameUIDataActiveQuestsInner> buildActiveQuestSummary(UUID characterId) {
        return questProgressRepository.findActiveQuestsByCharacterId(characterId).stream()
            .sorted(Comparator.comparing(QuestProgressEntity::getStartedAt, Comparator.nullsLast(Comparator.naturalOrder())))
            .map(progress -> {
                String questName = questRepository.findById(progress.getQuestId())
                    .map(QuestEntity::getName)
                    .orElse(progress.getQuestId());
                return new MainGameUIDataActiveQuestsInner()
                    .questId(progress.getQuestId())
                    .title(questName)
                    .status(mapQuestStatus(progress.getStatus()));
            })
            .toList();
    }

    private MainGameUIDataActiveQuestsInner.StatusEnum mapQuestStatus(QuestProgressEntity.QuestStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case ACTIVE -> MainGameUIDataActiveQuestsInner.StatusEnum.ACTIVE;
            case COMPLETED -> MainGameUIDataActiveQuestsInner.StatusEnum.COMPLETED;
            case FAILED, ABANDONED -> MainGameUIDataActiveQuestsInner.StatusEnum.FAILED;
        };
    }

    private List<MainGameUIDataNotificationsInner> buildNotificationSummary(UUID playerId) {
        return notificationRepository.findByPlayerIdAndIsReadFalse(playerId).stream()
            .sorted(Comparator.comparing(NotificationEntity::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
            .limit(MAX_NOTIFICATIONS)
            .map(this::toNotificationMap)
            .toList();
    }

    private MainGameUIDataNotificationsInner toNotificationMap(NotificationEntity entity) {
        MainGameUIDataNotificationsInner notification = new MainGameUIDataNotificationsInner()
            .message(entity.getMessage())
            .category(entity.getType())
            .createdAt(entity.getCreatedAt());
        Optional.ofNullable(entity.getPriority())
            .map(String::toUpperCase)
            .flatMap(priority -> {
                try {
                    return Optional.of(MainGameUIDataNotificationsInner.PriorityEnum.fromValue(priority));
                } catch (IllegalArgumentException ex) {
                    return Optional.empty();
                }
            })
            .ifPresent(notification::priority);
        return notification;
    }

    private JsonNullable<MainGameUIDataParty> buildPartyInfo(UUID characterId) {
        String characterIdString = characterId.toString();
        Optional<PartyEntity> partyOptional = partyRepository.findByLeaderCharacterId(characterIdString);
        if (partyOptional.isEmpty()) {
            partyOptional = partyRepository.findByMemberCharacterId(characterIdString);
        }

        if (partyOptional.isEmpty()) {
            return JsonNullable.undefined();
        }

        PartyEntity party = partyOptional.get();
        MainGameUIDataParty dto = new MainGameUIDataParty()
            .leaderId(parseUuidSafe(party.getLeaderCharacterId()))
            .members(parseMembers(party.getMembersJson()));
        return JsonNullable.of(dto);
    }

    private List<MainGameUIDataPartyMembersInner> parseMembers(String membersJson) {
        if (membersJson == null || membersJson.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(membersJson, new TypeReference<List<MainGameUIDataPartyMembersInner>>() { });
        } catch (Exception ex) {
            try {
                List<String> names = objectMapper.readValue(membersJson, new TypeReference<List<String>>() { });
                return names.stream()
                    .map(name -> new MainGameUIDataPartyMembersInner().name(name))
                    .toList();
            } catch (Exception ignored) {
                return List.of();
            }
        }
    }

    private MainGameUIDataWorldState buildWorldState() {
        Optional<MvpWorldStateEntity> stateOptional = worldStateRepository.findTopByOrderByCapturedAtDesc();
        if (stateOptional.isEmpty()) {
            return null;
        }
        MvpWorldStateEntity state = stateOptional.get();
        List<MainGameUIDataWorldStateActiveEventsInner> events = state.getActiveEvents().stream()
            .map(event -> new MainGameUIDataWorldStateActiveEventsInner()
                .eventId(event.getId() != null ? event.getId().toString() : null)
                .name(event.getEventName())
                .status(event.getSeverity()))
            .toList();
        return new MainGameUIDataWorldState()
            .timeOfDay(state.getTimeOfDay())
            .weather(state.getWeather())
            .activeEvents(events);
    }

    private UUID parseUuidSafe(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

