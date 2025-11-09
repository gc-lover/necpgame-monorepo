package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestTemplateDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestTemplateDefinitionRepository extends JpaRepository<QuestTemplateDefinitionEntity, String> {
}



