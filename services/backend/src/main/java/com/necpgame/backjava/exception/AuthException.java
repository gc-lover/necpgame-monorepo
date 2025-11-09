package com.necpgame.backjava.exception;

/**
 * РСЃРєР»СЋС‡РµРЅРёРµ РґР»СЏ РѕС€РёР±РѕРє Р°СѓС‚РµРЅС‚РёС„РёРєР°С†РёРё Рё Р°РІС‚РѕСЂРёР·Р°С†РёРё
 * (JWT, Р»РѕРіРёРЅ, РґРѕСЃС‚СѓРї Рє СЂРµСЃСѓСЂСЃР°Рј)
 */
public class AuthException extends ApiException {
    
    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public AuthException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDefaultMessage());
    }
}

