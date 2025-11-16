package com.necpgame.workqueue.service.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenApiSubmissionValidatorTest {
    private OpenApiSubmissionValidator validator;
    private QueueItemEntity item;

    @BeforeEach
    void setUp() {
        validator = new OpenApiSubmissionValidator(new ObjectMapper());
        QueueEntity queue = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment("api")
                .statusCode("in_progress")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        item = QueueItemEntity.builder()
                .id(UUID.randomUUID())
                .queue(queue)
                .title("API Spec")
                .build();
    }

    @Test
    void supportsOnlyApiSegment() {
        assertThat(validator.supports("api")).isTrue();
        assertThat(validator.supports("API")).isTrue();
        assertThat(validator.supports("backend")).isFalse();
    }

    @Test
    void validateFailsWhenMetadataMissing() {
        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto("notes",
                List.of(new TaskSubmissionRequestDto.SubmissionArtifactDto("spec", "https://specs/api.yaml")),
                null);
        TaskSubmissionContext context = new TaskSubmissionContext(item, request, List.of(), request.artifacts(), List.of());

        assertThatThrownBy(() -> validator.validate(context))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("metadata");
    }

    @Test
    void validateFailsWhenSpecArtifactMissing() {
        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto("notes",
                List.of(new TaskSubmissionRequestDto.SubmissionArtifactDto("report", "https://ci/result.html")),
                """
                        {"openapiVersion":"3.0.3","specPath":"services/openapi/api/v1/foo.yaml"}
                        """);
        TaskSubmissionContext context = new TaskSubmissionContext(item, request, List.of(), request.artifacts(), List.of());

        var thrown = org.assertj.core.api.Assertions.catchThrowable(() -> validator.validate(context));

        assertThat(thrown).isInstanceOf(ApiErrorException.class);
        assertThat(((ApiErrorException) thrown).getCode()).isEqualTo("validation.api.spec_artifact_required");
    }

    @Test
    void validatePassesWithYamlFile() {
        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto("notes",
                List.of(),
                """
                        {"openapiVersion":"3.1.0","specPath":"services/openapi/api/v1/ping.yaml"}
                        """);
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("ping.yaml");
        TaskSubmissionContext context = new TaskSubmissionContext(
                item,
                request,
                List.of(file),
                List.of(),
                List.of()
        );

        validator.validate(context);
    }
}

