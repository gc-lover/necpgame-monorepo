package com.necpgame.workqueue.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubmissionValidatorRegistry {
    private final List<SubmissionValidator> validators;

    public void validate(TaskSubmissionContext context) {
        if (validators == null || validators.isEmpty() || context == null) {
            return;
        }
        String segment = context.item() != null && context.item().getQueue() != null
                ? context.item().getQueue().getSegment()
                : null;
        if (segment == null) {
            return;
        }
        validators.stream()
                .filter(validator -> validator.supports(segment))
                .forEach(validator -> validator.validate(context));
    }
}

