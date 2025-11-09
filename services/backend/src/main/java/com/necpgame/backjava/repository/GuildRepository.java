package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuildRepository extends JpaRepository<GuildEntity, UUID> {

    Optional<GuildEntity> findByTagIgnoreCase(String tag);
}

