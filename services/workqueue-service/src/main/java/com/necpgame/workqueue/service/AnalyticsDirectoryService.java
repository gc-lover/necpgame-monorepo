package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.analytics.AnalyticsSchemaEntity;
import com.necpgame.workqueue.repository.AnalyticsSchemaRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsDirectoryService {
    private final AnalyticsSchemaRepository analyticsSchemaRepository;

    public List<AnalyticsSchemaEntity> listSchemas() {
        return analyticsSchemaRepository.findAllByOrderByFeatureNameAsc();
    }

    public AnalyticsSchemaEntity getSchema(UUID id) {
        return analyticsSchemaRepository.findDetailedById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analytics schema not found"));
    }

    public AnalyticsSchemaEntity getSchemaByContent(UUID contentId) {
        return analyticsSchemaRepository.findDetailedByContentEntity_Id(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Analytics schema not found"));
    }
}


