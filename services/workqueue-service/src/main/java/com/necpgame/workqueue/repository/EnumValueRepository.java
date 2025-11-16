package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnumValueRepository extends JpaRepository<EnumValueEntity, UUID> {
    @Query("select v from EnumValueEntity v where lower(v.code) = lower(:code) and v.group.code = :groupCode")
    Optional<EnumValueEntity> findByGroupCodeAndCode(@Param("groupCode") String groupCode, @Param("code") String code);

    @Query("select v from EnumValueEntity v where v.group.code = :groupCode order by v.sortOrder asc, v.displayName asc")
    List<EnumValueEntity> findByGroupCodeOrderBySortOrder(@Param("groupCode") String groupCode);
}


