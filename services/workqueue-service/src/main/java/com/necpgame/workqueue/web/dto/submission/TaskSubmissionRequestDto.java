package com.necpgame.workqueue.web.dto.submission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TaskSubmissionRequestDto(
        @NotBlank String notes,
        List<@Valid SubmissionArtifactDto> artifacts,
        String metadata
) {
    public record SubmissionArtifactDto(
            @NotBlank String title,
            @NotBlank String url
    ) {
    }
}

