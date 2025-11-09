package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.MaintenanceWindowEntity;
import com.necpgame.backjava.model.MaintenanceStatus;
import com.necpgame.backjava.model.MaintenanceWindow;
import com.necpgame.backjava.model.MaintenanceWindowCreateRequest;
import com.necpgame.backjava.repository.MaintenanceAuditEntryRepository;
import com.necpgame.backjava.repository.MaintenanceStatusPayloadRepository;
import com.necpgame.backjava.repository.MaintenanceWindowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemServiceImplTest {

    @Mock
    private MaintenanceWindowRepository windowRepository;

    @Mock
    private MaintenanceAuditEntryRepository auditRepository;

    @Mock
    private MaintenanceStatusPayloadRepository statusPayloadRepository;

    private SystemServiceImpl systemService;

    @BeforeEach
    void setUp() {
        systemService = new SystemServiceImpl(windowRepository, auditRepository, statusPayloadRepository, new ObjectMapper());
    }

    @Test
    void systemMaintenanceWindowsPost_createsWindowAndPersistsAudit() {
        MaintenanceWindowCreateRequest request = new MaintenanceWindowCreateRequest();
        request.setTitle("Maintenance");
        request.setDescription("description");
        request.setType(MaintenanceWindow.TypeEnum.SCHEDULED);
        request.setEnvironment(MaintenanceWindow.EnvironmentEnum.PRODUCTION);
        request.setStartAt(OffsetDateTime.now().plusHours(2));
        request.setZones(List.of("eu"));
        request.setServices(List.of("gateway"));

        when(windowRepository.findConflictingWindows(any(), any(), any(), any())).thenReturn(List.of());
        when(windowRepository.save(any(MaintenanceWindowEntity.class))).thenAnswer(invocation -> {
            MaintenanceWindowEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });
        when(auditRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        MaintenanceWindow result = systemService.systemMaintenanceWindowsPost(request);

        assertNotNull(result.getWindowId());
        assertEquals(MaintenanceWindow.StatusEnum.PLANNED, result.getStatus());
        ArgumentCaptor<MaintenanceWindowEntity> entityCaptor = ArgumentCaptor.forClass(MaintenanceWindowEntity.class);
        verify(windowRepository).save(entityCaptor.capture());
        assertTrue(entityCaptor.getValue().getServicesJson().contains("gateway"));
        verify(auditRepository).save(any());
    }

    @Test
    void systemMaintenanceActiveGet_returnsActiveStatus() {
        MaintenanceWindowEntity entity = new MaintenanceWindowEntity();
        entity.setId(UUID.randomUUID());
        entity.setTitle("Window");
        entity.setType("SCHEDULED");
        entity.setEnvironment("PRODUCTION");
        entity.setStartAt(OffsetDateTime.now().minusMinutes(30));
        entity.setStatus("IN_PROGRESS");
        entity.setCreatedBy("system");
        entity.setAffectedServicesJson("[\"gateway\"]");
        entity.setStatusUpdatedAt(OffsetDateTime.now());

        when(windowRepository.findFirstByStatusInOrderByUpdatedAtDesc(any())).thenReturn(Optional.of(entity));

        MaintenanceStatus status = systemService.systemMaintenanceActiveGet();

        assertEquals(MaintenanceStatus.StatusEnum.IN_PROGRESS, status.getStatus());
        assertTrue(status.getAffectedServices().contains("gateway"));
    }
}


