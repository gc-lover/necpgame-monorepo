package com.necpgame.backjava.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * РЈС‚РёР»РёС‚Р° РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Security РєРѕРЅС‚РµРєСЃС‚РѕРј
 */
public class SecurityUtil {
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ ID С‚РµРєСѓС‰РµРіРѕ Р°СѓС‚РµРЅС‚РёС„РёС†РёСЂРѕРІР°РЅРЅРѕРіРѕ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ
     */
    public static UUID getCurrentAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        // Р’СЂРµРјРµРЅРЅР°СЏ Р·Р°РіР»СѓС€РєР° - РІРѕР·РІСЂР°С‰Р°РµРј С‚РµСЃС‚РѕРІС‹Р№ UUID
        // TODO: СЂРµР°Р»РёР·РѕРІР°С‚СЊ РёР·РІР»РµС‡РµРЅРёРµ РёР· JWT С‚РѕРєРµРЅР°
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }
}

