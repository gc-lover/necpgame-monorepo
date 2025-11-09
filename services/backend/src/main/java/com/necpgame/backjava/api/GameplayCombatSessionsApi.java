package com.necpgame.backjava.api;

import com.necpgame.backjava.model.CombatEndResult;
import com.necpgame.backjava.model.CombatSession;
import com.necpgame.backjava.model.CreateCombatSessionRequest;
import com.necpgame.backjava.model.DamageRequest;
import com.necpgame.backjava.model.DamageResult;
import com.necpgame.backjava.model.EndCombatSessionRequest;
import com.necpgame.backjava.model.GetCombatEvents200Response;
import com.necpgame.backjava.model.NextTurn200Response;
import com.necpgame.backjava.model.UpdateParticipantStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
@Validated
@Tag(name = "Combat Sessions", description = "Gameplay combat session management")
public interface GameplayCombatSessionsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    String PATH_CREATE_SESSION = "/gameplay/combat/sessions";

    @Operation(
        operationId = "createCombatSession",
        summary = "Создать боевую сессию",
        tags = { "Combat Sessions" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Боевая сессия создана", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CombatSession.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_CREATE_SESSION,
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    default ResponseEntity<CombatSession> createCombatSession(@Parameter(name = "CreateCombatSessionRequest", required = true) @Valid @RequestBody CreateCombatSessionRequest createCombatSessionRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_SESSION = "/gameplay/combat/sessions/{session_id}";

    @Operation(
        operationId = "getCombatSession",
        summary = "Получить состояние боевой сессии",
        tags = { "Combat Sessions" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Состояние боя", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CombatSession.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = PATH_GET_SESSION,
        produces = {"application/json"}
    )
    default ResponseEntity<CombatSession> getCombatSession(@Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_APPLY_DAMAGE = "/gameplay/combat/sessions/{session_id}/damage";

    @Operation(
        operationId = "applyDamage",
        summary = "Нанести урон",
        tags = { "Combat Actions" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Урон применен", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = DamageResult.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_APPLY_DAMAGE,
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    default ResponseEntity<DamageResult> applyDamage(
        @Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId,
        @Parameter(name = "DamageRequest", required = true) @Valid @RequestBody DamageRequest damageRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_NEXT_TURN = "/gameplay/combat/sessions/{session_id}/turn/next";

    @Operation(
        operationId = "nextTurn",
        summary = "Следующий ход",
        description = "Для turn-based combat",
        tags = { "Combat Actions" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Новый ход начат", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = NextTurn200Response.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_NEXT_TURN,
        produces = {"application/json"}
    )
    default ResponseEntity<NextTurn200Response> nextTurn(@Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_END_SESSION = "/gameplay/combat/sessions/{session_id}/end";

    @Operation(
        operationId = "endCombatSession",
        summary = "Завершить бой",
        tags = { "Combat Results" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Бой завершен", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CombatEndResult.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_END_SESSION,
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    default ResponseEntity<CombatEndResult> endCombatSession(
        @Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId,
        @Parameter(name = "EndCombatSessionRequest", required = true) @Valid @RequestBody EndCombatSessionRequest endCombatSessionRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_EVENTS = "/gameplay/combat/sessions/{session_id}/events";

    @Operation(
        operationId = "getCombatEvents",
        summary = "Получить лог событий боя",
        tags = { "Combat Sessions" },
        responses = {
            @ApiResponse(responseCode = "200", description = "События боя", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GetCombatEvents200Response.class))
            })
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = PATH_GET_EVENTS,
        produces = {"application/json"}
    )
    default ResponseEntity<GetCombatEvents200Response> getCombatEvents(
        @Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId,
        @Parameter(name = "since_event_id") @RequestParam(name = "since_event_id", required = false) Integer sinceEventId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_UPDATE_PARTICIPANT = "/gameplay/combat/sessions/{session_id}/participants/{participant_id}/status";

    @Operation(
        operationId = "updateParticipantStatus",
        summary = "Обновить статус участника",
        tags = { "Combat Actions" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Статус обновлен")
        },
        security = {@SecurityRequirement(name = "BearerAuth")}
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = PATH_UPDATE_PARTICIPANT,
        consumes = {"application/json"}
    )
    default ResponseEntity<Void> updateParticipantStatus(
        @Parameter(name = "session_id", required = true) @PathVariable("session_id") UUID sessionId,
        @Parameter(name = "participant_id", required = true) @PathVariable("participant_id") String participantId,
        @Parameter(name = "UpdateParticipantStatusRequest", required = true) @Valid @RequestBody UpdateParticipantStatusRequest updateParticipantStatusRequest) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}


