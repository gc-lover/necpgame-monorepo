package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentBriefEntity;
import com.necpgame.workqueue.repository.AgentBriefRepository;
import com.necpgame.workqueue.web.dto.agent.AgentBriefDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentBriefServiceTest {
    @Mock
    private AgentBriefRepository agentBriefRepository;

    private AgentBriefService agentBriefService;

    @BeforeEach
    void setUp() {
        agentBriefService = new AgentBriefService(agentBriefRepository);
    }

    @Test
    void findBySegmentReturnsNormalizedBrief() {
        AgentBriefEntity entity = new AgentBriefEntity();
        entity.setSegment("vision");
        entity.setRoleKey("vision-manager");
        entity.setTitle("Vision Manager");
        entity.setMission("Test mission");
        entity.setResponsibilities("one\ntwo");
        entity.setSubmissionChecklist("alpha\nbeta");
        entity.setHandoffNotes("gamma");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(agentBriefRepository.findById("vision")).thenReturn(Optional.of(entity));

        Optional<AgentBriefDto> result = agentBriefService.findBySegment("Vision");

        assertThat(result).isPresent();
        AgentBriefDto dto = result.orElseThrow();
        assertThat(dto.segment()).isEqualTo("vision");
        assertThat(dto.responsibilities()).containsExactly("one", "two");
        assertThat(dto.submissionChecklist()).containsExactly("alpha", "beta");
        assertThat(dto.handoffNotes()).containsExactly("gamma");
    }

    @Test
    void findBySegmentSkipsEmptyInput() {
        assertThat(agentBriefService.findBySegment(null)).isEmpty();
        verifyNoInteractions(agentBriefRepository);
    }
}


