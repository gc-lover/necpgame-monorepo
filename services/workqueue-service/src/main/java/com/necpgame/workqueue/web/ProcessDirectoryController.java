package com.necpgame.workqueue.web;

import com.necpgame.workqueue.service.ProcessDirectoryService;
import com.necpgame.workqueue.web.dto.process.ChecklistDefinitionDto;
import com.necpgame.workqueue.web.dto.process.ChecklistListResponseDto;
import com.necpgame.workqueue.web.dto.process.ProcessTemplateDto;
import com.necpgame.workqueue.web.dto.process.ProcessTemplateListResponseDto;
import com.necpgame.workqueue.web.mapper.ProcessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessDirectoryController {
    private final ProcessDirectoryService processDirectoryService;
    private final ProcessMapper processMapper;

    @GetMapping("/templates")
    public ResponseEntity<ProcessTemplateListResponseDto> listTemplates() {
        var templates = processDirectoryService.listTemplates();
        return ResponseEntity.ok(processMapper.toTemplateList(templates));
    }

    @GetMapping("/templates/{code}")
    public ResponseEntity<ProcessTemplateDto> getTemplate(@PathVariable String code) {
        var template = processDirectoryService.getTemplateByCode(code);
        return ResponseEntity.ok(processMapper.toTemplate(template));
    }

    @GetMapping("/checklists")
    public ResponseEntity<ChecklistListResponseDto> listChecklists() {
        var checklists = processDirectoryService.listChecklists();
        return ResponseEntity.ok(processMapper.toChecklistList(checklists));
    }

    @GetMapping("/checklists/{code}")
    public ResponseEntity<ChecklistDefinitionDto> getChecklist(@PathVariable String code) {
        var checklist = processDirectoryService.getChecklistByCode(code);
        return ResponseEntity.ok(processMapper.toChecklist(checklist));
    }
}


