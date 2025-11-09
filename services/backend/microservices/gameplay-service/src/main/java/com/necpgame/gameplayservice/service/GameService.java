package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.GameReturnResponse;
import com.necpgame.gameplayservice.model.GameStartRequest;
import com.necpgame.gameplayservice.model.GameStartResponse;
import com.necpgame.gameplayservice.model.ReturnToGameRequest;
import java.util.UUID;
import com.necpgame.gameplayservice.model.WelcomeScreenResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameService {

    /**
     * GET /game/welcome : Получить приветственный экран
     * Возвращает приветственный экран для персонажа.  **Бизнес-логика:** - Показывает приветственное сообщение - Отображает информацию о персонаже - Предлагает начать игру или пропустить туториал  **Источник:** &#x60;.BRAIN/05-technical/ui-game-start.md&#x60; (Экран приветствия) 
     *
     * @param characterId ID персонажа (required)
     * @return WelcomeScreenResponse
     */
    WelcomeScreenResponse getWelcomeScreen(UUID characterId);

    /**
     * POST /game/return : Вернуться в игру
     * Возвращает игрока в игру при повторном входе.  **Бизнес-логика:** - Загружает сохраненное состояние персонажа - Восстанавливает текущую локацию - Загружает активные квесты - Создает новую игровую сессию  **Источник:** &#x60;.BRAIN/05-technical/game-start-scenario.md&#x60; (Повторный вход) 
     *
     * @param returnToGameRequest  (required)
     * @return GameReturnResponse
     */
    GameReturnResponse returnToGame(ReturnToGameRequest returnToGameRequest);

    /**
     * POST /game/start : Начать игру
     * Начинает игру для созданного персонажа.  **Бизнес-логика:** - Персонаж появляется в стартовой локации (Downtown, Night City) - Выдается стартовое снаряжение (пистолет Liberty, уличная броня) - Выдаются стартовые деньги (500 eddies) - Устанавливаются начальные характеристики (здоровье 100, энергия 100, человечность 100) - Создается игровая сессия - Опционально включается туториал  **Источник:** &#x60;.BRAIN/05-technical/game-start-scenario.md&#x60; (Этап 1-2) 
     *
     * @param gameStartRequest  (required)
     * @return GameStartResponse
     */
    GameStartResponse startGame(GameStartRequest gameStartRequest);
}

