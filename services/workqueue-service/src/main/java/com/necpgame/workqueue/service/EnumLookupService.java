package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.EnumValueRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnumLookupService {
    private final EnumValueRepository enumValueRepository;

    public EnumValueEntity require(String groupCode, String valueCode) {
        if (groupCode == null || groupCode.isBlank()) {
            throw new IllegalArgumentException("groupCode required");
        }
        if (valueCode == null || valueCode.isBlank()) {
            throw new IllegalArgumentException("valueCode required");
        }
        return enumValueRepository.findByGroupCodeAndCode(groupCode, valueCode)
                .orElseThrow(() -> new EntityNotFoundException("Enum value not found for %s:%s".formatted(groupCode, valueCode)));
    }
}


