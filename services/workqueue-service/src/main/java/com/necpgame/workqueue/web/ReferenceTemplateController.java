package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.ReferenceTemplateService;
import com.necpgame.workqueue.web.dto.reference.ReferenceTemplateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reference/templates")
@RequiredArgsConstructor
public class ReferenceTemplateController {
    private final ReferenceTemplateService referenceTemplateService;

    @GetMapping("/{code}")
    public ResponseEntity<ReferenceTemplateDto> getTemplate(@PathVariable String code) {
        return ResponseEntity.ok(referenceTemplateService.get(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ReferenceTemplateDto> upsertTemplate(@PathVariable String code,
                                                               @RequestBody @Valid ReferenceTemplateDto dto) {
        return ResponseEntity.ok(referenceTemplateService.upsert(code, dto));
    }
}

