package com.necpgame.backjava.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.GetMVPEndpoints200Response;
import com.necpgame.backjava.model.GetMVPHealth200Response;
import com.necpgame.backjava.model.GetMVPModels200Response;
import com.necpgame.backjava.model.InitialGameData;
import com.necpgame.backjava.model.MainGameUIData;
import com.necpgame.backjava.model.TextVersionState;
import com.necpgame.backjava.service.mvp.MvpPlayerStateFacade;
import com.necpgame.backjava.service.mvp.MvpProgressFacade;
import com.necpgame.backjava.service.mvp.MvpReferenceFacade;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MvpServiceImplTest {

    @Mock
    private MvpReferenceFacade referenceFacade;
    @Mock
    private MvpProgressFacade progressFacade;
    @Mock
    private MvpPlayerStateFacade playerStateFacade;

    private MvpServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MvpServiceImpl(referenceFacade, progressFacade, playerStateFacade);
    }

    @Test
    void getContentOverviewDelegatesToProgressFacade() {
        ContentOverview overview = new ContentOverview();
        when(progressFacade.getContentOverview("weekly")).thenReturn(overview);

        ContentOverview result = service.getContentOverview("weekly");

        assertSame(overview, result);
        verify(progressFacade).getContentOverview("weekly");
    }

    @Test
    void getInitialDataDelegatesToReferenceFacade() {
        InitialGameData initialData = new InitialGameData();
        when(referenceFacade.getInitialData()).thenReturn(initialData);

        InitialGameData result = service.getInitialData();

        assertSame(initialData, result);
        verify(referenceFacade).getInitialData();
    }

    @Test
    void getMvpHealthDelegatesToProgressFacade() {
        GetMVPHealth200Response health = new GetMVPHealth200Response();
        when(progressFacade.getHealthStatus()).thenReturn(health);

        GetMVPHealth200Response result = service.getMVPHealth();

        assertSame(health, result);
        verify(progressFacade).getHealthStatus();
    }

    @Test
    void getMainGameUiDelegatesToPlayerStateFacade() {
        UUID characterId = UUID.randomUUID();
        MainGameUIData ui = new MainGameUIData();
        when(playerStateFacade.getMainGameUi(characterId)).thenReturn(ui);

        MainGameUIData result = service.getMainGameUI(characterId);

        assertSame(ui, result);
        verify(playerStateFacade).getMainGameUi(characterId);
    }

    @Test
    void getMvpModelsDelegatesToReferenceFacade() {
        GetMVPModels200Response models = new GetMVPModels200Response();
        when(referenceFacade.getModels()).thenReturn(models);

        GetMVPModels200Response result = service.getMVPModels();

        assertSame(models, result);
        verify(referenceFacade).getModels();
    }

    @Test
    void getTextVersionStateDelegatesToPlayerStateFacade() {
        UUID characterId = UUID.randomUUID();
        TextVersionState state = new TextVersionState();
        when(playerStateFacade.getTextVersionState(characterId)).thenReturn(state);

        TextVersionState result = service.getTextVersionState(characterId);

        assertSame(state, result);
        verify(playerStateFacade).getTextVersionState(characterId);
    }

    @Test
    void getContentStatusDelegatesToProgressFacade() {
        ContentStatus status = new ContentStatus();
        when(progressFacade.getContentStatus()).thenReturn(status);

        ContentStatus result = service.getContentStatus();

        assertSame(status, result);
        verify(progressFacade).getContentStatus();
    }

    @Test
    void getMvpEndpointsDelegatesToReferenceFacade() {
        GetMVPEndpoints200Response endpoints = new GetMVPEndpoints200Response();
        when(referenceFacade.getEndpoints()).thenReturn(endpoints);

        GetMVPEndpoints200Response result = service.getMVPEndpoints();

        assertSame(endpoints, result);
        verify(referenceFacade).getEndpoints();
    }
}

