package com.necpgame.backjava.service.mvp;

import com.necpgame.backjava.entity.mvp.MvpContentStatusEntity;
import com.necpgame.backjava.entity.mvp.MvpEndpointEntity;
import com.necpgame.backjava.mapper.MvpContentMapper;
import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.GetMVPHealth200Response;
import com.necpgame.backjava.model.GetMVPHealth200Response.StatusEnum;
import com.necpgame.backjava.model.GetMVPHealth200ResponseSystems;
import com.necpgame.backjava.repository.mvp.MvpContentStatusRepository;
import com.necpgame.backjava.repository.mvp.MvpEndpointRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MvpProgressFacade {

    private final MvpReferenceFacade referenceFacade;
    private final MvpContentStatusRepository contentStatusRepository;
    private final MvpEndpointRepository endpointRepository;
    private final MvpContentMapper mapper;

    public ContentOverview getContentOverview(String period) {
        return mapper.toContentOverview(referenceFacade.requireOverview(period));
    }

    public ContentStatus getContentStatus() {
        MvpContentStatusEntity statusEntity = contentStatusRepository.findTopByOrderByIdAsc()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус MVP контента не инициализирован"));
        return mapper.toContentStatus(statusEntity);
    }

    public GetMVPHealth200Response getHealthStatus() {
        MvpContentStatusEntity statusEntity = contentStatusRepository.findTopByOrderByIdAsc()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус MVP контента не инициализирован"));

        List<MvpEndpointEntity> endpoints = endpointRepository.findAll();

        GetMVPHealth200ResponseSystems systems = new GetMVPHealth200ResponseSystems()
            .auth(resolveCategoryStatus(endpoints, "Authentication"))
            .playerManagement(resolveCategoryStatus(endpoints, "Player"))
            .questEngine(resolveBooleanStatus(statusEntity.isQuestEngineReady()))
            .combatSession(resolveBooleanStatus(statusEntity.isCombatReady()))
            .progression(resolveBooleanStatus(statusEntity.isProgressionReady()));

        StatusEnum aggregate = aggregateStatus(
            systems.getAuth(),
            systems.getPlayerManagement(),
            systems.getQuestEngine(),
            systems.getCombatSession(),
            systems.getProgression(),
            statusEntity.isMvpContentReady()
        );

        return new GetMVPHealth200Response()
            .status(aggregate)
            .systems(systems);
    }

    private String resolveCategoryStatus(List<MvpEndpointEntity> endpoints, String categoryPrefix) {
        List<MvpEndpointEntity> categoryEndpoints = endpoints.stream()
            .filter(endpoint -> endpoint.getCategory() != null)
            .filter(endpoint -> endpoint.getCategory().toLowerCase(Locale.ROOT).startsWith(categoryPrefix.toLowerCase(Locale.ROOT)))
            .toList();

        if (categoryEndpoints.isEmpty()) {
            return "UNKNOWN";
        }

        boolean allImplemented = categoryEndpoints.stream().allMatch(MvpEndpointEntity::isImplemented);
        if (allImplemented) {
            return "UP";
        }

        boolean anyImplemented = categoryEndpoints.stream().anyMatch(MvpEndpointEntity::isImplemented);
        return anyImplemented ? "DEGRADED" : "DOWN";
    }

    private String resolveBooleanStatus(boolean ready) {
        return ready ? "UP" : "DOWN";
    }

    private StatusEnum aggregateStatus(
        String auth,
        String playerManagement,
        String questEngine,
        String combatSession,
        String progression,
        boolean overallReady
    ) {
        List<String> statuses = List.of(auth, playerManagement, questEngine, combatSession, progression);
        if (statuses.stream().anyMatch("DOWN"::equalsIgnoreCase)) {
            return StatusEnum.DOWN;
        }
        if (overallReady && statuses.stream().allMatch("UP"::equalsIgnoreCase)) {
            return StatusEnum.HEALTHY;
        }
        return StatusEnum.DEGRADED;
    }
}

