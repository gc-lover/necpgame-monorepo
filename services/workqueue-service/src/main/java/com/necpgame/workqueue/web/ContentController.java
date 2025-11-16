package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.ContentQueryService;
import com.necpgame.workqueue.service.model.ContentSearchCriteria;
import com.necpgame.workqueue.web.dto.content.ContentDetailDto;
import com.necpgame.workqueue.web.dto.content.ContentListResponseDto;
import com.necpgame.workqueue.web.mapper.ContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {
    private static final int MAX_PAGE_SIZE = 100;
    private final ContentQueryService contentQueryService;
    private final ContentMapper contentMapper;

    @GetMapping("/entities")
    public ResponseEntity<ContentListResponseDto> listEntities(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "visibility", required = false) String visibility,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), MAX_PAGE_SIZE));
        ContentSearchCriteria criteria = new ContentSearchCriteria(type, status, category, visibility, search);
        var entities = contentQueryService.search(criteria, pageable);
        ContentListResponseDto response = contentMapper.toListResponse(entities);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entities/{id}")
    public ResponseEntity<ContentDetailDto> getEntity(@PathVariable UUID id) {
        var detail = contentQueryService.getDetail(id);
        return ResponseEntity.ok(detail);
    }
}


