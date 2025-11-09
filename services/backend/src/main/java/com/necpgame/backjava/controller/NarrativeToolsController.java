package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.NarrativeToolsApi;
import com.necpgame.backjava.model.GenerateNPC200Response;
import com.necpgame.backjava.model.GenerateNPCRequest;
import com.necpgame.backjava.model.GenerateQuestRequest;
import com.necpgame.backjava.model.ValidateNarrative200Response;
import com.necpgame.backjava.model.ValidateNarrativeRequest;
import com.necpgame.backjava.service.NarrativeToolsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NarrativeToolsController implements NarrativeToolsApi {

    private final NarrativeToolsService narrativeToolsService;

    @Override
    public ResponseEntity<GenerateNPC200Response> generateNPC(GenerateNPCRequest generateNPCRequest) {
        GenerateNPC200Response npc = narrativeToolsService.generateNPC(generateNPCRequest);
        return ResponseEntity.ok(npc);
    }

    @Override
    public ResponseEntity<Object> generateQuest(GenerateQuestRequest generateQuestRequest) {
        Object quest = narrativeToolsService.generateQuest(generateQuestRequest);
        if (quest instanceof Map<?, ?> questMap) {
            return ResponseEntity.ok(questMap);
        }
        return ResponseEntity.ok(quest);
    }

    @Override
    public ResponseEntity<ValidateNarrative200Response> validateNarrative(ValidateNarrativeRequest validateNarrativeRequest) {
        ValidateNarrative200Response result = narrativeToolsService.validateNarrative(validateNarrativeRequest);
        return ResponseEntity.ok(result);
    }
}



