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
public class VisionSubmissionValidator implements SubmissionValidator {
    private static final List<String> REQUIREMENTS = List.of("validator:vision");
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String segment) {
        return segment != null && segment.equalsIgnoreCase("vision");
    }

    @Override
    public void validate(TaskSubmissionContext context) {
        String metadata = context.request() != null ? context.request().metadata() : null;
        if (!StringUtils.hasText(metadata)) {
            throw error(context, "validation.vision.metadata_required", "Для Vision submit заполните metadata с handoff", new ApiErrorDetail("metadata", "required"));
        }
        try {
            JsonNode node = objectMapper.readTree(metadata);
            JsonNode handoff = node.path("handoff");
            String next = handoff.path("nextSegment").asText();
            if (!StringUtils.hasText(next) || !"api".equalsIgnoreCase(next)) {
                throw error(context, "validation.vision.handoff_next_invalid", "handoff.nextSegment должен быть 'api'", new ApiErrorDetail("metadata.handoff.nextSegment", next));
            }
        } catch (ApiErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw error(context, "validation.vision.metadata_invalid", "metadata должен быть корректным JSON", new ApiErrorDetail("metadata", ex.getMessage()));
        }
    }

    private ApiErrorException error(TaskSubmissionContext context, String code, String message, ApiErrorDetail detail) {
        List<String> req = new ArrayList<>(context.requirements());
        req.addAll(REQUIREMENTS);
        return new ApiErrorException(HttpStatus.BAD_REQUEST, code, message, List.copyOf(req), List.of(detail));
    }
}


