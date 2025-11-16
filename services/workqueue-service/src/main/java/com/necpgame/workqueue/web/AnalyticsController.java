package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.AnalyticsDirectoryService;
import com.necpgame.workqueue.web.dto.analytics.AnalyticsSchemaDto;
import com.necpgame.workqueue.web.dto.analytics.AnalyticsSchemaListResponseDto;
import com.necpgame.workqueue.web.mapper.AnalyticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/analytics/schemas")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsDirectoryService analyticsDirectoryService;
    private final AnalyticsMapper analyticsMapper;

    @GetMapping
    public ResponseEntity<AnalyticsSchemaListResponseDto> listSchemas() {
        var schemas = analyticsDirectoryService.listSchemas();
        return ResponseEntity.ok(analyticsMapper.toListResponse(schemas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsSchemaDto> getSchema(@PathVariable UUID id) {
        var schema = analyticsDirectoryService.getSchema(id);
        return ResponseEntity.ok(analyticsMapper.toDto(schema));
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<AnalyticsSchemaDto> getSchemaByContent(@PathVariable UUID contentId) {
        var schema = analyticsDirectoryService.getSchemaByContent(contentId);
        return ResponseEntity.ok(analyticsMapper.toDto(schema));
    }
}


