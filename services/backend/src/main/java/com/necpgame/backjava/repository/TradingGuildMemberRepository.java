package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildMemberEntity;
import com.necpgame.backjava.entity.TradingGuildMemberId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildMemberRepository extends JpaRepository<TradingGuildMemberEntity, TradingGuildMemberId> {

    List<TradingGuildMemberEntity> findByIdGuildId(UUID guildId);

    Optional<TradingGuildMemberEntity> findByIdGuildIdAndIdCharacterId(UUID guildId, UUID characterId);

    long countByIdGuildId(UUID guildId);
}

