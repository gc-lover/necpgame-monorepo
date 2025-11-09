package com.necpgame.backjava.api;

import com.necpgame.backjava.model.GenerateNPC200Response;
import com.necpgame.backjava.model.GenerateNPCRequest;
import com.necpgame.backjava.model.GenerateQuestRequest;
import com.necpgame.backjava.model.ValidateNarrative200Response;
import com.necpgame.backjava.model.ValidateNarrativeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Контракты Narrative Tools API.
 * <p>
 * Сгенерировано на основе OpenAPI спецификации.
 */
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    comments = "Generator version: 7.17.0"
)
@Validated
@Tag(name = "Narrative Tools", description = "Генераторы и валидация нарратива")
public interface NarrativeToolsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    String PATH_GENERATE_NPC = "/narrative/tools/npc/generate";

    @Operation(
        operationId = "generateNPC",
        summary = "Сгенерировать NPC",
        tags = { "Generators" },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "NPC сгенерирован",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GenerateNPC200Response.class)
                )
            )
        },
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_GENERATE_NPC,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    default ResponseEntity<GenerateNPC200Response> generateNPC(
        @Parameter(name = "GenerateNPCRequest", required = true)
        @Valid @RequestBody GenerateNPCRequest generateNPCRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GENERATE_QUEST = "/narrative/tools/quest/generate";

    @Operation(
        operationId = "generateQuest",
        summary = "Сгенерировать процедурный квест",
        tags = { "Generators" },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Квест сгенерирован",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Object.class)
                )
            )
        },
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_GENERATE_QUEST,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    default ResponseEntity<Object> generateQuest(
        @Parameter(name = "GenerateQuestRequest", required = true)
        @Valid @RequestBody GenerateQuestRequest generateQuestRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_VALIDATE_NARRATIVE = "/narrative/tools/validate";

    @Operation(
        operationId = "validateNarrative",
        summary = "Валидировать нарративную последовательность",
        tags = { "Validation" },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Результат валидации",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ValidateNarrative200Response.class)
                )
            )
        },
        security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = PATH_VALIDATE_NARRATIVE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    default ResponseEntity<ValidateNarrative200Response> validateNarrative(
        @Parameter(name = "ValidateNarrativeRequest", required = true)
        @Valid @RequestBody ValidateNarrativeRequest validateNarrativeRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}



