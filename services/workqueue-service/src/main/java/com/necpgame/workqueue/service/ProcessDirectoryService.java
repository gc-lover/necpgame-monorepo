package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.process.ChecklistDefinitionEntity;
import com.necpgame.workqueue.domain.process.ProcessTemplateEntity;
import com.necpgame.workqueue.repository.ChecklistDefinitionRepository;
import com.necpgame.workqueue.repository.ProcessTemplateRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessDirectoryService {
    private final ProcessTemplateRepository processTemplateRepository;
    private final ChecklistDefinitionRepository checklistDefinitionRepository;

    public List<ProcessTemplateEntity> listTemplates() {
        return processTemplateRepository.findAllByOrderByNameAsc();
    }

    public ProcessTemplateEntity getTemplateByCode(String code) {
        return processTemplateRepository.findByCode_CodeIgnoreCase(code)
                .orElseThrow(() -> new EntityNotFoundException("Process template not found"));
    }

    public List<ChecklistDefinitionEntity> listChecklists() {
        return checklistDefinitionRepository.findAllByOrderByNameAsc();
    }

    public ChecklistDefinitionEntity getChecklistByCode(String code) {
        return checklistDefinitionRepository.findDetailedByCode_CodeIgnoreCase(code)
                .orElseThrow(() -> new EntityNotFoundException("Checklist definition not found"));
    }
}


