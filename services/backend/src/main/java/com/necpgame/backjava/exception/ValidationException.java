package com.necpgame.backjava.exception;

/**
 * РСЃРєР»СЋС‡РµРЅРёРµ РґР»СЏ РѕС€РёР±РѕРє РІР°Р»РёРґР°С†РёРё РґР°РЅРЅС‹С… РѕС‚ РєР»РёРµРЅС‚Р°
 * (РЅРµРєРѕСЂСЂРµРєС‚РЅС‹Рµ РґР°РЅРЅС‹Рµ, РѕС‚СЃСѓС‚СЃС‚РІСѓСЋС‰РёРµ РїРѕР»СЏ, РЅРµРІРµСЂРЅС‹Р№ С„РѕСЂРјР°С‚)
 */
public class ValidationException extends ApiException {
    
    public ValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public ValidationException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDefaultMessage());
    }
}

