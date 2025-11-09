package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterLocationEntity;
import com.necpgame.backjava.entity.CharacterSlotEntity;
import com.necpgame.backjava.entity.CharacterStatsEntity;
import com.necpgame.backjava.entity.CharacterStatsSnapshotEntity;
import com.necpgame.backjava.entity.CharacterStatusEntity;
import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.entity.GameSessionEntity;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.mapper.PlayerCharacterMapper;
import com.necpgame.backjava.mapper.PlayerProfileMapper;
import com.necpgame.backjava.model.CreatePlayerCharacterRequest;
import com.necpgame.backjava.model.DeletePlayerCharacter200Response;
import com.necpgame.backjava.model.GetCharacters200Response;
import com.necpgame.backjava.model.PlayerCharacter;
import com.necpgame.backjava.model.PlayerCharacterDetails;
import com.necpgame.backjava.model.PlayerProfile;
import com.necpgame.backjava.model.SwitchCharacter200Response;
import com.necpgame.backjava.model.SwitchCharacterRequest;
import com.necpgame.backjava.repository.CharacterClassRepository;
import com.necpgame.backjava.repository.CharacterLocationRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.service.PlayersService;
import com.necpgame.backjava.service.player.CharacterLifecycleService;
import com.necpgame.backjava.service.player.CharacterSlotService;
import com.necpgame.backjava.service.player.GameSessionService;
import com.necpgame.backjava.service.player.PlayerAppearanceService;
import com.necpgame.backjava.service.player.PlayerContextService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayersServiceImpl implements PlayersService {

    private static final int RESTORE_GRACE_DAYS = 30;

    private final PlayerContextService playerContextService;
    private final PlayerAppearanceService playerAppearanceService;
    private final CharacterLifecycleService characterLifecycleService;
    private final CharacterSlotService characterSlotService;
    private final GameSessionService gameSessionService;
    private final CharacterRepository characterRepository;
    private final CharacterLocationRepository characterLocationRepository;
    private final CharacterClassRepository characterClassRepository;
    private final PlayerProfileMapper playerProfileMapper;
    private final PlayerCharacterMapper playerCharacterMapper;

    @Override
    @Transactional(readOnly = true)
    public PlayerProfile getPlayerProfile() {
        AccountEntity account = playerContextService.getCurrentAccount();
        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        return playerProfileMapper.toProfile(player);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCharacters200Response getCharacters(Boolean includeDeleted) {
        AccountEntity account = playerContextService.getCurrentAccount();
        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        List<CharacterSlotEntity> slots = characterSlotService.syncSlots(player);
        List<CharacterEntity> characters = characterRepository.findAllByAccountId(account.getId());
        Map<UUID, CharacterStatusEntity> statuses = characterLifecycleService.loadStatuses(characters);
        boolean showDeleted = Boolean.TRUE.equals(includeDeleted);

        List<PlayerCharacter> items = characters.stream()
            .filter(character -> showDeleted || !character.isDeleted())
            .sorted((left, right) -> left.getCreatedAt().compareTo(right.getCreatedAt()))
            .map(character -> playerCharacterMapper.toSummary(character, statuses.get(character.getId()), player))
            .collect(Collectors.toList());

        GetCharacters200Response response = new GetCharacters200Response();
        response.setCharacters(items);
        response.setTotalSlots(slots.size());
        response.setAvailableSlots(Math.toIntExact(characterSlotService.countAvailable(player)));
        return response;
    }

    @Override
    public PlayerCharacter createPlayerCharacter(CreatePlayerCharacterRequest request) {
        AccountEntity account = playerContextService.getCurrentAccount();
        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        List<CharacterSlotEntity> slots = characterSlotService.syncSlots(player);
        CharacterSlotEntity slot = characterSlotService.pickFreeSlot(slots);

        if (characterRepository.existsByNameAndAccountId(request.getName(), account.getId())) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Имя персонажа уже занято");
        }

        String classCode = normalizeClassCode(request.getClassId());
        characterClassRepository.findByClassCode(classCode)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Класс персонажа не найден"));

        CityEntity city = playerAppearanceService.resolveDefaultCity(player);
        CharacterAppearanceEntity appearance = playerAppearanceService.createAppearance(request.getAppearance(), player.getSettings());

        CharacterEntity character = new CharacterEntity();
        character.setAccount(account);
        character.setPlayer(player);
        character.setName(request.getName());
        character.setClassCode(classCode);
        character.setSubclassCode(null);
        character.setGender(playerAppearanceService.resolveGender(player.getSettings()));
        character.setOriginCode(playerAppearanceService.resolveOrigin(player.getSettings(), request.getOriginId()));
        character.setFaction(null);
        character.setCity(city);
        character.setAppearance(appearance);
        character = characterRepository.save(character);

        characterSlotService.assign(slot, character.getId());
        characterLifecycleService.ensureStats(character.getId());
        characterLifecycleService.ensureLocation(character, buildLocationTemplate(character, city));
        CharacterStatusEntity status = characterLifecycleService.ensureStatus(character.getId());

        return playerCharacterMapper.toSummary(character, status, player);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerCharacterDetails getCharacter(String characterId) {
        UUID id = parseUuid(characterId, "character_id");
        AccountEntity account = playerContextService.getCurrentAccount();
        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        CharacterEntity character = requireOwnedCharacter(id, account.getId());

        CharacterStatusEntity status = characterLifecycleService.ensureStatus(id);
        CharacterStatsEntity stats = characterLifecycleService.ensureStats(id);

        PlayerCharacter summary = playerCharacterMapper.toSummary(character, status, player);
        PlayerCharacterDetails details = new PlayerCharacterDetails();
        details.setCharacterId(summary.getCharacterId());
        details.setPlayerId(summary.getPlayerId());
        details.setName(summary.getName());
        details.setClassId(summary.getClassId());
        details.setLevel(summary.getLevel());
        details.setExperience(summary.getExperience());
        details.setCreatedAt(summary.getCreatedAt());
        details.setLastLogin(summary.getLastLogin());
        details.setIsDeleted(summary.getIsDeleted());
        details.setAttributes(characterLifecycleService.buildAttributes(stats));
        details.setSkills(characterLifecycleService.buildSkills(id));
        details.setReputation(Map.of());
        details.setPosition(buildPosition(character));
        details.setAppearance(characterLifecycleService.toAppearanceDto(character.getAppearance()));
        return details;
    }

    @Override
    public DeletePlayerCharacter200Response deletePlayerCharacter(String characterId) {
        UUID id = parseUuid(characterId, "character_id");
        AccountEntity account = playerContextService.getCurrentAccount();
        CharacterEntity character = requireOwnedCharacter(id, account.getId());
        if (character.isDeleted()) {
            return alreadyDeletedResponse(id, character);
        }

        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        CharacterStatusEntity status = characterLifecycleService.ensureStatus(id);
        CharacterStatsEntity stats = characterLifecycleService.ensureStats(id);

        characterLifecycleService.persistSnapshot(
            id,
            characterSlotService,
            character.getAppearance(),
            character,
            status,
            stats
        );

        CharacterSlotEntity slot = characterSlotService.findSlotByCharacterId(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Слот персонажа не найден"));
        OffsetDateTime deadline = nowUtc().plusDays(RESTORE_GRACE_DAYS);
        characterSlotService.release(slot, deadline);

        character.setDeleted(true);
        OffsetDateTime deletedAt = nowUtc();
        character.setDeletedAt(deletedAt);
        character.setRestoreDeadline(deadline);
        characterRepository.save(character);

        DeletePlayerCharacter200Response response = new DeletePlayerCharacter200Response();
        response.setCharacterId(character.getId().toString());
        response.setDeletedAt(deletedAt);
        response.setRestoreDeadline(deadline);
        response.setMessage("Персонаж отправлен в резерв на " + RESTORE_GRACE_DAYS + " дней");
        return response;
    }

    @Override
    public PlayerCharacter restoreCharacter(String characterId) {
        UUID id = parseUuid(characterId, "character_id");
        AccountEntity account = playerContextService.getCurrentAccount();
        CharacterEntity character = requireOwnedCharacter(id, account.getId());
        if (!character.isDeleted()) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "Персонаж не требует восстановления");
        }
        if (character.getRestoreDeadline() != null && character.getRestoreDeadline().isBefore(nowUtc())) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "Истёк срок восстановления персонажа");
        }

        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        List<CharacterSlotEntity> slots = characterSlotService.syncSlots(player);
        CharacterStatsSnapshotEntity snapshot = characterLifecycleService.getSnapshot(id);
        CharacterSlotEntity slot = characterSlotService.resolveRestoreSlot(player, slots, snapshot.getSlotNumber());
        characterSlotService.assign(slot, id);

        characterLifecycleService.applySnapshot(id, snapshot.getPayload());
        characterLifecycleService.deleteSnapshot(id);

        character.setDeleted(false);
        character.setDeletedAt(null);
        character.setRestoreDeadline(null);
        characterRepository.save(character);

        CharacterStatusEntity status = characterLifecycleService.ensureStatus(id);
        return playerCharacterMapper.toSummary(character, status, player);
    }

    @Override
    public SwitchCharacter200Response switchCharacter(SwitchCharacterRequest request) {
        UUID id = parseUuid(request.getCharacterId(), "character_id");
        AccountEntity account = playerContextService.getCurrentAccount();
        CharacterEntity character = requireOwnedCharacter(id, account.getId());
        if (character.isDeleted()) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "Персонаж находится в состоянии удаления");
        }

        PlayerEntity player = playerContextService.loadOrCreatePlayer(account);
        String locationId = character.getCity() != null ? character.getCity().getId().toString() : "unknown";
        GameSessionEntity session = gameSessionService.openSession(account.getId(), id, locationId);
        player.setActiveCharacterId(id);

        SwitchCharacter200Response response = new SwitchCharacter200Response();
        response.setCharacterId(id.toString());
        response.setSessionId(session.getId().toString());
        return response;
    }

    private CharacterEntity requireOwnedCharacter(UUID id, UUID accountId) {
        CharacterEntity character = characterRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Персонаж не найден"));
        if (!character.getAccount().getId().equals(accountId)) {
            throw new BusinessException(ErrorCode.OPERATION_NOT_ALLOWED, "Персонаж принадлежит другому аккаунту");
        }
        return character;
    }

    private CharacterLocationEntity buildLocationTemplate(CharacterEntity character, CityEntity city) {
        CharacterLocationEntity template = new CharacterLocationEntity();
        template.setCharacterId(character.getId());
        template.setCurrentLocationId(city.getId().toString());
        return template;
    }

    private Map<String, Object> buildPosition(CharacterEntity character) {
        CharacterLocationEntity location = characterLocationRepository.findByCharacterId(character.getId()).orElse(null);
        CityEntity city = character.getCity();
        var position = new java.util.HashMap<String, Object>();
        position.put("city_id", city != null ? city.getId().toString() : null);
        position.put("current_location_id", location != null ? location.getCurrentLocationId() : null);
        position.put("previous_location_id", location != null ? location.getPreviousLocationId() : null);
        return position;
    }

    private DeletePlayerCharacter200Response alreadyDeletedResponse(UUID id, CharacterEntity character) {
        DeletePlayerCharacter200Response response = new DeletePlayerCharacter200Response();
        response.setCharacterId(id.toString());
        response.setDeletedAt(character.getDeletedAt());
        response.setRestoreDeadline(character.getRestoreDeadline());
        response.setMessage("Персонаж уже находится в состоянии удаления");
        return response;
    }

    private OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    private UUID parseUuid(String value, String field) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Некорректный формат поля " + field);
        }
    }

    private String normalizeClassCode(String classId) {
        if (classId == null || classId.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "Класс персонажа не указан");
        }
        return classId.trim().toLowerCase();
    }
}

