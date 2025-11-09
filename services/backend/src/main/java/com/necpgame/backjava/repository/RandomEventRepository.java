package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.RandomEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RandomEventRepository extends JpaRepository<RandomEventEntity, String>, JpaSpecificationExecutor<RandomEventEntity> {

    List<RandomEventEntity> findByActiveTrue();
}

