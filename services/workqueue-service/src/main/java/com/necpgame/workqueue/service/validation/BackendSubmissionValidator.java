package com.necpgame.workqueue.service.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackendSubmissionValidator implements SubmissionValidator {
    private static final List<String> REQUIREMENTS = List.of("validator:backend");
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String segment) {
        return segment != null && segment.equalsIgnoreCase("backend");
    }

    @Override
    public void validate(TaskSubmissionContext context) {
        String metadata = context.request() != null ? context.request().metadata() : null;
        if (!StringUtils.hasText(metadata)) {
            throw error(context, "validation.backend.metadata_required", "Для Backend submit заполните metadata", new ApiErrorDetail("metadata", "required"));
        }
        try {
            JsonNode node = objectMapper.readTree(metadata);
            boolean buildSuccess = node.path("buildSuccess").asBoolean(false);
            String commit = node.path("commit").asText();
            if (!buildSuccess) {
                throw error(context, "validation.backend.build_failed", "buildSuccess должен быть true", new ApiErrorDetail("metadata.buildSuccess", "false"));
            }
            if (!StringUtils.hasText(commit)) {
                throw error(context, "validation.backend.commit_missing", "Укажите commit", new ApiErrorDetail("metadata.commit", "missing"));
            }
        } catch (ApiErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw error(context, "validation.backend.metadata_invalid", "metadata должен быть корректным JSON", new ApiErrorDetail("metadata", ex.getMessage()));
        }
    }

    private ApiErrorException error(TaskSubmissionContext context, String code, String message, ApiErrorDetail detail) {
        List<String> req = new ArrayList<>(context.requirements());
        req.addAll(REQUIREMENTS);
        return new ApiErrorException(HttpStatus.BAD_REQUEST, code, message, List.copyOf(req), List.of(detail));
    }
}


