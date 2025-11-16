package com.necpgame.workqueue.service.model;

public record StoredArtifactFile(
        String originalFilename,
        String storagePath,
        String mediaType,
        long sizeBytes
) {
}

