package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestCatalogEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestCatalogEntryRepository extends JpaRepository<QuestCatalogEntryEntity, String>,
    JpaSpecificationExecutor<QuestCatalogEntryEntity> {
}


