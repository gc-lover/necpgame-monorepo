package com.necpgame.backjava.service.player;

import com.necpgame.backjava.entity.GameSessionEntity;
import com.necpgame.backjava.repository.GameSessionRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    public GameSessionEntity openSession(UUID accountId, UUID characterId, String locationId) {
        closeActiveSessions(accountId);
        GameSessionEntity session = new GameSessionEntity();
        session.setAccountId(accountId);
        session.setCharacterId(characterId);
        session.setLocationId(locationId);
        session.setTutorialEnabled(false);
        session.setSessionStart(nowUtc());
        session.setIsActive(true);
        return gameSessionRepository.save(session);
    }

    public void closeActiveSessions(UUID accountId) {
        List<GameSessionEntity> sessions = gameSessionRepository.findByAccountIdAndIsActiveTrue(accountId);
        if (sessions.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        sessions.forEach(session -> {
            session.setIsActive(false);
            session.setSessionEnd(now);
        });
        gameSessionRepository.saveAll(sessions);
    }

    private LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}

