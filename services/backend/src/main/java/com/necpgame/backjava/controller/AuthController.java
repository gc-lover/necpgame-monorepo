package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.AuthApi;
import com.necpgame.backjava.model.LoginRequest;
import com.necpgame.backjava.model.LoginResponse;
import com.necpgame.backjava.model.Register201Response;
import com.necpgame.backjava.model.RegisterRequest;
import com.necpgame.backjava.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller РґР»СЏ Р°СѓС‚РµРЅС‚РёС„РёРєР°С†РёРё Рё СЂРµРіРёСЃС‚СЂР°С†РёРё.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link AuthApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/auth/character-creation.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    
    private final AuthService authService;
    
    @Override
    public ResponseEntity<Register201Response> register(RegisterRequest registerRequest) {
        log.info("POST /auth/register - {}", registerRequest.getEmail());
        Register201Response response = authService.register(registerRequest);
        return ResponseEntity.status(201).body(response);
    }
    
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        log.info("POST /auth/login - {}", loginRequest.getLogin());
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}

