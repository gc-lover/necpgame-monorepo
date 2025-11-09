package com.necpgame.backjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterClassEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterSlotEntity;
import com.necpgame.backjava.entity.CharacterSlotEntity.SlotType;
import com.necpgame.backjava.entity.CharacterSlotId;
import com.necpgame.backjava.entity.CharacterStatsEntity;
import com.necpgame.backjava.entity.CharacterStatusEntity;
import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.mapper.CharacterAppearanceMapperMS;
import com.necpgame.backjava.mapper.PlayerCharacterMapper;
import com.necpgame.backjava.mapper.PlayerProfileMapper;
import com.necpgame.backjava.model.CreatePlayerCharacterRequest;
import com.necpgame.backjava.model.CreatePlayerCharacterRequestAppearance;
import com.necpgame.backjava.model.GetCharacters200Response;
import com.necpgame.backjava.model.PlayerCharacter;
import com.necpgame.backjava.model.PlayerProfile;
import com.necpgame.backjava.repository.AccountRepository;
import com.necpgame.backjava.repository.CharacterClassRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterSkillRepository;
import com.necpgame.backjava.repository.CharacterSlotRepository;
import com.necpgame.backjava.repository.CharacterStatsRepository;
import com.necpgame.backjava.repository.CharacterStatusRepository;
import com.necpgame.backjava.repository.CityRepository;
import com.necpgame.backjava.repository.PlayerRepository;
import com.necpgame.backjava.repository.CharacterStatsSnapshotRepository;
import com.necpgame.backjava.repository.CharacterLocationRepository;
import com.necpgame.backjava.repository.GameSessionRepository;
import com.necpgame.backjava.entity.CharacterLocationEntity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayersServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterSlotRepository characterSlotRepository;
    @Mock
    private CharacterStatusRepository characterStatusRepository;
    @Mock
    private CharacterStatsRepository characterStatsRepository;
    @Mock
    private CharacterSkillRepository characterSkillRepository;
    @Mock
    private CharacterStatsSnapshotRepository characterStatsSnapshotRepository;
    @Mock
    private CharacterLocationRepository characterLocationRepository;
    @Mock
    private CharacterClassRepository characterClassRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private GameSessionRepository gameSessionRepository;
    @Mock
    private CharacterAppearanceMapperMS characterAppearanceMapper;
    @Mock
    private PlayerProfileMapper playerProfileMapper;
    @Mock
    private PlayerCharacterMapper playerCharacterMapper;

    private PlayersServiceImpl playersService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountEntity account;
    private PlayerEntity player;

    @BeforeEach
    void setUp() {
        playersService = new PlayersServiceImpl(
            playerRepository,
            accountRepository,
            characterRepository,
            characterSlotRepository,
            characterStatusRepository,
            characterStatsRepository,
            characterSkillRepository,
            characterStatsSnapshotRepository,
            characterLocationRepository,
            characterClassRepository,
            cityRepository,
            gameSessionRepository,
            characterAppearanceMapper,
            playerProfileMapper,
            playerCharacterMapper,
            objectMapper
        );
        account = new AccountEntity();
        account.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        account.setEmail("test@example.com");
        player = new PlayerEntity();
        player.setId(UUID.randomUUID());
        player.setAccount(account);
        player.setPremiumCurrency(10L);
        player.setSettings(new HashMap<>());
    }

    @Test
    void getPlayerProfile_shouldReturnProfileFromMapper() {
        PlayerProfile profile = new PlayerProfile();
        List<CharacterSlotEntity> slots = defaultSlots();

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(playerRepository.findWithSlotsByAccountId(account.getId())).thenReturn(Optional.of(player));
        when(characterSlotRepository.findByIdPlayerIdOrderByIdSlotNumber(player.getId())).thenReturn(slots);
        when(playerProfileMapper.toProfile(player)).thenReturn(profile);

        PlayerProfile result = playersService.getPlayerProfile();

        assertSame(profile, result);
        verify(characterSlotRepository, times(1)).findByIdPlayerIdOrderByIdSlotNumber(player.getId());
    }

    @Test
    void createCharacter_shouldPersistCharacterAndAssignSlot() {
        List<CharacterSlotEntity> slots = defaultSlots();
        CharacterSlotEntity freeSlot = slots.get(0);

        CreatePlayerCharacterRequest request = new CreatePlayerCharacterRequest();
        request.setName("V");
        request.setClassId("class_solo");
        request.setOriginId("origin_nomad");
        CreatePlayerCharacterRequestAppearance appearance = new CreatePlayerCharacterRequestAppearance();
        appearance.setBodyType("normal");
        appearance.setHairColor("black");
        appearance.setSkinTone("light");
        request.setAppearance(appearance);
        player.getSettings().put("default_gender", "male");
        player.getSettings().put("default_eye_color", "brown");

        CharacterAppearanceEntity appearanceEntity = new CharacterAppearanceEntity();
        CityEntity city = new CityEntity();
        city.setId(UUID.randomUUID());
        CharacterEntity persisted = new CharacterEntity();
        persisted.setId(UUID.randomUUID());
        persisted.setAccount(account);
        persisted.setPlayer(player);
        CharacterStatusEntity status = new CharacterStatusEntity();
        status.setCharacterId(persisted.getId());
        PlayerCharacter summary = new PlayerCharacter().characterId(persisted.getId().toString());

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(playerRepository.findWithSlotsByAccountId(account.getId())).thenReturn(Optional.of(player));
        when(characterSlotRepository.findByIdPlayerIdOrderByIdSlotNumber(player.getId())).thenReturn(slots);
        when(characterRepository.existsByNameAndAccountId(request.getName(), account.getId())).thenReturn(false);
        when(characterClassRepository.findByClassCode(anyString())).thenReturn(Optional.of(new CharacterClassEntity()));
        when(cityRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(city)));
        when(characterAppearanceMapper.toEntity(any(com.necpgame.backjava.model.GameCharacterAppearance.class))).thenReturn(appearanceEntity);
        when(characterRepository.save(any(CharacterEntity.class))).thenReturn(persisted);
        when(characterStatusRepository.findByCharacterId(persisted.getId())).thenReturn(Optional.of(status));
        when(characterStatsRepository.findByCharacterId(persisted.getId())).thenReturn(Optional.empty());
        when(characterStatsRepository.save(any(CharacterStatsEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(characterLocationRepository.findByCharacterId(persisted.getId())).thenReturn(Optional.empty());
        when(characterLocationRepository.save(any(CharacterLocationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(playerCharacterMapper.toSummary(persisted, status, player)).thenReturn(summary);

        PlayerCharacter result = playersService.createPlayerCharacter(request);

        assertSame(summary, result);
        verify(characterRepository, times(1)).save(any(CharacterEntity.class));
        verify(characterSlotRepository, times(1)).save(freeSlot);
    }

    @Test
    void getCharacters_shouldFilterDeletedWhenFlagFalse() {
        List<CharacterSlotEntity> slots = defaultSlots();
        CharacterEntity active = new CharacterEntity();
        active.setId(UUID.randomUUID());
        active.setAccount(account);
        active.setPlayer(player);
        active.setDeleted(false);
        active.setCreatedAt(OffsetDateTime.now());
        CharacterEntity deleted = new CharacterEntity();
        deleted.setId(UUID.randomUUID());
        deleted.setAccount(account);
        deleted.setPlayer(player);
        deleted.setDeleted(true);
        deleted.setCreatedAt(OffsetDateTime.now().minusDays(1));
        CharacterStatusEntity status = new CharacterStatusEntity();
        status.setCharacterId(active.getId());
        PlayerCharacter summary = new PlayerCharacter().characterId(active.getId().toString());

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(playerRepository.findWithSlotsByAccountId(account.getId())).thenReturn(Optional.of(player));
        when(characterSlotRepository.findByIdPlayerIdOrderByIdSlotNumber(player.getId())).thenReturn(slots);
        when(characterRepository.findAllByAccountId(account.getId())).thenReturn(List.of(active, deleted));
        when(characterStatusRepository.findByCharacterIdIn(any())).thenReturn(List.of(status));
        when(playerCharacterMapper.toSummary(active, status, player)).thenReturn(summary);

        GetCharacters200Response response = playersService.getCharacters(false);

        assertEquals(1, response.getCharacters().size());
        assertEquals(summary.getCharacterId(), response.getCharacters().get(0).getCharacterId());
    }

    private List<CharacterSlotEntity> defaultSlots() {
        List<CharacterSlotEntity> slots = new ArrayList<>();
        for (int number = 1; number <= 5; number++) {
            CharacterSlotEntity slot = new CharacterSlotEntity();
            slot.setId(new CharacterSlotId(player.getId(), number));
            slot.setPlayer(player);
            slot.setSlotType(number <= 3 ? SlotType.BASE : SlotType.PREMIUM);
            slot.setUnlocked(true);
            slot.setCharacterId(null);
            slots.add(slot);
        }
        return slots;
    }
}

