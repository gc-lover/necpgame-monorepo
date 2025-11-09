package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.AccountEntity;
import com.necpgame.backjava.exception.AuthException;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.model.LoginRequest;
import com.necpgame.backjava.model.LoginResponse;
import com.necpgame.backjava.model.Register201Response;
import com.necpgame.backjava.model.RegisterRequest;
import com.necpgame.backjava.repository.AccountRepository;
import com.necpgame.backjava.service.AuthService;
import com.necpgame.backjava.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Р РµР°Р»РёР·Р°С†РёСЏ СЃРµСЂРІРёСЃР° Р°СѓС‚РµРЅС‚РёС„РёРєР°С†РёРё
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * Р РµРіРёСЃС‚СЂР°С†РёСЏ РЅРѕРІРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°
     */
    @Override
    @Transactional
    public Register201Response register(RegisterRequest request) {
        log.info("Registering new account: {}", request.getEmail());
        
        // РџСЂРѕРІРµСЂРєР° СѓРЅРёРєР°Р»СЊРЅРѕСЃС‚Рё email
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS, 
                "Account with this email already exists");
        }
        
        // РџСЂРѕРІРµСЂРєР° СѓРЅРёРєР°Р»СЊРЅРѕСЃС‚Рё username
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.RESOURCE_ALREADY_EXISTS, 
                "Account with this username already exists");
        }
        
        // РЎРѕР·РґР°РЅРёРµ РЅРѕРІРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°
        AccountEntity account = new AccountEntity();
        account.setEmail(request.getEmail());
        account.setUsername(request.getUsername());
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        account = accountRepository.save(account);
        log.info("Account created successfully: {}", account.getId());
        
        // Р“РµРЅРµСЂР°С†РёСЏ JWT С‚РѕРєРµРЅР°
        String token = jwtUtil.generateToken(account.getId(), account.getEmail());
        
        // Р¤РѕСЂРјРёСЂРѕРІР°РЅРёРµ РѕС‚РІРµС‚Р°
        Register201Response response = new Register201Response();
        response.setAccountId(account.getId());
        response.setMessage("Account created successfully");
        
        return response;
    }
    
    /**
     * Р’С…РѕРґ РІ СЃРёСЃС‚РµРјСѓ
     */
    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getLogin());
        
        // РџРѕРёСЃРє Р°РєРєР°СѓРЅС‚Р° РїРѕ email РёР»Рё username
        AccountEntity account = accountRepository.findByEmail(request.getLogin())
            .or(() -> accountRepository.findByUsername(request.getLogin()))
            .orElseThrow(() -> new AuthException(ErrorCode.INVALID_CREDENTIALS));
        
        // РџСЂРѕРІРµСЂРєР° РїР°СЂРѕР»СЏ
        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS);
        }
        
        log.info("Login successful for account: {}", account.getId());
        
        // Р“РµРЅРµСЂР°С†РёСЏ JWT С‚РѕРєРµРЅР°
        String token = jwtUtil.generateToken(account.getId(), account.getEmail());
        
        // Р¤РѕСЂРјРёСЂРѕРІР°РЅРёРµ РѕС‚РІРµС‚Р°
        LoginResponse response = new LoginResponse();
        response.setAccountId(account.getId());
        response.setToken(token);
        response.setExpiresAt(OffsetDateTime.now().plusHours(24));
        
        return response;
    }
}

