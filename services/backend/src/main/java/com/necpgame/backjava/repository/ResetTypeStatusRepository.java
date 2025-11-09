package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.ResetTypeStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTypeStatusRepository extends JpaRepository<ResetTypeStatusEntity, String> {
}

