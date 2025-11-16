package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.ReleaseDirectoryService;
import com.necpgame.workqueue.web.dto.release.ReleaseRunDto;
import com.necpgame.workqueue.web.dto.release.ReleaseRunListResponseDto;
import com.necpgame.workqueue.web.mapper.ReleaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/release/runs")
@RequiredArgsConstructor
public class ReleaseController {
    private final ReleaseDirectoryService releaseDirectoryService;
    private final ReleaseMapper releaseMapper;

    @GetMapping
    public ResponseEntity<ReleaseRunListResponseDto> listRuns() {
        var runs = releaseDirectoryService.listRuns();
        return ResponseEntity.ok(releaseMapper.toListResponse(runs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReleaseRunDto> getRun(@PathVariable UUID id) {
        var run = releaseDirectoryService.getRun(id);
        return ResponseEntity.ok(releaseMapper.toDto(run));
    }

    @GetMapping("/change/{changeId}")
    public ResponseEntity<ReleaseRunDto> getRunByChange(@PathVariable String changeId) {
        var run = releaseDirectoryService.getRunByChangeId(changeId);
        return ResponseEntity.ok(releaseMapper.toDto(run));
    }
}


