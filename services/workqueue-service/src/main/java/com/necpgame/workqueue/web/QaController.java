package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.QaDirectoryService;
import com.necpgame.workqueue.web.dto.qa.QaPlanDto;
import com.necpgame.workqueue.web.dto.qa.QaPlanListResponseDto;
import com.necpgame.workqueue.web.mapper.QaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/qa/plans")
@RequiredArgsConstructor
public class QaController {
    private final QaDirectoryService qaDirectoryService;
    private final QaMapper qaMapper;

    @GetMapping
    public ResponseEntity<QaPlanListResponseDto> listPlans() {
        var plans = qaDirectoryService.listPlans();
        return ResponseEntity.ok(qaMapper.toListResponse(plans));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QaPlanDto> getPlan(@PathVariable UUID id) {
        var plan = qaDirectoryService.getPlan(id);
        return ResponseEntity.ok(qaMapper.toDto(plan));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<QaPlanDto> getPlanByCode(@PathVariable String code) {
        var plan = qaDirectoryService.getPlanByCode(code);
        return ResponseEntity.ok(qaMapper.toDto(plan));
    }
}


