package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.GuildMemberEntity;
import com.necpgame.backjava.entity.GuildMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMemberEntity, GuildMemberId> {

    List<GuildMemberEntity> findByIdGuildId(UUID guildId);

    long countByIdGuildId(UUID guildId);

    Optional<GuildMemberEntity> findByIdCharacterId(UUID characterId);
}

