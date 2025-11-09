package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterRestoreQueueEntity;
import com.necpgame.backjava.entity.CharacterRestoreQueueEntity.RestoreStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRestoreQueueRepository extends JpaRepository<CharacterRestoreQueueEntity, UUID> {

    List<CharacterRestoreQueueEntity> findByAccountIdAndStatus(UUID accountId, RestoreStatus status);

    Optional<CharacterRestoreQueueEntity> findByCharacterIdAndStatus(UUID characterId, RestoreStatus status);
}
