package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.GetMVPEndpoints200Response;
import com.necpgame.backjava.model.GetMVPHealth200Response;
import com.necpgame.backjava.model.GetMVPModels200Response;
import com.necpgame.backjava.model.InitialGameData;
import com.necpgame.backjava.model.MainGameUIData;
import com.necpgame.backjava.model.TextVersionState;
import com.necpgame.backjava.service.MvpService;
import com.necpgame.backjava.service.mvp.MvpPlayerStateFacade;
import com.necpgame.backjava.service.mvp.MvpProgressFacade;
import com.necpgame.backjava.service.mvp.MvpReferenceFacade;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MvpServiceImpl implements MvpService {

    private final MvpReferenceFacade referenceFacade;
    private final MvpProgressFacade progressFacade;
    private final MvpPlayerStateFacade playerStateFacade;

    @Override
    public ContentOverview getContentOverview(String period) {
        return progressFacade.getContentOverview(period);
    }

    @Override
    public ContentStatus getContentStatus() {
        return progressFacade.getContentStatus();
    }

    @Override
    public InitialGameData getInitialData() {
        return referenceFacade.getInitialData();
    }

    @Override
    public GetMVPEndpoints200Response getMVPEndpoints() {
        return referenceFacade.getEndpoints();
    }

    @Override
    public GetMVPHealth200Response getMVPHealth() {
        return progressFacade.getHealthStatus();
    }

    @Override
    public GetMVPModels200Response getMVPModels() {
        return referenceFacade.getModels();
    }

    @Override
    public MainGameUIData getMainGameUI(UUID characterId) {
        return playerStateFacade.getMainGameUi(characterId);
    }

    @Override
    public TextVersionState getTextVersionState(UUID characterId) {
        return playerStateFacade.getTextVersionState(characterId);
    }
}

