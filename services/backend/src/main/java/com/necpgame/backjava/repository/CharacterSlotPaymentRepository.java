package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSlotPaymentEntity;
import com.necpgame.backjava.entity.CharacterSlotPaymentEntity.PaymentStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterSlotPaymentRepository extends JpaRepository<CharacterSlotPaymentEntity, UUID> {

    List<CharacterSlotPaymentEntity> findByAccountIdAndStatusIn(UUID accountId, List<PaymentStatus> statuses);
}
