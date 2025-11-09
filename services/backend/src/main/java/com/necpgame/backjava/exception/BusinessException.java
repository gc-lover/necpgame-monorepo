package com.necpgame.backjava.exception;

/**
 * РСЃРєР»СЋС‡РµРЅРёРµ РґР»СЏ Р±РёР·РЅРµСЃ-Р»РѕРіРёРєРё РїСЂРёР»РѕР¶РµРЅРёСЏ
 * (РЅРµ РЅР°Р№РґРµРЅ СЂРµСЃСѓСЂСЃ, РєРѕРЅС„Р»РёРєС‚С‹, Р»РёРјРёС‚С‹)
 */
public class BusinessException extends ApiException {
    
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    public BusinessException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDefaultMessage());
    }
}

