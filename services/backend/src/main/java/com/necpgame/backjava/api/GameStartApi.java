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
 * Game Start API - РєРѕРЅС‚СЂР°РєС‚ РґР»СЏ Р·Р°РїСѓСЃРєР° РёРіСЂС‹.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/game/start.yaml
 * OpenAPI Generator version: 7.2.0
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
@Validated
@Tag(name = "Game Start", description = "Р—Р°РїСѓСЃРє РёРіСЂС‹ Рё РЅР°С‡Р°Р»СЊРЅС‹Р№ РєРѕРЅС‚РµРЅС‚")
public interface GameStartApi {

    /**
     * POST /v1/game/start : РќР°С‡Р°С‚СЊ РёРіСЂСѓ
     * 
     * @param body  (required)
     * @return РРіСЂР° РЅР°С‡Р°Р»Р°СЃСЊ СѓСЃРїРµС€РЅРѕ (status code 200)
     *         or Bad Request (status code 400)
     *         or Not Found (status code 404)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "startGame",
        summary = "РќР°С‡Р°С‚СЊ РёРіСЂСѓ",
        description = "РќР°С‡РёРЅР°РµС‚ РёРіСЂСѓ РґР»СЏ СЃРѕР·РґР°РЅРЅРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°.",
        tags = { "Game Start" },
        responses = {
            @ApiResponse(responseCode = "200", description = "РРіСЂР° РЅР°С‡Р°Р»Р°СЃСЊ СѓСЃРїРµС€РЅРѕ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GameStartResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = com.necpgame.backjava.model.Error.class))
            }),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
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
    @PostMapping(
        value = "/v1/game/start",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<GameStartResponse> startGame(
        @Parameter(name = "GameStartRequest", description = "", required = true)
        @Valid @RequestBody GameStartRequest body
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * GET /v1/game/welcome : РџРѕР»СѓС‡РёС‚СЊ РїСЂРёРІРµС‚СЃС‚РІРµРЅРЅС‹Р№ СЌРєСЂР°РЅ
     * 
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р° (required)
     * @return РџСЂРёРІРµС‚СЃС‚РІРµРЅРЅС‹Р№ СЌРєСЂР°РЅ (status code 200)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "getWelcomeScreen",
        summary = "РџРѕР»СѓС‡РёС‚СЊ РїСЂРёРІРµС‚СЃС‚РІРµРЅРЅС‹Р№ СЌРєСЂР°РЅ",
        description = "Р’РѕР·РІСЂР°С‰Р°РµС‚ РїСЂРёРІРµС‚СЃС‚РІРµРЅРЅС‹Р№ СЌРєСЂР°РЅ РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.",
        tags = { "Game Start" },
        responses = {
            @ApiResponse(responseCode = "200", description = "РџСЂРёРІРµС‚СЃС‚РІРµРЅРЅС‹Р№ СЌРєСЂР°РЅ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = WelcomeScreenResponse.class))
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
        value = "/v1/game/welcome",
        produces = { "application/json" }
    )
    default ResponseEntity<WelcomeScreenResponse> getWelcomeScreen(
        @Parameter(name = "characterId", description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", required = true)
        @Valid @RequestParam(value = "characterId", required = true) UUID characterId
    ) {
        return ResponseEntity.ok().build();
    }

    /**
     * POST /v1/game/return : Р’РµСЂРЅСѓС‚СЊСЃСЏ РІ РёРіСЂСѓ
     * 
     * @param body  (required)
     * @return Р’РѕР·РІСЂР°С‚ РІ РёРіСЂСѓ (status code 200)
     *         or Not Found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "returnToGame",
        summary = "Р’РµСЂРЅСѓС‚СЊСЃСЏ РІ РёРіСЂСѓ",
        description = "Р’РѕР·РІСЂР°С‰Р°РµС‚ РёРіСЂРѕРєР° РІ РёРіСЂСѓ РїСЂРё РїРѕРІС‚РѕСЂРЅРѕРј РІС…РѕРґРµ.",
        tags = { "Game Start" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Р’РѕР·РІСЂР°С‚ РІ РёРіСЂСѓ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GameReturnResponse.class))
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
    @PostMapping(
        value = "/v1/game/return",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<GameReturnResponse> returnToGame(
        @Parameter(name = "GameReturnRequest", description = "", required = true)
        @Valid @RequestBody GameReturnRequest body
    ) {
        return ResponseEntity.ok().build();
    }
}

