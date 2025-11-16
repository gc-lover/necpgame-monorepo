package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.ReferenceTemplateEntity;
import com.necpgame.workqueue.repository.ReferenceTemplateRepository;
import com.necpgame.workqueue.web.dto.reference.ReferenceTemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReferenceTemplateService {
    private final ReferenceTemplateRepository repository;

    public ReferenceTemplateDto get(String code) {
        return find(code).orElseThrow(() -> new IllegalArgumentException("Template not found: " + code));
    }

    public Optional<ReferenceTemplateDto> find(String code) {
        return repository.findById(code).map(this::toDto);
    }

    public ReferenceTemplateDto upsert(String code, ReferenceTemplateDto dto) {
        ReferenceTemplateEntity entity = repository.findById(code).orElse(new ReferenceTemplateEntity());
        entity.setCode(code);
        entity.setTitle(dto.title());
        entity.setBody(dto.body());
        entity.setType(dto.type());
        entity.setSourcePath(dto.sourcePath());
        entity.setVersion(dto.version());
        entity.setContentHash(dto.contentHash());
        entity.setUpdatedAt(Optional.ofNullable(dto.updatedAt()).orElse(OffsetDateTime.now()));
        return toDto(repository.save(entity));
    }

    private ReferenceTemplateDto toDto(ReferenceTemplateEntity entity) {
        return new ReferenceTemplateDto(
                entity.getCode(),
                entity.getTitle(),
                entity.getBody(),
                entity.getType(),
                entity.getSourcePath(),
                entity.getVersion(),
                entity.getContentHash(),
                entity.getUpdatedAt()
        );
    }
}

