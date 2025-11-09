package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.LoreLocationEntity;
import com.necpgame.backjava.entity.enums.LoreLocationType;
import com.necpgame.backjava.model.GetConnectedLocations200Response;
import com.necpgame.backjava.model.GetLocationActions200Response;
import com.necpgame.backjava.model.GetLocations200Response;
import com.necpgame.backjava.model.ListLocations200Response;
import com.necpgame.backjava.model.Location;
import com.necpgame.backjava.model.LocationDetailed;
import com.necpgame.backjava.model.LocationDetails;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.TravelRequest;
import com.necpgame.backjava.model.TravelResponse;
import com.necpgame.backjava.repository.LoreLocationRepository;
import com.necpgame.backjava.repository.specification.LoreLocationSpecifications;
import com.necpgame.backjava.service.LocationsService;
import com.necpgame.backjava.service.mapper.LoreMapper;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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
public class LocationsServiceImpl implements LocationsService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final LoreLocationRepository loreLocationRepository;
    private final LoreMapper loreMapper;

    @Override
    @Transactional(readOnly = true)
    public GetLocations200Response getLocations(UUID characterId, String region, String dangerLevel, Integer minLevel) {
        log.info("getLocations characterId={} region={} dangerLevel={} minLevel={}", characterId, region, dangerLevel, minLevel);
        return new GetLocations200Response().locations(List.of()).total(0);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDetails getLocationDetails(String locationId, UUID characterId) {
        log.info("getLocationDetails locationId={} characterId={}", locationId, characterId);
        return new LocationDetails();
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDetails getCurrentLocation(UUID characterId) {
        log.info("getCurrentLocation characterId={}", characterId);
        return new LocationDetails();
    }

    @Override
    @Transactional
    public TravelResponse travelToLocation(TravelRequest request) {
        log.info("travelToLocation request={}", request);
        return new TravelResponse();
    }

    @Override
    @Transactional(readOnly = true)
    public GetLocationActions200Response getLocationActions(String locationId, UUID characterId) {
        log.info("getLocationActions locationId={} characterId={}", locationId, characterId);
        return new GetLocationActions200Response();
    }

    @Override
    @Transactional(readOnly = true)
    public GetConnectedLocations200Response getConnectedLocations(String locationId, UUID characterId) {
        log.info("getConnectedLocations locationId={} characterId={}", locationId, characterId);
        return new GetConnectedLocations200Response();
    }

    @Override
    @Transactional(readOnly = true)
    public ListLocations200Response listLocations(String region, String type, Integer page, Integer pageSize) {
        int resolvedPage = normalizePage(page);
        int resolvedSize = normalizePageSize(pageSize);

        Specification<LoreLocationEntity> specification = Specification.where(null);
        if (StringUtils.hasText(region)) {
            specification = specification == null
                    ? LoreLocationSpecifications.hasRegion(region)
                    : specification.and(LoreLocationSpecifications.hasRegion(region));
        }
        if (StringUtils.hasText(type)) {
            LoreLocationType locationType = parseLocationType(type);
            specification = specification == null
                    ? LoreLocationSpecifications.hasType(locationType)
                    : specification.and(LoreLocationSpecifications.hasType(locationType));
        }

        Pageable pageable = PageRequest.of(resolvedPage - 1, resolvedSize, Sort.by("name").ascending());
        Page<LoreLocationEntity> pageResult = loreLocationRepository.findAll(specification, pageable);

        List<Location> data = pageResult.getContent().stream()
                .map(loreMapper::toLocation)
                .collect(Collectors.toList());

        PaginationMeta meta = buildPaginationMeta(pageResult, resolvedPage, resolvedSize);
        return new ListLocations200Response()
                .data(data)
                .meta(meta);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDetailed getLocation(String locationId) {
        LoreLocationEntity entity = loreLocationRepository.findByExternalId(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found: " + locationId));
        return loreMapper.toLocationDetailed(entity);
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

    private PaginationMeta buildPaginationMeta(Page<?> page, int pageNumber, int pageSize) {
        return new PaginationMeta()
                .page(pageNumber)
                .pageSize(pageSize)
                .total(Math.toIntExact(page.getTotalElements()))
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrev(page.hasPrevious());
    }

    private LoreLocationType parseLocationType(String raw) {
        try {
            return LoreLocationType.valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported location type: " + raw);
        }
    }
}
