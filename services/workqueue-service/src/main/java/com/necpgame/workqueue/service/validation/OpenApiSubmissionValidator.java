package com.necpgame.workqueue.service.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.oas.models.media.Schema;

@Component
@RequiredArgsConstructor
public class OpenApiSubmissionValidator implements SubmissionValidator {
    private static final List<String> REQUIREMENTS = List.of("validator:openapi");
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String segment) {
        return segment != null && segment.equalsIgnoreCase("api");
    }

    @Override
    public void validate(TaskSubmissionContext context) {
        TaskSubmissionRequestDto request = context.request();
        if (request == null) {
            return;
        }
        JsonNode metadataNode = parseMetadata(context, request.metadata());
        requireText(context, metadataNode, "openapiVersion", "validation.api.version_missing", "Укажите поле openapiVersion в metadata");
        String specPath = requireText(context, metadataNode, "specPath", "validation.api.spec_path_missing", "Укажите specPath (путь к спецификации)");
        if (!specPath.endsWith(".yaml") && !specPath.endsWith(".yml") && !specPath.endsWith(".json")) {
            throw error(context, "validation.api.spec_path_invalid", "specPath должен оканчиваться на .yaml, .yml или .json",
                    new ApiErrorDetail("metadata.specPath", specPath));
        }
        boolean hasSpecAttachment = context.linkArtifacts().stream()
                .map(TaskSubmissionRequestDto.SubmissionArtifactDto::url)
                .filter(StringUtils::hasText)
                .anyMatch(this::isSpecReference)
                || context.files().stream()
                .map(MultipartFile::getOriginalFilename)
                .filter(StringUtils::hasText)
                .anyMatch(this::isSpecReference);
        if (!hasSpecAttachment) {
            throw error(context, "validation.api.spec_artifact_required",
                    "Добавьте артефакт со спецификацией (.yaml/.yml/.json) или ссылку на неё",
                    new ApiErrorDetail("artifacts", "spec file not found"));
        }

        String inlineSpec = readInlineSpecIfAny(context);
        if (StringUtils.hasText(inlineSpec)) {
            SwaggerParseResult result = new OpenAPIV3Parser().readContents(inlineSpec, null, null);
            if (result == null || result.getOpenAPI() == null || (result.getMessages() != null && !result.getMessages().isEmpty())) {
                String messages = result != null && result.getMessages() != null
                        ? String.join("; ", result.getMessages())
                        : "parse failed";
                throw error(context, "validation.api.spec_invalid",
                        "Файл спецификации OpenAPI невалиден",
                        new ApiErrorDetail("artifacts.spec", messages));
            }
            validateConventions(context, result);
        }
    }

    private JsonNode parseMetadata(TaskSubmissionContext context, String metadata) {
        if (!StringUtils.hasText(metadata)) {
            throw error(context, "validation.api.metadata_required", "Для OpenAPI submit заполните metadata (JSON)",
                    new ApiErrorDetail("metadata", "required"));
        }
        try {
            return objectMapper.readTree(metadata);
        } catch (JsonProcessingException ex) {
            throw error(context, "validation.api.metadata_invalid", "metadata должен быть корректным JSON",
                    new ApiErrorDetail("metadata", ex.getOriginalMessage()));
        }
    }

    private String requireText(TaskSubmissionContext context, JsonNode metadata, String field, String code, String message) {
        JsonNode node = metadata.path(field);
        if (node.isMissingNode() || !StringUtils.hasText(node.asText())) {
            throw error(context, code, message, new ApiErrorDetail("metadata." + field, "missing"));
        }
        return node.asText();
    }

    private boolean isSpecReference(String value) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        String lower = value.toLowerCase();
        return lower.endsWith(".yaml") || lower.endsWith(".yml") || lower.endsWith(".json");
    }

    private void validateConventions(TaskSubmissionContext context, SwaggerParseResult result) {
        var spec = result.getOpenAPI();
        // operationId must be unique
        java.util.Set<String> opIds = new java.util.HashSet<>();
        boolean hasSecuritySchemes = spec.getComponents() != null && spec.getComponents().getSecuritySchemes() != null && !spec.getComponents().getSecuritySchemes().isEmpty();
        if (spec.getPaths() != null) {
            spec.getPaths().forEach((path, item) -> {
                item.readOperations().forEach(op -> {
                    if (!StringUtils.hasText(op.getOperationId())) {
                        throw error(context, "validation.api.convention.operationId_missing",
                                "operationId обязателен для каждой операции",
                                new ApiErrorDetail("operation", path));
                    }
                    if (!opIds.add(op.getOperationId())) {
                        throw error(context, "validation.api.convention.operationId_unique",
                                "operationId должен быть уникальным в пределах спецификации",
                                new ApiErrorDetail("operationId", op.getOperationId()));
                    }
                    if (op.getTags() == null || op.getTags().isEmpty()) {
                        throw error(context, "validation.api.convention.tags_missing",
                                "Каждая операция должна иметь хотя бы один tag",
                                new ApiErrorDetail("operation", op.getOperationId()));
                    }
                    if (op.getResponses() == null || op.getResponses().isEmpty()) {
                        throw error(context, "validation.api.convention.responses_missing",
                                "Определите ответы для операции",
                                new ApiErrorDetail("operation", op.getOperationId()));
                    }
                    if (!op.getResponses().containsKey("default")) {
                        throw error(context, "validation.api.convention.default_response_missing",
                                "Определите default ответ (ApiError) для операции",
                                new ApiErrorDetail("operation", op.getOperationId()));
                    }
                    if (hasSecuritySchemes && (op.getSecurity() == null || op.getSecurity().isEmpty())) {
                        boolean isPublic = op.getTags().stream().anyMatch(t -> "public".equalsIgnoreCase(t));
                        if (!isPublic) {
                            throw error(context, "validation.api.convention.security_missing",
                                    "Операция должна декларировать security (или быть помечена тегом public)",
                                    new ApiErrorDetail("operation", op.getOperationId()));
                        }
                    }
                });
            });
        }
        if (spec.getComponents() != null && spec.getComponents().getSchemas() != null) {
            spec.getComponents().getSchemas().forEach((schemaName, schema) -> {
                if (schema.getProperties() != null) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Schema<?>> props = (java.util.Map<String, Schema<?>>) (java.util.Map<?, ?>) schema.getProperties();
                    for (java.util.Map.Entry<String, Schema<?>> entry : props.entrySet()) {
                        String propName = entry.getKey();
                        Schema<?> propSchema = entry.getValue();
                        if (!isCamelCase(propName)) {
                            throw error(context, "validation.api.convention.camel_case",
                                    "Имена полей должны быть в camelCase",
                                    new ApiErrorDetail(schemaName + "." + propName, "use camelCase"));
                        }
                        if (propName.endsWith("Id")) {
                            String fmt = propSchema.getFormat();
                            if (fmt == null || !"uuid".equals(fmt)) {
                                throw error(context, "validation.api.convention.uuid_format",
                                        "Поля, оканчивающиеся на 'Id', должны иметь format: uuid",
                                        new ApiErrorDetail(schemaName + "." + propName, String.valueOf(fmt)));
                            }
                        }
                        if (propName.endsWith("At")) {
                            String fmt = propSchema.getFormat();
                            if (fmt == null || !"date-time".equals(fmt)) {
                                throw error(context, "validation.api.convention.datetime_format",
                                        "Поля, оканчивающиеся на 'At', должны иметь format: date-time",
                                        new ApiErrorDetail(schemaName + "." + propName, String.valueOf(fmt)));
                            }
                        }
                    }
                }
                // Простейшая эвристика для enum-кодов: если схема оканчивается на Code, значения snake_case
                if (schemaName.endsWith("Code") && schema.getEnum() != null) {
                    for (Object v : schema.getEnum()) {
                        if (v instanceof String s) {
                            if (!isSnakeCase(s)) {
                                throw error(context, "validation.api.convention.enum_snake_case",
                                        "Enum значения для *Code должны быть в snake_case",
                                        new ApiErrorDetail(schemaName, s));
                            }
                        }
                    }
                }
            });
        }
    }

    private boolean isCamelCase(String s) {
        return s != null && s.matches("^[a-z]+[A-Za-z0-9]*$");
    }

    private boolean isSnakeCase(String s) {
        return s != null && s.matches("^[a-z0-9]+(?:_[a-z0-9]+)*$");
    }

    private String readInlineSpecIfAny(TaskSubmissionContext context) {
        if (context.files() == null || context.files().isEmpty()) {
            return null;
        }
        List<MultipartFile> specFiles = context.files().stream()
                .filter(f -> {
                    String name = f.getOriginalFilename();
                    return StringUtils.hasText(name) && isSpecReference(name);
                })
                .collect(Collectors.toList());
        if (specFiles.isEmpty()) {
            return null;
        }
        MultipartFile first = specFiles.get(0);
        try {
            byte[] bytes = first.getBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return null;
        }
    }

    private ApiErrorException error(TaskSubmissionContext context, String code, String message, ApiErrorDetail detail) {
        List<String> requirements = new ArrayList<>(context.requirements());
        requirements.addAll(REQUIREMENTS);
        return new ApiErrorException(
                HttpStatus.BAD_REQUEST,
                code,
                message,
                List.copyOf(requirements),
                List.of(detail)
        );
    }
}

