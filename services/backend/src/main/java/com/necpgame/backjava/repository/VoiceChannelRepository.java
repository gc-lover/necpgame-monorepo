package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.VoiceChannelEntity;
import com.necpgame.backjava.entity.enums.VoiceChannelOwnerType;
import com.necpgame.backjava.entity.enums.VoiceChannelStatus;
import com.necpgame.backjava.entity.enums.VoiceChannelType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoiceChannelRepository extends JpaRepository<VoiceChannelEntity, UUID>, JpaSpecificationExecutor<VoiceChannelEntity> {

    boolean existsByOwnerTypeAndOwnerIdAndChannelTypeAndStatusIn(VoiceChannelOwnerType ownerType,
                                                                 String ownerId,
                                                                 VoiceChannelType channelType,
                                                                 Iterable<VoiceChannelStatus> statuses);

    Optional<VoiceChannelEntity> findByIdAndStatusNot(UUID id, VoiceChannelStatus status);
}




