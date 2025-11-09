package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.LoginRequest;
import com.necpgame.backjava.model.LoginResponse;
import com.necpgame.backjava.model.Register201Response;
import com.necpgame.backjava.model.RegisterRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for AuthService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface AuthService {

    /**
     * POST /auth/login : Р’С…РѕРґ РІ СЃРёСЃС‚РµРјСѓ
     * РђСѓС‚РµРЅС‚РёС„РёРєР°С†РёСЏ РёРіСЂРѕРєР° РїРѕ email РёР»Рё username Рё РїР°СЂРѕР»СЋ. Р’РѕР·РІСЂР°С‰Р°РµС‚ JWT С‚РѕРєРµРЅ.
     *
     * @param loginRequest  (required)
     * @return LoginResponse
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * POST /auth/register : Р РµРіРёСЃС‚СЂР°С†РёСЏ РЅРѕРІРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°
     * РЎРѕР·РґР°РµС‚ РЅРѕРІС‹Р№ Р°РєРєР°СѓРЅС‚ РёРіСЂРѕРєР°. РџСЂРѕРІРµСЂСЏРµС‚ СѓРЅРёРєР°Р»СЊРЅРѕСЃС‚СЊ email Рё username.
     *
     * @param registerRequest  (required)
     * @return Register201Response
     */
    Register201Response register(RegisterRequest registerRequest);
}

