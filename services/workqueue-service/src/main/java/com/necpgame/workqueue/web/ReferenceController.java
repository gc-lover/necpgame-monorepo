package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.ReferenceDirectoryService;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.mapper.ContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceDirectoryService referenceDirectoryService;
    private final ContentMapper contentMapper;

    @GetMapping("/task-statuses")
    public ResponseEntity<List<EnumValueDto>> taskStatuses() {
        var values = referenceDirectoryService.listTaskStatuses();
        List<EnumValueDto> dto = values.stream().map(contentMapper::toEnum).toList();
        return ResponseEntity.ok(dto);
    }
}


