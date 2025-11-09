package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.MailMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MailMessageRepository extends JpaRepository<MailMessageEntity, Long> {

    Page<MailMessageEntity> findByRecipientCharacterIdAndDeletedFalse(UUID recipientCharacterId, Pageable pageable);

    Optional<MailMessageEntity> findByIdAndDeletedFalse(Long id);
}

