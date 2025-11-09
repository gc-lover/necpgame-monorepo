package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LoreFactionEntity;
import com.necpgame.backjava.entity.enums.LoreFactionType;
import com.necpgame.backjava.model.Faction;
import com.necpgame.backjava.model.FactionDetailed;
import com.necpgame.backjava.model.GetFactions200Response;
import com.necpgame.backjava.model.ListFactions200Response;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.repository.FactionRepository;
import com.necpgame.backjava.repository.LoreFactionRepository;
import com.necpgame.backjava.repository.specification.LoreFactionSpecifications;
import com.necpgame.backjava.service.FactionsService;
import com.necpgame.backjava.service.mapper.LoreMapper;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FactionsServiceImpl implements FactionsService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final FactionRepository factionRepository;
    private final LoreFactionRepository loreFactionRepository;
    private final LoreMapper loreMapper;

    @Override
    @Transactional(readOnly = true)
    public GetFactions200Response getFactions(String origin) {
        log.info("Fetching legacy factions list, origin filter: {}", origin);

        List<Faction> factions = factionRepository.findAll().stream()
            .map(entity -> {
                Faction dto = new Faction()
                    .factionId(entity.getId() != null ? entity.getId().toString() : null)
                    .name(entity.getName())
                    .descriptionShort(entity.getDescription());
                if (entity.getType() != null) {
                    dto.type(Faction.TypeEnum.fromValue(entity.getType().name()));
                }
                return dto;
            })
            .collect(Collectors.toList());

        GetFactions200Response response = new GetFactions200Response();
        response.setFactions(factions);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ListFactions200Response listFactions(String type, String region, Integer page, Integer pageSize) {
        int resolvedPage = normalizePage(page);
        int resolvedSize = normalizePageSize(pageSize);
        Specification<LoreFactionEntity> specification = Specification.where(null);

        if (StringUtils.hasText(type)) {
            specification = specification == null
                    ? LoreFactionSpecifications.hasType(parseFactionType(type))
                    : specification.and(LoreFactionSpecifications.hasType(parseFactionType(type)));
        }
        if (StringUtils.hasText(region)) {
            specification = specification == null
                    ? LoreFactionSpecifications.hasRegion(region)
                    : specification.and(LoreFactionSpecifications.hasRegion(region));
        }

        Pageable pageable = PageRequest.of(resolvedPage - 1, resolvedSize, Sort.by("name").ascending());
        Page<LoreFactionEntity> pageResult = loreFactionRepository.findAll(specification, pageable);

        List<Faction> items = pageResult.getContent().stream()
                .map(loreMapper::toFaction)
                .collect(Collectors.toList());

        PaginationMeta meta = buildPaginationMeta(pageResult, resolvedPage, resolvedSize);
        return new ListFactions200Response()
                .data(items)
                .meta(meta);
    }

    @Override
    @Transactional(readOnly = true)
    public FactionDetailed getFaction(String factionId) {
        LoreFactionEntity entity = loreFactionRepository.findByExternalId(factionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faction not found: " + factionId));
        return loreMapper.toFactionDetailed(entity);
    }

    private int normalizePage(Integer page) {
        if (page == null || page < 1) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    private int normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    private LoreFactionType parseFactionType(String raw) {
        try {
            return LoreFactionType.valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported faction type: " + raw);
        }
    }

    private PaginationMeta buildPaginationMeta(Page<?> page, int pageNumber, int pageSize) {
        return new PaginationMeta()
                .page(pageNumber)
                .pageSize(pageSize)
                .total(Math.toIntExact(page.getTotalElements()))
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrev(page.hasPrevious());
    }
}

