package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.ReferenceTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceTemplateRepository extends JpaRepository<ReferenceTemplateEntity, String> {
}

