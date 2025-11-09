package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpStarterNpcEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpStarterNpcRepository extends JpaRepository<MvpStarterNpcEntity, UUID> {

    @EntityGraph(attributePaths = "location")
    List<MvpStarterNpcEntity> findAllByOrderByNameAsc();

}
