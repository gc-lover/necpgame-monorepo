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
import com.necpgame.backjava.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Unit тест для AuthServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private AuthServiceImpl authService;
    
    @BeforeEach
    void setUp() {
        // Mock для JwtUtil (lenient чтобы избежать UnnecessaryStubbing)
        lenient().when(jwtUtil.generateToken(any(UUID.class), anyString()))
            .thenReturn("test-jwt-token");
    }
    
    @Test
    void register_shouldCreateNewAccount() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("Password123!");
        request.setPasswordConfirm("Password123!");
        request.setTermsAccepted(true);
        
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-password");
        
        AccountEntity savedAccount = new AccountEntity();
        savedAccount.setId(UUID.randomUUID());
        savedAccount.setEmail("test@example.com");
        savedAccount.setUsername("testuser");
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(savedAccount);
        
        // Act
        Register201Response response = authService.register(request);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertEquals("Account created successfully", response.getMessage());
        
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }
    
    @Test
    void register_shouldThrowExceptionWhenEmailExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setUsername("newuser");
        request.setPassword("Password123!");
        request.setPasswordConfirm("Password123!");
        request.setTermsAccepted(true);
        
        when(accountRepository.existsByEmail("existing@example.com")).thenReturn(true);
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> authService.register(request));
        assertEquals(ErrorCode.RESOURCE_ALREADY_EXISTS, exception.getErrorCode());
        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }
    
    @Test
    void register_shouldThrowExceptionWhenPasswordsDontMatch() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("Password123!");
        request.setPasswordConfirm("DifferentPassword123!");
        request.setTermsAccepted(true);
        
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByUsername(anyString())).thenReturn(false);
        
        // Act & Assert
        assertThrows(Exception.class, () -> authService.register(request));
    }
    
    @Test
    void login_shouldReturnTokenForValidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setLogin("test@example.com");
        request.setPassword("Password123!");
        
        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setEmail("test@example.com");
        account.setUsername("testuser");
        account.setPasswordHash("hashed-password");
        account.setIsActive(true);
        
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("Password123!", "hashed-password")).thenReturn(true);
        
        // Act
        LoginResponse response = authService.login(request);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertNotNull(response.getToken());
        assertEquals("test-jwt-token", response.getToken());
        assertNotNull(response.getExpiresAt());
        
        // login() is readOnly transaction - no save() call
        verify(accountRepository, never()).save(any(AccountEntity.class));
    }
    
    @Test
    void login_shouldThrowExceptionForInvalidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setLogin("test@example.com");
        request.setPassword("WrongPassword!");
        
        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setEmail("test@example.com");
        account.setPasswordHash("hashed-password");
        account.setIsActive(true);
        
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("WrongPassword!", "hashed-password")).thenReturn(false);
        
        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, 
            () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
    }
}

