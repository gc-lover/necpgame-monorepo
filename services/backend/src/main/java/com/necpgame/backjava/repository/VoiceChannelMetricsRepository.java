package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.VoiceChannelMetricsEntity;
import com.necpgame.backjava.entity.enums.VoiceChannelOwnerType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceChannelMetricsRepository extends JpaRepository<VoiceChannelMetricsEntity, UUID> {

    List<VoiceChannelMetricsEntity> findByChannelIdAndRecordedAtGreaterThanEqualOrderByRecordedAtAsc(UUID channelId, OffsetDateTime from);

    List<VoiceChannelMetricsEntity> findByOwnerTypeAndOwnerIdAndRecordedAtGreaterThanEqualOrderByRecordedAtAsc(VoiceChannelOwnerType ownerType,
                                                                                                               String ownerId,
                                                                                                               OffsetDateTime from);
}




