package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.ActionRequest;
import com.necpgame.backjava.model.ActionResult;
import com.necpgame.backjava.model.CombatLogResponse;
import com.necpgame.backjava.model.CombatMetricsResponse;
import com.necpgame.backjava.model.CombatSession;
import com.necpgame.backjava.model.CombatSessionCreateRequest;
import com.necpgame.backjava.model.CombatSessionStateResponse;
import com.necpgame.backjava.model.DamagePreviewRequest;
import com.necpgame.backjava.model.DamagePreviewResponse;
import com.necpgame.backjava.model.LagCompensationRequest;
import com.necpgame.backjava.model.LagCompensationResponse;
import com.necpgame.backjava.model.Participant;
import com.necpgame.backjava.model.ReviveRequest;
import com.necpgame.backjava.model.SessionAbortRequest;
import com.necpgame.backjava.model.SessionCompleteRequest;
import com.necpgame.backjava.model.SessionCompleteResponse;
import com.necpgame.backjava.model.SessionJoinRequest;
import com.necpgame.backjava.model.SimulationRequest;
import com.necpgame.backjava.model.SurrenderRequest;
import com.necpgame.backjava.service.CombatService;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
public class CombatServiceImpl implements CombatService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Combat service is not implemented yet");
    }

    @Override
    public CombatSession combatSessionsPost(CombatSessionCreateRequest combatSessionCreateRequest) {
        throw error();
    }

    @Override
    public Void combatSessionsSessionIdAbortPost(String sessionId, SessionAbortRequest sessionAbortRequest) {
        throw error();
    }

    @Override
    public ActionResult combatSessionsSessionIdActionsPost(String sessionId, ActionRequest actionRequest) {
        throw error();
    }

    @Override
    public SessionCompleteResponse combatSessionsSessionIdCompletePost(String sessionId, SessionCompleteRequest sessionCompleteRequest) {
        throw error();
    }

    @Override
    public DamagePreviewResponse combatSessionsSessionIdDamagePreviewPost(String sessionId, DamagePreviewRequest damagePreviewRequest) {
        throw error();
    }

    @Override
    public CombatSessionStateResponse combatSessionsSessionIdGet(String sessionId) {
        throw error();
    }

    @Override
    public Participant combatSessionsSessionIdJoinPost(String sessionId, SessionJoinRequest sessionJoinRequest) {
        throw error();
    }

    @Override
    public LagCompensationResponse combatSessionsSessionIdLagCompensationPost(String sessionId, LagCompensationRequest lagCompensationRequest) {
        throw error();
    }

    @Override
    public CombatLogResponse combatSessionsSessionIdLogGet(String sessionId, OffsetDateTime from, OffsetDateTime to) {
        throw error();
    }

    @Override
    public CombatMetricsResponse combatSessionsSessionIdMetricsGet(String sessionId) {
        throw error();
    }

    @Override
    public Void combatSessionsSessionIdRevivePost(String sessionId, ReviveRequest reviveRequest) {
        throw error();
    }

    @Override
    public Void combatSessionsSessionIdSimulatePost(String sessionId, SimulationRequest simulationRequest) {
        throw error();
    }

    @Override
    public Void combatSessionsSessionIdSurrenderPost(String sessionId, SurrenderRequest surrenderRequest) {
        throw error();
    }

    @Override
    public Void combatSessionsSessionIdTurnEndPost(String sessionId) {
        throw error();
    }
}


