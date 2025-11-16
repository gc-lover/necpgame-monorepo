package com.necpgame.workqueue.service.validation;

public interface SubmissionValidator {
    boolean supports(String segment);

    void validate(TaskSubmissionContext context);
}

