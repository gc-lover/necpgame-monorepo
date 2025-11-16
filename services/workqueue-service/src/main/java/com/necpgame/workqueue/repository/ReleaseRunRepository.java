package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.release.ReleaseRunEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReleaseRunRepository extends JpaRepository<ReleaseRunEntity, UUID> {
    @EntityGraph(attributePaths = {"status", "impactLevel"})
    List<ReleaseRunEntity> findAllByOrderByReleaseDateDesc();

    @EntityGraph(attributePaths = {
            "status",
            "impactLevel",
            "author",
            "author.role",
            "steps",
            "steps.status",
            "validations",
            "validations.status",
            "validations.validatedBy"
    })
    Optional<ReleaseRunEntity> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {
            "status",
            "impactLevel",
            "author",
            "steps",
            "steps.status",
            "validations",
            "validations.status",
            "validations.validatedBy"
    })
    Optional<ReleaseRunEntity> findDetailedByChangeIdIgnoreCase(String changeId);
}


