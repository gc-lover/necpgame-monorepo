package com.necpgame.backjava.repository.narrative;

import com.necpgame.backjava.entity.narrative.NarrativeNpcTemplateEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NarrativeNpcTemplateRepository extends
    JpaRepository<NarrativeNpcTemplateEntity, UUID>,
    JpaSpecificationExecutor<NarrativeNpcTemplateEntity> {
}



