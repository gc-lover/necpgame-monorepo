package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.AgentPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgentTaskServiceTest {

    @Mock
    private AgentPreferenceService agentPreferenceService;
    @Mock
    private AgentDirectoryService agentDirectoryService;
    @Mock
    private QueueItemRepository queueItemRepository;
    @Mock
    private QueueCommandService queueCommandService;
    @Mock
    private QueueLockService queueLockService;
    @Mock
    private QueueLockRepository queueLockRepository;
    @Mock
    private EnumCatalogService enumCatalogService;

    private AgentTaskService service;
    private UUID agentId;
    private AgentPreference preference;

    @BeforeEach
    void setUp() {
        service = spy(new AgentTaskService(
                agentPreferenceService,
                agentDirectoryService,
                queueItemRepository,
                queueCommandService,
                queueLockService,
                queueLockRepository,
                enumCatalogService
        ));
        agentId = UUID.randomUUID();
        preference = new AgentPreference(
                "backend-implementer",
                List.of("backend"),
                List.of("qa"),
                List.of("queued"),
                List.of("in_progress"),
                "in_progress",
                "queued",
                30
        );
        when(agentPreferenceService.get(agentId)).thenReturn(preference);
    }

    @Test
    void claimTaskReturnsEmptyWhenNoCandidate() {
        when(queueItemRepository.findByAssignedToIdAndStatusValueIdIn(eq(agentId), anyList()))
                .thenReturn(List.of());
        when(enumCatalogService.requireTaskStatuses(preference.activeStatuses())).thenReturn(List.of(UUID.randomUUID()));
        when(enumCatalogService.requireTaskStatuses(preference.pickupStatuses())).thenReturn(List.of(UUID.randomUUID()));
        when(queueItemRepository.findNextTaskCandidate(anyList(), anyList(), any())).thenReturn(List.of());

        Optional<AgentTaskService.ClaimedTask> result = service.claimTask(agentId, List.of("backend"), 2);

        assertThat(result).isEmpty();
    }

    @Test
    void claimTaskThrowsConflictWhenActiveTaskExists() {
        QueueItemEntity active = queueItem();
        when(queueItemRepository.findByAssignedToIdAndStatusValueIdIn(eq(agentId), anyList()))
                .thenReturn(List.of(active));
        when(enumCatalogService.requireTaskStatuses(preference.activeStatuses())).thenReturn(List.of(UUID.randomUUID()));

        assertThatThrownBy(() -> service.claimTask(agentId, List.of("backend"), null))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("активная задача");
    }

    @Test
    void claimTaskAcceptsCandidate() {
        QueueItemEntity candidate = queueItem();
        when(queueItemRepository.findByAssignedToIdAndStatusValueIdIn(eq(agentId), anyList()))
                .thenReturn(List.of());
        when(enumCatalogService.requireTaskStatuses(preference.activeStatuses())).thenReturn(List.of(UUID.randomUUID()));
        when(enumCatalogService.requireTaskStatuses(preference.pickupStatuses())).thenReturn(List.of(UUID.randomUUID()));
        when(queueItemRepository.findNextTaskCandidate(anyList(), anyList(), any())).thenReturn(List.of(candidate));
        doReturn(new AgentTaskService.AcceptedTask(candidate, 30))
                .when(service)
                .acceptTask(eq(agentId), eq(candidate.getId()), eq(candidate.getVersion()), isNull(), anyString(), isNull(), isNull());

        Optional<AgentTaskService.ClaimedTask> result = service.claimTask(agentId, List.of("backend"), null);

        assertThat(result).isPresent();
        assertThat(result.get().item().getId()).isEqualTo(candidate.getId());
    }

    private QueueItemEntity queueItem() {
        OffsetDateTime now = OffsetDateTime.now();
        QueueEntity queue = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment("backend")
                .statusCode("queued")
                .title("Backend :: queued")
                .createdAt(now)
                .updatedAt(now)
                .build();
        return QueueItemEntity.builder()
                .id(UUID.randomUUID())
                .queue(queue)
                .externalRef("EXT-" + UUID.randomUUID())
                .title("Task")
                .priority(3)
                .createdAt(now)
                .updatedAt(now)
                .statusValueId(UUID.randomUUID())
                .version(1L)
                .build();
    }
}

