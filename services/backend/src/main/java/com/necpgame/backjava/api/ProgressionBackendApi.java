package com.necpgame.backjava.api;

import com.necpgame.backjava.model.AddSkillExperienceRequest;
import com.necpgame.backjava.model.AwardExperienceRequest;
import com.necpgame.backjava.model.CharacterAttributes;
import com.necpgame.backjava.model.CharacterExperience;
import com.necpgame.backjava.model.CharacterSkills;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.ExperienceAwardResult;
import com.necpgame.backjava.model.GetProgressionMilestones200Response;
import com.necpgame.backjava.model.LevelUpResult;
import com.necpgame.backjava.model.SkillExperienceResult;
import com.necpgame.backjava.model.SpendAttributePointsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.util.Optional;
import java.util.UUID;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
@Validated
@Tag(name = "Experience", description = "Управление опытом")
@Tag(name = "Level Up", description = "Повышение уровня")
@Tag(name = "Attributes", description = "Атрибуты персонажа")
@Tag(name = "Skills", description = "Навыки персонажа")
public interface ProgressionBackendApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    String PATH_ADD_SKILL_EXPERIENCE = "/progression/characters/{character_id}/skills/{skill_id}/experience";

    @Operation(
        operationId = "addSkillExperience",
        summary = "Добавить опыт навыку",
        description = "Навык прокачивается использованием",
        tags = { "Skills" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Опыт добавлен", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SkillExperienceResult.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = ProgressionBackendApi.PATH_ADD_SKILL_EXPERIENCE,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<SkillExperienceResult> addSkillExperience(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId,
        @NotNull @Parameter(name = "skill_id", required = true, in = ParameterIn.PATH) @PathVariable("skill_id") String skillId,
        @Parameter(name = "AddSkillExperienceRequest", required = true) @Valid @RequestBody AddSkillExperienceRequest addSkillExperienceRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_AWARD_EXPERIENCE = "/progression/characters/{character_id}/experience/award";

    @Operation(
        operationId = "awardExperience",
        summary = "Выдать опыт персонажу",
        description = "Используется backend системами (quest, combat) для выдачи опыта",
        tags = { "Experience" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Опыт выдан", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceAwardResult.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос. Параметры запроса некорректны или отсутствуют обязательные поля. ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class), examples = {
                    @ExampleObject(
                        value = "{\"error\":{\"code\":\"VALIDATION_ERROR\",\"message\":\"Неверные параметры запроса\",\"details\":[{\"field\":\"name\",\"message\":\"Имя должно быть не пустым\",\"code\":\"REQUIRED\"}]}}"
                    )
                })
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = ProgressionBackendApi.PATH_AWARD_EXPERIENCE,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<ExperienceAwardResult> awardExperience(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId,
        @Parameter(name = "AwardExperienceRequest", required = true) @Valid @RequestBody AwardExperienceRequest awardExperienceRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_CHARACTER_ATTRIBUTES = "/progression/characters/{character_id}/attributes";

    @Operation(
        operationId = "getCharacterAttributes",
        summary = "Получить атрибуты персонажа",
        tags = { "Attributes" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Атрибуты персонажа", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterAttributes.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = ProgressionBackendApi.PATH_GET_CHARACTER_ATTRIBUTES,
        produces = { "application/json" }
    )
    default ResponseEntity<CharacterAttributes> getCharacterAttributes(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_CHARACTER_EXPERIENCE = "/progression/characters/{character_id}/experience";

    @Operation(
        operationId = "getCharacterExperience",
        summary = "Получить информацию об опыте персонажа",
        tags = { "Experience" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Информация об опыте", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterExperience.class))
            }),
            @ApiResponse(responseCode = "404", description = "Запрошенный ресурс не найден. ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = ProgressionBackendApi.PATH_GET_CHARACTER_EXPERIENCE,
        produces = { "application/json" }
    )
    default ResponseEntity<CharacterExperience> getCharacterExperience(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_CHARACTER_SKILLS = "/progression/characters/{character_id}/skills";

    @Operation(
        operationId = "getCharacterSkills",
        summary = "Получить навыки персонажа",
        tags = { "Skills" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Навыки персонажа", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterSkills.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = ProgressionBackendApi.PATH_GET_CHARACTER_SKILLS,
        produces = { "application/json" }
    )
    default ResponseEntity<CharacterSkills> getCharacterSkills(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_GET_PROGRESS_MILESTONES = "/progression/characters/{character_id}/milestones";

    @Operation(
        operationId = "getProgressionMilestones",
        summary = "Получить прогрессионные вехи персонажа",
        tags = { "Level Up" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Прогрессионные вехи", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = GetProgressionMilestones200Response.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = ProgressionBackendApi.PATH_GET_PROGRESS_MILESTONES,
        produces = { "application/json" }
    )
    default ResponseEntity<GetProgressionMilestones200Response> getProgressionMilestones(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_LEVEL_UP = "/progression/characters/{character_id}/level-up";

    @Operation(
        operationId = "levelUp",
        summary = "Повысить уровень персонажа",
        description = "Автоматически вызывается когда достаточно опыта",
        tags = { "Level Up" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Уровень повышен", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LevelUpResult.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос. Параметры запроса некорректны или отсутствуют обязательные поля. ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = ProgressionBackendApi.PATH_LEVEL_UP,
        produces = { "application/json" }
    )
    default ResponseEntity<LevelUpResult> levelUp(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    String PATH_SPEND_ATTRIBUTE_POINTS = "/progression/characters/{character_id}/attributes/spend";

    @Operation(
        operationId = "spendAttributePoints",
        summary = "Потратить очки атрибутов",
        tags = { "Attributes" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Очки потрачены", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = CharacterAttributes.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос. Параметры запроса некорректны или отсутствуют обязательные поля. ", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
            })
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = ProgressionBackendApi.PATH_SPEND_ATTRIBUTE_POINTS,
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<CharacterAttributes> spendAttributePoints(
        @NotNull @Parameter(name = "character_id", required = true, in = ParameterIn.PATH) @PathVariable("character_id") UUID characterId,
        @Parameter(name = "SpendAttributePointsRequest", required = true) @Valid @RequestBody SpendAttributePointsRequest spendAttributePointsRequest
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}


