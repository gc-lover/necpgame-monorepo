package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.qa.QaPlanEntity;
import com.necpgame.workqueue.repository.QaPlanRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QaDirectoryService {
    private final QaPlanRepository qaPlanRepository;

    public List<QaPlanEntity> listPlans() {
        return qaPlanRepository.findAllByOrderByPlanDateDesc();
    }

    public QaPlanEntity getPlan(UUID id) {
        return qaPlanRepository.findDetailedById(id)
                .orElseThrow(() -> new EntityNotFoundException("QA plan not found"));
    }

    public QaPlanEntity getPlanByCode(String code) {
        return qaPlanRepository.findDetailedByPlanCodeIgnoreCase(code)
                .orElseThrow(() -> new EntityNotFoundException("QA plan not found"));
    }
}


