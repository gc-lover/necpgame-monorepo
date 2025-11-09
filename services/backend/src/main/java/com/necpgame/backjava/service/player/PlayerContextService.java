package com.necpgame.backjava.service.player;

import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.repository.AccountRepository;
import com.necpgame.backjava.repository.PlayerRepository;
import com.necpgame.backjava.util.SecurityUtil;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerContextService {

    private final AccountRepository accountRepository;
    private final PlayerRepository playerRepository;
    private final CharacterSlotService characterSlotService;

    @Transactional(readOnly = true)
    public AccountEntity getCurrentAccount() {
        return accountRepository.findById(SecurityUtil.getCurrentAccountId())
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Аккаунт не найден"));
    }

    public PlayerEntity loadOrCreatePlayer(AccountEntity account) {
        return playerRepository.findWithSlotsByAccountId(account.getId())
            .map(existing -> {
                characterSlotService.syncSlots(existing);
                return existing;
            })
            .orElseGet(() -> createPlayer(account));
    }

    private PlayerEntity createPlayer(AccountEntity account) {
        PlayerEntity player = new PlayerEntity();
        player.setAccount(account);
        player.setPremiumCurrency(0L);
        player.setSettings(new HashMap<>());
        player.setSlots(new ArrayList<>());
        PlayerEntity persisted = playerRepository.save(player);
        characterSlotService.syncSlots(persisted);
        return playerRepository.findById(persisted.getId()).orElse(persisted);
    }
}

