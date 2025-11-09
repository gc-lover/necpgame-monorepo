package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.FactionEntity;
import com.necpgame.backjava.model.Faction;
import com.necpgame.backjava.model.GetFactions200Response;
import com.necpgame.backjava.repository.FactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit тест для FactionsServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class FactionsServiceImplTest {
    
    @Mock
    private FactionRepository factionRepository;
    
    @InjectMocks
    private FactionsServiceImpl factionsService;
    
    private FactionEntity arasaka;
    private FactionEntity militech;
    
    @BeforeEach
    void setUp() {
        // Создаём тестовые данные
        arasaka = new FactionEntity();
        arasaka.setId(UUID.randomUUID());
        arasaka.setName("Arasaka");
        arasaka.setType(FactionEntity.FactionType.corporation);
        arasaka.setDescription("Могущественная корпорация");
        
        militech = new FactionEntity();
        militech.setId(UUID.randomUUID());
        militech.setName("Militech");
        militech.setType(FactionEntity.FactionType.corporation);
        militech.setDescription("Военная корпорация");
    }
    
    @Test
    void getFactions_shouldReturnAllFactions() {
        // Arrange
        when(factionRepository.findAll()).thenReturn(Arrays.asList(arasaka, militech));
        
        // Act
        GetFactions200Response response = factionsService.getFactions(null);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getFactions());
        assertEquals(2, response.getFactions().size());
        
        Faction faction1 = response.getFactions().get(0);
        assertEquals("Arasaka", faction1.getName());
        assertEquals(Faction.TypeEnum.CORPORATION, faction1.getType());
        assertEquals("Могущественная корпорация", faction1.getDescription());
        
        verify(factionRepository, times(1)).findAll();
    }
    
    @Test
    void getFactions_shouldHandleEmptyList() {
        // Arrange
        when(factionRepository.findAll()).thenReturn(List.of());
        
        // Act
        GetFactions200Response response = factionsService.getFactions(null);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getFactions());
        assertTrue(response.getFactions().isEmpty());
    }
    
    @Test
    void getFactions_shouldMapEnumCorrectly() {
        // Arrange
        FactionEntity gang = new FactionEntity();
        gang.setId(UUID.randomUUID());
        gang.setName("Valentinos");
        gang.setType(FactionEntity.FactionType.gang);
        gang.setDescription("Уличная банда");
        
        when(factionRepository.findAll()).thenReturn(List.of(gang));
        
        // Act
        GetFactions200Response response = factionsService.getFactions(null);
        
        // Assert
        assertEquals(1, response.getFactions().size());
        assertEquals(Faction.TypeEnum.GANG, response.getFactions().get(0).getType());
    }
}

