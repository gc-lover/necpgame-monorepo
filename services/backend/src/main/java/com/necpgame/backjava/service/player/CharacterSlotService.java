package com.necpgame.backjava.service.player;

import com.necpgame.backjava.entity.CharacterSlotEntity;
import com.necpgame.backjava.entity.CharacterSlotEntity.SlotType;
import com.necpgame.backjava.entity.CharacterSlotId;
import com.necpgame.backjava.entity.PlayerEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.repository.CharacterSlotRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterSlotService {

    private static final int BASE_SLOT_COUNT = 3;
    private static final int TOTAL_SLOT_COUNT = 5;

    private final CharacterSlotRepository characterSlotRepository;

    public List<CharacterSlotEntity> syncSlots(PlayerEntity player) {
        List<CharacterSlotEntity> slots = characterSlotRepository.findByIdPlayerIdOrderByIdSlotNumber(player.getId());
        if (slots.size() == TOTAL_SLOT_COUNT) {
            return slots;
        }
        List<Integer> existing = slots.stream()
            .map(slot -> slot.getId().getSlotNumber())
            .collect(Collectors.toList());
        for (int number = 1; number <= TOTAL_SLOT_COUNT; number++) {
            if (!existing.contains(number)) {
                CharacterSlotEntity slot = new CharacterSlotEntity();
                slot.setId(new CharacterSlotId(player.getId(), number));
                slot.setPlayer(player);
                slot.setSlotType(number <= BASE_SLOT_COUNT ? SlotType.BASE : SlotType.PREMIUM);
                slot.setUnlocked(number <= BASE_SLOT_COUNT);
                characterSlotRepository.save(slot);
            }
        }
        return characterSlotRepository.findByIdPlayerIdOrderByIdSlotNumber(player.getId());
    }

    @Transactional(readOnly = true)
    public long countAvailable(PlayerEntity player) {
        return characterSlotRepository.countByIdPlayerIdAndUnlockedTrueAndCharacterIdIsNull(player.getId());
    }

    @Transactional(readOnly = true)
    public Optional<CharacterSlotEntity> findSlotByCharacterId(UUID characterId) {
        return characterSlotRepository.findByCharacterId(characterId);
    }

    public CharacterSlotEntity pickFreeSlot(List<CharacterSlotEntity> slots) {
        return slots.stream()
            .filter(CharacterSlotEntity::isFree)
            .min((left, right) -> Integer.compare(left.getId().getSlotNumber(), right.getId().getSlotNumber()))
            .orElseThrow(() -> new BusinessException(ErrorCode.LIMIT_EXCEEDED, "Нет свободных слотов персонажей"));
    }

    public void assign(CharacterSlotEntity slot, UUID characterId) {
        slot.assignCharacter(characterId);
        characterSlotRepository.save(slot);
    }

    public void release(CharacterSlotEntity slot, OffsetDateTime deadline) {
        slot.releaseSlot(deadline);
        characterSlotRepository.save(slot);
    }

    public CharacterSlotEntity resolveRestoreSlot(PlayerEntity player, List<CharacterSlotEntity> slots, Integer preferredSlot) {
        if (preferredSlot != null) {
            CharacterSlotId id = new CharacterSlotId(player.getId(), preferredSlot);
            CharacterSlotEntity preferred = characterSlotRepository.findById(id).orElse(null);
            if (preferred != null && preferred.isFree()) {
                return preferred;
            }
        }
        return slots.stream()
            .filter(CharacterSlotEntity::isFree)
            .min((left, right) -> Integer.compare(left.getId().getSlotNumber(), right.getId().getSlotNumber()))
            .orElseThrow(() -> new BusinessException(ErrorCode.LIMIT_EXCEEDED, "Нет свободных слотов для восстановления персонажа"));
    }
}
