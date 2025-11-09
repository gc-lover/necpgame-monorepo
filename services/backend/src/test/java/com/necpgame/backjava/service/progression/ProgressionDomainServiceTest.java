package com.necpgame.backjava.service.progression;

import com.necpgame.backjava.entity.progression.CharacterProgressionEntity;
import com.necpgame.backjava.entity.progression.ProgressionHistoryType;
import com.necpgame.backjava.entity.progression.RespecType;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterSkillRepository;
import com.necpgame.backjava.repository.CharacterStatsRepository;
import com.necpgame.backjava.repository.SkillRepository;
import com.necpgame.backjava.repository.progression.CharacterProgressionHistoryRepository;
import com.necpgame.backjava.repository.progression.CharacterProgressionRepository;
import com.necpgame.backjava.repository.progression.CharacterSkillAllocationRepository;
import com.necpgame.backjava.service.progression.ProgressionHistoryRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressionDomainServiceTest {

    @Mock
    private CharacterProgressionRepository progressionRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterStatsRepository statsRepository;
    @Mock
    private CharacterSkillRepository skillRepository;
    @Mock
    private SkillRepository skillCatalogRepository;
    @Mock
    private CharacterSkillAllocationRepository allocationRepository;
    @Mock
    private CharacterProgressionHistoryRepository historyRepository;
    @Mock
    private ProgressionHistoryRecorder historyRecorder;

    private ProgressionDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new ProgressionDomainService(
                progressionRepository,
                characterRepository,
                statsRepository,
                skillRepository,
                skillCatalogRepository,
                allocationRepository,
                historyRepository,
                historyRecorder
        );
    }

    @Test
    void grantExperienceShouldLevelUpAndRecordHistory() {
        CharacterProgressionEntity progression = new CharacterProgressionEntity();
        progression.setCharacterId(UUID.randomUUID());
        progression.setLevel(1);
        progression.setExperienceToNextLevel(1_000L);

        domainService.grantExperience(progression, 1_500, "quests");

        assertThat(progression.getLevel()).isEqualTo(2);
        assertThat(progression.getExperience()).isEqualTo(500);
        assertThat(progression.getUnspentAttributePoints()).isEqualTo(1);
        assertThat(progression.getUnspentSkillPoints()).isEqualTo(2);

        ArgumentCaptor<Map<String, Object>> beforeCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> afterCaptor = ArgumentCaptor.forClass(Map.class);
        verify(historyRecorder, times(2)).record(
                org.mockito.Mockito.eq(progression.getCharacterId()),
                org.mockito.Mockito.any(ProgressionHistoryType.class),
                org.mockito.Mockito.anyString(),
                beforeCaptor.capture(),
                afterCaptor.capture()
        );
    }

    @Test
    void applyRespecShouldResetPointsAndDeleteAllocations() {
        CharacterProgressionEntity progression = new CharacterProgressionEntity();
        progression.setUnspentAttributePoints(5);
        progression.setTotalAttributePointsSpent(10);
        progression.setUnspentSkillPoints(3);
        progression.setTotalSkillPointsSpent(12);

        domainService.applyRespec(progression, UUID.randomUUID(), RespecType.FULL);

        assertThat(progression.getUnspentAttributePoints()).isEqualTo(15);
        assertThat(progression.getTotalAttributePointsSpent()).isZero();
        assertThat(progression.getUnspentSkillPoints()).isEqualTo(15);
        assertThat(progression.getTotalSkillPointsSpent()).isZero();
        verify(allocationRepository).deleteByCharacterId(org.mockito.Mockito.any(UUID.class));
    }
}

