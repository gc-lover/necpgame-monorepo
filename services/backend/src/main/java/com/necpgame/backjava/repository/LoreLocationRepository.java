package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreLocationEntity;
import com.necpgame.backjava.entity.enums.LoreLocationType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoreLocationRepository extends JpaRepository<LoreLocationEntity, UUID>, JpaSpecificationExecutor<LoreLocationEntity> {

    Optional<LoreLocationEntity> findByExternalId(String externalId);

    List<LoreLocationEntity> findTop10ByNameContainingIgnoreCase(String name);

    List<LoreLocationEntity> findTop10ByDescriptionShortContainingIgnoreCase(String descriptionShort);

    List<LoreLocationEntity> findTop10ByType(LoreLocationType type);
}

