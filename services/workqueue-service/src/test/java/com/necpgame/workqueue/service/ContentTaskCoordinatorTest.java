package com.necpgame.workqueue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.model.AgentPreference;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentTaskCoordinatorTest {

    @Mock
    private AgentPreferenceService agentPreferenceService;
    @Mock
    private HandoffRuleService handoffRuleService;
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private QueueItemRepository queueItemRepository;
    @Mock
    private QueueItemStateRepository queueItemStateRepository;
    @Mock
    private QueueItemTemplateRepository queueItemTemplateRepository;
    @Mock
    private EnumCatalogService enumCatalogService;
    @Mock
    private ActivityLogService activityLogService;

    private ContentTaskCoordinator coordinator;

    @BeforeEach
    void setup() {
        coordinator = new ContentTaskCoordinator(
                agentPreferenceService,
                handoffRuleService,
                queueRepository,
                queueItemRepository,
                queueItemStateRepository,
                queueItemTemplateRepository,
                enumCatalogService,
                activityLogService,
                new ObjectMapper()
        );
    }

    @Test
    void enqueueCreatesQueueItem() {
        AgentEntity actor = agent();
        ContentEntryEntity content = content("quest::alpha", "1.0", "Quest Alpha");
        when(agentPreferenceService.find(actor.getId()))
                .thenReturn(Optional.of(new AgentPreference("vision-manager", List.of("vision"), List.of(), List.of(), List.of(), "in_progress", "queued", 60)));
        HandoffRuleEntity rule = HandoffRuleEntity.builder()
                .id(UUID.randomUUID())
                .currentSegment("vision")
                .nextSegment("api")
                .templateCodes("concept-canon")
                .createdAt(OffsetDateTime.now())
                .build();
        when(handoffRuleService.findRules("vision", "completed")).thenReturn(List.of(rule));
        QueueEntity queue = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment("api")
                .statusCode("queued")
                .title("API :: queued")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        when(queueRepository.findBySegmentAndStatusCode("api", "queued")).thenReturn(Optional.of(queue));
        when(queueItemRepository.findByExternalRef(any())).thenReturn(Optional.empty());
        UUID statusId = UUID.randomUUID();
        when(enumCatalogService.requireTaskStatus("queued")).thenReturn(statusId);
        when(queueItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueItemStateRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueItemTemplateRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ContentTaskContext context = new ContentTaskContext(
                "quest.created",
                "Auto",
                List.of("/api/content/entities/" + content.getId()),
                Map.of("domain", "quests"),
                4
        );
        coordinator.enqueueContentChange(actor, content, context);

        verify(queueItemRepository, atLeast(1)).save(any());
        ArgumentCaptor<Map<String, Object>> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(activityLogService).recordContentEvent(eq(actor), eq(content), eq("content.queue_enqueued"), metadataCaptor.capture());
        assertThat(metadataCaptor.getValue().get("nextSegment")).isEqualTo("api");
        verify(queueItemTemplateRepository).saveAll(any());
    }

    @Test
    void enqueueSkipsWhenNoRules() {
        AgentEntity actor = agent();
        ContentEntryEntity content = content("quest::beta", "1.0", "Quest Beta");
        when(agentPreferenceService.find(actor.getId()))
                .thenReturn(Optional.of(new AgentPreference("vision-manager", List.of("vision"), List.of(), List.of(), List.of(), "in_progress", "queued", 60)));
        when(handoffRuleService.findRules("vision", "completed")).thenReturn(List.of());

        coordinator.enqueueContentChange(actor, content, new ContentTaskContext(
                "quest.created",
                null,
                List.of(),
                Map.of(),
                3
        ));

        verify(queueItemRepository, never()).save(any());
        verify(activityLogService, never()).recordContentEvent(eq(actor), eq(content), eq("content.queue_enqueued"), any());
    }

    @Test
    void enqueueSkipsWhenExternalRefExists() {
        AgentEntity actor = agent();
        ContentEntryEntity content = content("quest::gamma", "1.1", "Quest Gamma");
        when(agentPreferenceService.find(actor.getId()))
                .thenReturn(Optional.of(new AgentPreference("vision-manager", List.of("vision"), List.of(), List.of(), List.of(), "in_progress", "queued", 60)));
        HandoffRuleEntity rule = HandoffRuleEntity.builder()
                .id(UUID.randomUUID())
                .currentSegment("vision")
                .nextSegment("api")
                .createdAt(OffsetDateTime.now())
                .build();
        when(handoffRuleService.findRules("vision", "completed")).thenReturn(List.of(rule));
        when(queueItemRepository.findByExternalRef(any())).thenReturn(Optional.of(new QueueItemEntity()));

        coordinator.enqueueContentChange(actor, content, new ContentTaskContext(
                "quest.updated",
                null,
                List.of("/api/content/entities/" + content.getId()),
                Map.of(),
                3
        ));

        verify(queueItemRepository, never()).save(any());
        verify(activityLogService, never()).recordContentEvent(eq(actor), eq(content), eq("content.queue_enqueued"), any());
    }

    private AgentEntity agent() {
        AgentEntity actor = new AgentEntity();
        actor.setId(UUID.randomUUID());
        actor.setRoleKey("vision-manager");
        actor.setDisplayName("Vision");
        actor.setActive(true);
        actor.setCreatedAt(OffsetDateTime.now());
        actor.setUpdatedAt(OffsetDateTime.now());
        return actor;
    }

    private ContentEntryEntity content(String code, String version, String title) {
        ContentEntryEntity entry = new ContentEntryEntity();
        entry.setId(UUID.randomUUID());
        entry.setCode(code);
        entry.setVersion(version);
        entry.setTitle(title);
        EnumValueEntity type = new EnumValueEntity();
        type.setId(UUID.randomUUID());
        type.setCode("quest");
        entry.setEntityType(type);
        return entry;
    }
}

