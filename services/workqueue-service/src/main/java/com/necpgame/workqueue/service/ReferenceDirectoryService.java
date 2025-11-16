package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.EnumValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceDirectoryService {
    private final EnumValueRepository enumValueRepository;

    public List<EnumValueEntity> listTaskStatuses() {
        return enumValueRepository.findByGroupCodeOrderBySortOrder("task_status");
    }
}


