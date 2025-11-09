package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GenerateNPC200Response;
import com.necpgame.backjava.model.GenerateNPCRequest;
import com.necpgame.backjava.model.GenerateQuestRequest;
import com.necpgame.backjava.model.ValidateNarrative200Response;
import com.necpgame.backjava.model.ValidateNarrativeRequest;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NarrativeToolsService {

    GenerateNPC200Response generateNPC(@Valid GenerateNPCRequest generateNPCRequest);

    Object generateQuest(@Valid GenerateQuestRequest generateQuestRequest);

    ValidateNarrative200Response validateNarrative(@Valid ValidateNarrativeRequest validateNarrativeRequest);
}



