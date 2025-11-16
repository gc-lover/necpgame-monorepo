package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.model.ContentSearchCriteria;
import com.necpgame.workqueue.web.dto.content.ContentDetailDto;
import com.necpgame.workqueue.web.mapper.ContentMapper;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentQueryService {
    private static final String ENUM_CODE_FIELD = "code";
    private final ContentEntryRepository contentEntryRepository;
    private final ContentMapper contentMapper;

    @Transactional(readOnly = true)
    public Page<ContentEntryEntity> search(ContentSearchCriteria criteria, Pageable pageable) {
        Specification<ContentEntryEntity> specification = Specification.where(null);
        if (criteria.hasType()) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                var join = root.join("entityType", JoinType.INNER);
                return cb.equal(cb.lower(join.get(ENUM_CODE_FIELD)), criteria.typeCode().toLowerCase(Locale.ROOT));
            });
        }
        if (criteria.hasStatus()) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                var join = root.join("status", JoinType.INNER);
                return cb.equal(cb.lower(join.get(ENUM_CODE_FIELD)), criteria.statusCode().toLowerCase(Locale.ROOT));
            });
        }
        if (criteria.hasCategory()) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                var join = root.join("category", JoinType.LEFT);
                return cb.equal(cb.lower(join.get(ENUM_CODE_FIELD)), criteria.categoryCode().toLowerCase(Locale.ROOT));
            });
        }
        if (criteria.hasVisibility()) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                var join = root.join("visibility", JoinType.INNER);
                return cb.equal(cb.lower(join.get(ENUM_CODE_FIELD)), criteria.visibilityCode().toLowerCase(Locale.ROOT));
            });
        }
        if (criteria.hasSearch()) {
            specification = specification.and((root, query, cb) -> {
                String pattern = "%" + criteria.search().toLowerCase(Locale.ROOT) + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(root.get("summary")), pattern)
                );
            });
        }
        Objects.requireNonNull(pageable, "pageable required");
        return contentEntryRepository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public ContentDetailDto getDetail(UUID id) {
        ContentEntryEntity entity = contentEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content entity not found"));
        return contentMapper.toDetail(entity);
    }
}


