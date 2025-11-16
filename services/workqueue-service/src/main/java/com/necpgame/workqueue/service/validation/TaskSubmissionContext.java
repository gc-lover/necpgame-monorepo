package com.necpgame.workqueue.service.validation;

import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record TaskSubmissionContext(
        QueueItemEntity item,
        TaskSubmissionRequestDto request,
        List<MultipartFile> files,
        List<TaskSubmissionRequestDto.SubmissionArtifactDto> linkArtifacts,
        List<String> requirements
) {
}

