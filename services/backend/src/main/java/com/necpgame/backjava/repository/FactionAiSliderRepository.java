package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FactionAiSliderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FactionAiSliderRepository extends JpaRepository<FactionAiSliderEntity, UUID> {
}

