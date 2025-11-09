package com.necpgame.socialservice.service;

import com.necpgame.socialservice.model.Error;
import com.necpgame.socialservice.model.GetNPCs200Response;
import com.necpgame.socialservice.model.InteractWithNPC200Response;
import com.necpgame.socialservice.model.InteractWithNPCRequest;
import com.necpgame.socialservice.model.NPC;
import com.necpgame.socialservice.model.NPCDialogue;
import org.springframework.lang.Nullable;
import com.necpgame.socialservice.model.RespondToDialogueRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for NpcsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface NpcsService {

    /**
     * GET /npcs/{npcId} : Детали NPC
     *
     * @param npcId  (required)
     * @param characterId  (required)
     * @return NPC
     */
    NPC getNPCDetails(String npcId, UUID characterId);

    /**
     * GET /npcs/{npcId}/dialogue : Диалог с NPC
     *
     * @param npcId  (required)
     * @param characterId  (required)
     * @return NPCDialogue
     */
    NPCDialogue getNPCDialogue(String npcId, UUID characterId);

    /**
     * GET /npcs : Список всех NPC
     *
     * @param characterId  (required)
     * @param type  (optional)
     * @return GetNPCs200Response
     */
    GetNPCs200Response getNPCs(UUID characterId, String type);

    /**
     * GET /npcs/location/{locationId} : NPC в локации
     *
     * @param locationId  (required)
     * @param characterId  (required)
     * @return GetNPCs200Response
     */
    GetNPCs200Response getNPCsByLocation(String locationId, UUID characterId);

    /**
     * POST /npcs/{npcId}/interact : Взаимодействие с NPC
     *
     * @param npcId  (required)
     * @param interactWithNPCRequest  (optional)
     * @return InteractWithNPC200Response
     */
    InteractWithNPC200Response interactWithNPC(String npcId, InteractWithNPCRequest interactWithNPCRequest);

    /**
     * POST /npcs/{npcId}/dialogue/respond : Ответить в диалоге
     *
     * @param npcId  (required)
     * @param respondToDialogueRequest  (optional)
     * @return NPCDialogue
     */
    NPCDialogue respondToDialogue(String npcId, RespondToDialogueRequest respondToDialogueRequest);
}

