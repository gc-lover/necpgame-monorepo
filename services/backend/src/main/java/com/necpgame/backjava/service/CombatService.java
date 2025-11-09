package com.necpgame.backjava.service;

import com.necpgame.backjava.model.ActionRequest;
import com.necpgame.backjava.model.ActionResult;
import com.necpgame.backjava.model.CombatError;
import com.necpgame.backjava.model.CombatLogResponse;
import com.necpgame.backjava.model.CombatMetricsResponse;
import com.necpgame.backjava.model.CombatSession;
import com.necpgame.backjava.model.CombatSessionCreateRequest;
import com.necpgame.backjava.model.CombatSessionStateResponse;
import com.necpgame.backjava.model.DamagePreviewRequest;
import com.necpgame.backjava.model.DamagePreviewResponse;
import org.springframework.format.annotation.DateTimeFormat;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.LagCompensationRequest;
import com.necpgame.backjava.model.LagCompensationResponse;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import com.necpgame.backjava.model.Participant;
import com.necpgame.backjava.model.ReviveRequest;
import com.necpgame.backjava.model.SessionAbortRequest;
import com.necpgame.backjava.model.SessionCompleteRequest;
import com.necpgame.backjava.model.SessionCompleteResponse;
import com.necpgame.backjava.model.SessionJoinRequest;
import com.necpgame.backjava.model.SimulationRequest;
import com.necpgame.backjava.model.SurrenderRequest;
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

