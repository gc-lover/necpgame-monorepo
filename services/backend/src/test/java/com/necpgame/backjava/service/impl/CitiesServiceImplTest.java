package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.entity.CityEntity;
import com.necpgame.backjava.model.City;
import com.necpgame.backjava.model.GetCities200Response;
import com.necpgame.backjava.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CitiesServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CitiesServiceImpl citiesService;

    private CityEntity nightCity;
    private CityEntity neoTokyo;

    @BeforeEach
    void setUp() {
        nightCity = new CityEntity();
        nightCity.setId(UUID.randomUUID());
        nightCity.setName("Night City");
        nightCity.setRegion("US");
        nightCity.setDescription("Город будущего");

        neoTokyo = new CityEntity();
        neoTokyo.setId(UUID.randomUUID());
        neoTokyo.setName("Neo-Tokyo");
        neoTokyo.setRegion("ASIA");
        neoTokyo.setDescription("Неоновый мегаполис");
    }

    @Test
    void getCities_shouldReturnAllCities() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(nightCity, neoTokyo));

        GetCities200Response response = citiesService.getCities(null, null);

        assertNotNull(response);
        assertNotNull(response.getCities());
        assertEquals(2, response.getCities().size());

        City city1 = response.getCities().get(0);
        assertEquals("Night City", city1.getName());
        assertEquals("US", city1.getRegion());
        assertEquals("Город будущего", city1.getDescription());

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void getCities_shouldFilterByRegion() {
        when(cityRepository.findByRegion("US")).thenReturn(List.of(nightCity));

        GetCities200Response response = citiesService.getCities(null, "US");

        assertNotNull(response);
        assertEquals(1, response.getCities().size());
        assertEquals("Night City", response.getCities().get(0).getName());

        verify(cityRepository, times(1)).findByRegion("US");
    }

    @Test
    void getCities_shouldHandleEmptyList() {
        when(cityRepository.findAll()).thenReturn(List.of());

        GetCities200Response response = citiesService.getCities(null, null);

        assertNotNull(response);
        assertNotNull(response.getCities());
        assertTrue(response.getCities().isEmpty());
    }
}

