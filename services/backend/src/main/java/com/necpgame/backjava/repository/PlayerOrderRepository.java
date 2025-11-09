package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerOrderEntity;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerOrderRepository extends JpaRepository<PlayerOrderEntity, UUID>, JpaSpecificationExecutor<PlayerOrderEntity> {

    long countByStatusIn(Collection<PlayerOrderStatus> statuses);

    @Query("SELECT AVG(o.payment) FROM PlayerOrderEntity o WHERE o.status IN :statuses")
    Double findAveragePaymentByStatusIn(@Param("statuses") Collection<PlayerOrderStatus> statuses);

    @Query("SELECT o.type, COUNT(o), AVG(o.payment) FROM PlayerOrderEntity o WHERE o.status IN :statuses GROUP BY o.type ORDER BY COUNT(o) DESC")
    List<Object[]> findPopularTypes(@Param("statuses") Collection<PlayerOrderStatus> statuses);

    List<PlayerOrderEntity> findAllByCreatorId(UUID creatorId);

    List<PlayerOrderEntity> findAllByCreatorIdAndStatus(UUID creatorId, PlayerOrderStatus status);

    List<PlayerOrderEntity> findAllByExecutorId(UUID executorId);

    List<PlayerOrderEntity> findAllByExecutorIdAndStatusIn(UUID executorId, Collection<PlayerOrderStatus> statuses);
}

