package com.necpgame.combatservice.service;

import com.necpgame.combatservice.model.ActionRequest;
import com.necpgame.combatservice.model.ActionResult;
import com.necpgame.combatservice.model.CombatError;
import com.necpgame.combatservice.model.CombatLogResponse;
import com.necpgame.combatservice.model.CombatMetricsResponse;
import com.necpgame.combatservice.model.CombatSession;
import com.necpgame.combatservice.model.CombatSessionCreateRequest;
import com.necpgame.combatservice.model.CombatSessionStateResponse;
import com.necpgame.combatservice.model.DamagePreviewRequest;
import com.necpgame.combatservice.model.DamagePreviewResponse;
import org.springframework.format.annotation.DateTimeFormat;
import com.necpgame.combatservice.model.Error;
import com.necpgame.combatservice.model.LagCompensationRequest;
import com.necpgame.combatservice.model.LagCompensationResponse;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import com.necpgame.combatservice.model.Participant;
import com.necpgame.combatservice.model.ReviveRequest;
import com.necpgame.combatservice.model.SessionAbortRequest;
import com.necpgame.combatservice.model.SessionCompleteRequest;
import com.necpgame.combatservice.model.SessionCompleteResponse;
import com.necpgame.combatservice.model.SessionJoinRequest;
import com.necpgame.combatservice.model.SimulationRequest;
import com.necpgame.combatservice.model.SurrenderRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CombatService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CombatService {

    /**
     * POST /combat/sessions : Создать боевую сессию
     *
     * @param combatSessionCreateRequest  (required)
     * @return CombatSession
     */
    CombatSession combatSessionsPost(CombatSessionCreateRequest combatSessionCreateRequest);

    /**
     * POST /combat/sessions/{sessionId}/abort : Аварийно завершить бой
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param sessionAbortRequest  (required)
     * @return Void
     */
    Void combatSessionsSessionIdAbortPost(String sessionId, SessionAbortRequest sessionAbortRequest);

    /**
     * POST /combat/sessions/{sessionId}/actions : Выполнить действие в боевой сессии
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param actionRequest  (required)
     * @return ActionResult
     */
    ActionResult combatSessionsSessionIdActionsPost(String sessionId, ActionRequest actionRequest);

    /**
     * POST /combat/sessions/{sessionId}/complete : Завершить бой и выдать награды
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param sessionCompleteRequest  (required)
     * @return SessionCompleteResponse
     */
    SessionCompleteResponse combatSessionsSessionIdCompletePost(String sessionId, SessionCompleteRequest sessionCompleteRequest);

    /**
     * POST /combat/sessions/{sessionId}/damage/preview : Получить предварительный расчёт урона
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param damagePreviewRequest  (required)
     * @return DamagePreviewResponse
     */
    DamagePreviewResponse combatSessionsSessionIdDamagePreviewPost(String sessionId, DamagePreviewRequest damagePreviewRequest);

    /**
     * GET /combat/sessions/{sessionId} : Получить состояние боевой сессии
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @return CombatSessionStateResponse
     */
    CombatSessionStateResponse combatSessionsSessionIdGet(String sessionId);

    /**
     * POST /combat/sessions/{sessionId}/join : Присоединиться к существующему бою
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param sessionJoinRequest  (required)
     * @return Participant
     */
    Participant combatSessionsSessionIdJoinPost(String sessionId, SessionJoinRequest sessionJoinRequest);

    /**
     * POST /combat/sessions/{sessionId}/lag-compensation : Запрос лаг-компенсации события
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param lagCompensationRequest  (required)
     * @return LagCompensationResponse
     */
    LagCompensationResponse combatSessionsSessionIdLagCompensationPost(String sessionId, LagCompensationRequest lagCompensationRequest);

    /**
     * GET /combat/sessions/{sessionId}/log : Получить журнал боя
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param from  (optional)
     * @param to  (optional)
     * @return CombatLogResponse
     */
    CombatLogResponse combatSessionsSessionIdLogGet(String sessionId, OffsetDateTime from, OffsetDateTime to);

    /**
     * GET /combat/sessions/{sessionId}/metrics : Аналитические метрики сессии
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @return CombatMetricsResponse
     */
    CombatMetricsResponse combatSessionsSessionIdMetricsGet(String sessionId);

    /**
     * POST /combat/sessions/{sessionId}/revive : Воскрешение участника
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param reviveRequest  (required)
     * @return Void
     */
    Void combatSessionsSessionIdRevivePost(String sessionId, ReviveRequest reviveRequest);

    /**
     * POST /combat/sessions/{sessionId}/simulate : Запуск симуляции боя (GM/Design)
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param simulationRequest  (required)
     * @return Void
     */
    Void combatSessionsSessionIdSimulatePost(String sessionId, SimulationRequest simulationRequest);

    /**
     * POST /combat/sessions/{sessionId}/surrender : Инициировать капитуляцию команды
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @param surrenderRequest  (required)
     * @return Void
     */
    Void combatSessionsSessionIdSurrenderPost(String sessionId, SurrenderRequest surrenderRequest);

    /**
     * POST /combat/sessions/{sessionId}/turn/end : Завершить текущий ход
     *
     * @param sessionId Идентификатор боевой сессии (required)
     * @return Void
     */
    Void combatSessionsSessionIdTurnEndPost(String sessionId);
}

