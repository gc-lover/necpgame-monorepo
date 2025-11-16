package com.necpgame.workqueue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "workqueue.knowledge-import")
public record KnowledgeImportProperties(
        boolean enabled,
        String rootPath,
        String categoryDefault,
        String documentTypeDefault
) {
    public KnowledgeImportProperties {
        rootPath = rootPath == null || rootPath.isBlank() ? "shared/docs/knowledge" : rootPath;
        categoryDefault = categoryDefault == null || categoryDefault.isBlank() ? "knowledge" : categoryDefault;
        documentTypeDefault = documentTypeDefault == null || documentTypeDefault.isBlank() ? "reference" : documentTypeDefault;
    }
}

