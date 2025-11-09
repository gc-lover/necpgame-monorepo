package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.CharacterActivityLogEntity;
import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterSlotPaymentEntity;
import com.necpgame.backjava.entity.CharacterSlotStateEntity;
import com.necpgame.backjava.entity.CharacterSlotPaymentEntity;
import com.necpgame.backjava.entity.PlayerProfileEntity;
import com.necpgame.backjava.model.CharacterAppearance;
import com.necpgame.backjava.model.CharacterCreateRequest;
import com.necpgame.backjava.model.CharacterCreateResponse;
import com.necpgame.backjava.model.CharacterSlotPurchaseRequest;
import com.necpgame.backjava.model.CharacterSlotPurchaseResponse;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.repository.AccountRepository;
import com.necpgame.backjava.repository.CharacterActivityLogRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterRestoreQueueRepository;
import com.necpgame.backjava.repository.CharacterSlotPaymentRepository;
import com.necpgame.backjava.repository.CharacterSlotStateRepository;
import com.necpgame.backjava.repository.CharacterSnapshotRepository;
import com.necpgame.backjava.repository.PlayerProfileRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharactersServiceImplTest {

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CharacterSlotStateRepository slotStateRepository;
    @Mock
    private CharacterSlotPaymentRepository slotPaymentRepository;
    @Mock
    private CharacterRestoreQueueRepository restoreQueueRepository;
    @Mock
    private CharacterActivityLogRepository activityLogRepository;
    @Mock
    private CharacterSnapshotRepository snapshotRepository;
    @Mock
    private PlayerProfileRepository playerProfileRepository;

    private CharactersServiceImpl service;
    private AccountEntity account;
    private CharacterSlotStateEntity slotState;
    private PlayerProfileEntity profile;

    @BeforeEach
    void setUp() {
        service = new CharactersServiceImpl(
            characterRepository,
            accountRepository,
            slotStateRepository,
            slotPaymentRepository,
            restoreQueueRepository,
            activityLogRepository,
            snapshotRepository,
            playerProfileRepository,
            new ObjectMapper().findAndRegisterModules()
        );
        account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setEmail("user@example.com");
        account.setUsername("user");
        slotState = new CharacterSlotStateEntity();
        slotState.setAccount(account);
        slotState.setAccountId(account.getId());
        slotState.setTotalSlots(3);
        slotState.setUsedSlots(0);
        slotState.setPremiumSlotsPurchased(0);
        slotState.setMaxSlots(5);
        profile = new PlayerProfileEntity();
        profile.setAccount(account);
        profile.setAccountId(account.getId());
        profile.setPremiumCurrency(0);
        profile.setTotalPlaytimeSeconds(0);
        profile.setLanguage("ru");
        profile.setTimezone("Europe/Moscow");
        profile.setFriendsJson("[]");
        profile.setBlockedJson("[]");

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(slotStateRepository.findByAccountId(account.getId())).thenReturn(Optional.of(slotState));
        lenient().when(playerProfileRepository.findByAccountId(account.getId())).thenReturn(Optional.of(profile));
        lenient().when(activityLogRepository.save(any(CharacterActivityLogEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createCharacter_shouldPersistEntityAndIncreaseUsedSlots() {
        CharacterCreateRequest request = new CharacterCreateRequest();
        request.setName("NeonGhost");
        request.setOrigin(CharacterCreateRequest.OriginEnum.STREETKID);
        request.setCharacterClass(CharacterCreateRequest.CharacterClassEnum.SOLO);
        request.setAppearance(buildAppearance());
        request.setSeed("seed-01");

        CharacterEntity saved = new CharacterEntity();
        saved.setId(UUID.randomUUID());
        saved.setAccount(account);
        saved.setName(request.getName());
        saved.setClassCode(request.getCharacterClass().getValue());
        saved.setOriginCode(request.getOrigin().getValue());
        saved.setAppearance(new CharacterAppearanceEntity());

        when(characterRepository.existsByNameAndAccountIdAndDeletedFalse(request.getName(), account.getId())).thenReturn(false);
        when(characterRepository.countByAccountIdAndDeletedFalse(account.getId())).thenReturn(0L);
        when(characterRepository.save(any(CharacterEntity.class))).thenReturn(saved);
        doAnswer(invocation -> invocation.getArgument(0)).when(slotStateRepository).save(any(CharacterSlotStateEntity.class));

        CharacterCreateResponse response = service.charactersPlayersAccountsAccountIdCharactersPost(account.getId(), request);

        assertThat(response).isNotNull();
        assertThat(response.getCharacter().getName()).isEqualTo(request.getName());
        assertThat(response.getSlots()).isNotNull();
        assertThat(response.getEvents()).hasSize(1);

        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        verify(characterRepository).save(captor.capture());
        CharacterEntity persisted = captor.getValue();
        assertThat(persisted.getAccount()).isEqualTo(account);
        assertThat(persisted.getAppearance()).isNotNull();
        assertThat(slotState.getUsedSlots()).isEqualTo(1);
    }

    @Test
    void slotsPurchase_walletShouldCompleteAndIncreaseTotalSlots() {
        when(characterRepository.countByAccountIdAndDeletedFalse(account.getId())).thenReturn(2L);
        doAnswer(invocation -> invocation.getArgument(0)).when(slotStateRepository).save(any(CharacterSlotStateEntity.class));
        when(slotPaymentRepository.save(any())).thenAnswer(invocation -> {
            CharacterSlotPaymentEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        CharacterSlotPurchaseRequest request = new CharacterSlotPurchaseRequest();
        request.setPaymentMethod(CharacterSlotPurchaseRequest.PaymentMethodEnum.WALLET);
        request.setCurrency("eddies");
        request.setAmount(500);

        CharacterSlotPurchaseResponse response = service
            .charactersPlayersAccountsAccountIdSlotsPurchasePost(account.getId(), request);

        assertThat(response.getStatus()).isEqualTo(CharacterSlotPurchaseResponse.StatusEnum.COMPLETED);
        CharacterSlotState state = response.getSlots();
        assertThat(state.getTotalSlots()).isEqualTo(slotState.getTotalSlots());
        verify(slotPaymentRepository).save(any());
    }

    private CharacterAppearance buildAppearance() {
        CharacterAppearance appearance = new CharacterAppearance();
        appearance.setBodyType(CharacterAppearance.BodyTypeEnum.SLIM);
        appearance.setSkinTone("pale");
        appearance.setHairStyle("short");
        appearance.setHairColor("#000000");
        appearance.setEyeColor("#112233");
        appearance.setTattoos(Collections.emptyList());
        appearance.setScars(Collections.emptyList());
        appearance.setImplantsVisible(Collections.emptyList());
        return appearance;
    }
}
