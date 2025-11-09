package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ accounts - Р°РєРєР°СѓРЅС‚С‹ РёРіСЂРѕРєРѕРІ
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ Account DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_accounts_email", columnList = "email", unique = true),
    @Index(name = "idx_accounts_username", columnList = "username", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;
    
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "last_login")
    private OffsetDateTime lastLogin;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CharacterEntity> characters = new ArrayList<>();
    
    // Helper methods
    public void addCharacter(CharacterEntity character) {
        characters.add(character);
        character.setAccount(this);
    }
    
    public void removeCharacter(CharacterEntity character) {
        characters.remove(character);
        character.setAccount(null);
    }
}

