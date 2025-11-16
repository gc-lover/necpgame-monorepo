package com.necpgame.workqueue.service.runner;

import com.necpgame.workqueue.service.KnowledgeImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "workqueue.knowledge-import.enabled", havingValue = "true")
@Slf4j
public class KnowledgeImportRunner implements CommandLineRunner {
    private final KnowledgeImportService knowledgeImportService;

    @Override
    public void run(String... args) {
        log.info("Knowledge import enabled, starting scan");
        knowledgeImportService.importAll();
    }
}

