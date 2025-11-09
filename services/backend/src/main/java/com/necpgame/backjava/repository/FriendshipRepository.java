package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для управления друзьями
 */
@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, String> {
    
    /**
     * Найти всех друзей персонажа (bidirectional)
     */
    @Query("SELECT f FROM FriendshipEntity f WHERE (f.characterId1 = :characterId OR f.characterId2 = :characterId) AND f.status = 'accepted'")
    List<FriendshipEntity> findAcceptedFriendships(String characterId);
    
    /**
     * Найти pending requests для персонажа
     */
    List<FriendshipEntity> findByCharacterId2AndStatus(String characterId, String status);
}

