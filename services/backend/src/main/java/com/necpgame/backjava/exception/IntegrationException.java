package com.necpgame.backjava.exception;

/**
 * РСЃРєР»СЋС‡РµРЅРёРµ РґР»СЏ РѕС€РёР±РѕРє РёРЅС‚РµРіСЂР°С†РёРё СЃ РІРЅРµС€РЅРёРјРё СЃРёСЃС‚РµРјР°РјРё
 * (Р‘Р”, РІРЅРµС€РЅРёРµ API, С„Р°Р№Р»РѕРІР°СЏ СЃРёСЃС‚РµРјР°)
 */
public class IntegrationException extends ApiException {
    
    public IntegrationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public IntegrationException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDefaultMessage());
    }
}

