package com.necpgame.backjava.api;

import com.necpgame.backjava.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Game Initial State API - РєРѕРЅС‚СЂР°РєС‚ РґР»СЏ РїРѕР»СѓС‡РµРЅРёСЏ РЅР°С‡Р°Р»СЊРЅРѕРіРѕ СЃРѕСЃС‚РѕСЏРЅРёСЏ РёРіСЂС‹.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/game/initial-state.yaml
 * OpenAPI Generator version: 7.2.0
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
@Validated
@Tag(name = "Initial State", description = "РќР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹")
public interface GameInitialStateApi {

    /**
     * GET /v1/game/initial-state : РџРѕР»СѓС‡РёС‚СЊ РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹
     * 
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р° (required)
     * @return РќР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹ (status code 200)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "getInitialState",
        summary = "РџРѕР»СѓС‡РёС‚СЊ РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹",
        description = "Р’РѕР·РІСЂР°С‰Р°РµС‚ РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹ РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.",
        tags = { "Initial State" },
        responses = {
            @ApiResponse(responseCode = "200", description = "РќР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = InitialStateResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @GetMapping(
        value = "/v1/game/initial-state",
        produces = { "application/json" }
    )
    default ResponseEntity<InitialStateResponse> getInitialState(
        @Parameter(name = "characterId", description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", required = true)
        @Valid @RequestParam(value = "characterId", required = true) UUID characterId
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * GET /v1/game/tutorial-steps : РџРѕР»СѓС‡РёС‚СЊ С€Р°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°
     * 
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р° (required)
     * @return РЁР°РіРё С‚СѓС‚РѕСЂРёР°Р»Р° (status code 200)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "getTutorialSteps",
        summary = "РџРѕР»СѓС‡РёС‚СЊ С€Р°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°",
        description = "Р’РѕР·РІСЂР°С‰Р°РµС‚ СЃРїРёСЃРѕРє С€Р°РіРѕРІ С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РЅРѕРІРѕРіРѕ РёРіСЂРѕРєР°.",
        tags = { "Initial State" },
        responses = {
            @ApiResponse(responseCode = "200", description = "РЁР°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = TutorialStepsResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @GetMapping(
        value = "/v1/game/tutorial-steps",
        produces = { "application/json" }
    )
    default ResponseEntity<TutorialStepsResponse> getTutorialSteps(
        @Parameter(name = "characterId", description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", required = true)
        @Valid @RequestParam(value = "characterId", required = true) UUID characterId
    ) {
        return ResponseEntity.ok().build();
    }
}

