package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.VoiceParticipantEntity;
import com.necpgame.backjava.entity.enums.VoiceParticipantStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceParticipantRepository extends JpaRepository<VoiceParticipantEntity, UUID> {

    Optional<VoiceParticipantEntity> findByChannelIdAndPlayerId(UUID channelId, String playerId);

    long countByChannelIdAndStatus(UUID channelId, VoiceParticipantStatus status);

    List<VoiceParticipantEntity> findByChannelId(UUID channelId);
}




