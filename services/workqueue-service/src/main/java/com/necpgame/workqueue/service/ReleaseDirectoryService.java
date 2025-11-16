package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.release.ReleaseRunEntity;
import com.necpgame.workqueue.repository.ReleaseRunRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReleaseDirectoryService {
    private final ReleaseRunRepository releaseRunRepository;

    public List<ReleaseRunEntity> listRuns() {
        return releaseRunRepository.findAllByOrderByReleaseDateDesc();
    }

    public ReleaseRunEntity getRun(UUID id) {
        return releaseRunRepository.findDetailedById(id)
                .orElseThrow(() -> new EntityNotFoundException("Release run not found"));
    }

    public ReleaseRunEntity getRunByChangeId(String changeId) {
        return releaseRunRepository.findDetailedByChangeIdIgnoreCase(changeId)
                .orElseThrow(() -> new EntityNotFoundException("Release run not found"));
    }
}


