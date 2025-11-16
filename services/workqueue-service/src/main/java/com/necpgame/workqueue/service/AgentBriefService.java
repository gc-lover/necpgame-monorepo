package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentBriefEntity;
import com.necpgame.workqueue.repository.AgentBriefRepository;
import com.necpgame.workqueue.web.dto.agent.AgentBriefDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgentBriefService {
    private final AgentBriefRepository agentBriefRepository;

    public Optional<AgentBriefDto> findBySegment(String segment) {
        if (segment == null || segment.isBlank()) {
            return Optional.empty();
        }
        String normalized = segment.trim().toLowerCase(Locale.ROOT);
        return agentBriefRepository.findById(normalized).map(this::toDto);
    }

    private AgentBriefDto toDto(AgentBriefEntity entity) {
        return new AgentBriefDto(
                entity.getSegment(),
                entity.getRoleKey(),
                entity.getTitle(),
                entity.getMission(),
                split(entity.getResponsibilities()),
                split(entity.getSubmissionChecklist()),
                split(entity.getHandoffNotes())
        );
    }

    private List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }
}


