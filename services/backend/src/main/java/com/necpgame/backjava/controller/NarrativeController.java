package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.NarrativeApi;
import com.necpgame.backjava.model.CompleteQuestRequest;
import com.necpgame.backjava.model.DialogueChoiceRequest;
import com.necpgame.backjava.model.DialogueChoiceResult;
import com.necpgame.backjava.model.DialogueNode;
import com.necpgame.backjava.model.DialogueTree;
import com.necpgame.backjava.model.FactionQuestDetailed;
import com.necpgame.backjava.model.GetActiveQuests200Response;
import com.necpgame.backjava.model.GetAvailableFactionQuests200Response;
import com.necpgame.backjava.model.GetFactionQuestProgress200Response;
import com.necpgame.backjava.model.GetQuestCatalog200Response;
import com.necpgame.backjava.model.GetQuestBranches200Response;
import com.necpgame.backjava.model.GetQuestChains200Response;
import com.necpgame.backjava.model.GetQuestEndings200Response;
import com.necpgame.backjava.model.GetQuestRecommendations200Response;
import com.necpgame.backjava.model.ListFactionQuests200Response;
import com.necpgame.backjava.model.QuestCompletionResult;
import com.necpgame.backjava.model.QuestDetails;
import com.necpgame.backjava.model.QuestInstance;
import com.necpgame.backjava.model.QuestLootTable;
import com.necpgame.backjava.model.SearchQuests200Response;
import com.necpgame.backjava.model.SkillCheckRequest;
import com.necpgame.backjava.model.SkillCheckResult;
import com.necpgame.backjava.model.StartQuestRequest;
import com.necpgame.backjava.service.NarrativeService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NarrativeController implements NarrativeApi {

    private final NarrativeService narrativeService;

    @Override
    public ResponseEntity<GetQuestCatalog200Response> getQuestCatalog(@Nullable String type, @Nullable String period, @Nullable String difficulty, @Nullable String faction, @Nullable Integer minLevel, @Nullable Integer maxLevel, @Nullable Boolean hasRomance, @Nullable Boolean hasCombat, @Nullable Integer estimatedTimeMin, @Nullable Integer estimatedTimeMax, @Nullable Integer page, @Nullable Integer pageSize) {
        GetQuestCatalog200Response response = narrativeService.getQuestCatalog(type, period, difficulty, faction, minLevel, maxLevel, hasRomance, hasCombat, estimatedTimeMin, estimatedTimeMax, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SearchQuests200Response> searchQuests(String q, @Nullable List<String> searchIn, @Nullable Integer page, @Nullable Integer pageSize) {
        SearchQuests200Response response = narrativeService.searchQuests(q, searchIn, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<QuestDetails> getQuestDetails(String questId) {
        QuestDetails details = narrativeService.getQuestDetails(questId);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<DialogueTree> getQuestDialogueTree(String questId) {
        DialogueTree tree = narrativeService.getQuestDialogueTree(questId);
        return ResponseEntity.ok(tree);
    }

    @Override
    public ResponseEntity<QuestLootTable> getQuestLootTable(String questId) {
        QuestLootTable lootTable = narrativeService.getQuestLootTable(questId);
        return ResponseEntity.ok(lootTable);
    }

    @Override
    public ResponseEntity<GetQuestRecommendations200Response> getQuestRecommendations(UUID characterId, @Nullable Integer count) {
        GetQuestRecommendations200Response response = narrativeService.getQuestRecommendations(characterId, count);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetQuestChains200Response> getQuestChains(@Nullable String faction, @Nullable String storyline) {
        GetQuestChains200Response response = narrativeService.getQuestChains(faction, storyline);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ListFactionQuests200Response> listFactionQuests(@Nullable String faction, @Nullable Integer minReputation, @Nullable Integer playerLevelMin, @Nullable Integer page, @Nullable Integer pageSize) {
        ListFactionQuests200Response response = narrativeService.listFactionQuests(faction, minReputation, playerLevelMin, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FactionQuestDetailed> getFactionQuest(String questId) {
        FactionQuestDetailed quest = narrativeService.getFactionQuest(questId);
        return ResponseEntity.ok(quest);
    }

    @Override
    public ResponseEntity<GetQuestBranches200Response> getQuestBranches(String questId) {
        GetQuestBranches200Response response = narrativeService.getQuestBranches(questId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetQuestEndings200Response> getQuestEndings(String questId) {
        GetQuestEndings200Response response = narrativeService.getQuestEndings(questId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetAvailableFactionQuests200Response> getAvailableFactionQuests(UUID characterId) {
        GetAvailableFactionQuests200Response response = narrativeService.getAvailableFactionQuests(characterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetFactionQuestProgress200Response> getFactionQuestProgress(UUID characterId) {
        GetFactionQuestProgress200Response response = narrativeService.getFactionQuestProgress(characterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> abandonQuest(UUID instanceId) {
        narrativeService.abandonQuest(instanceId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<DialogueChoiceResult> chooseDialogueOption(UUID instanceId, DialogueChoiceRequest dialogueChoiceRequest) {
        DialogueChoiceResult result = narrativeService.chooseDialogueOption(instanceId, dialogueChoiceRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<QuestCompletionResult> completeQuest(UUID instanceId, CompleteQuestRequest completeQuestRequest) {
        QuestCompletionResult result = narrativeService.completeQuest(instanceId, completeQuestRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<GetActiveQuests200Response> getActiveQuests(UUID characterId) {
        GetActiveQuests200Response response = narrativeService.getActiveQuests(characterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DialogueNode> getCurrentDialogue(UUID instanceId) {
        DialogueNode node = narrativeService.getCurrentDialogue(instanceId);
        return ResponseEntity.ok(node);
    }

    @Override
    public ResponseEntity<QuestInstance> getQuestInstance(UUID instanceId) {
        QuestInstance questInstance = narrativeService.getQuestInstance(instanceId);
        return ResponseEntity.ok(questInstance);
    }

    @Override
    public ResponseEntity<SkillCheckResult> performSkillCheck(UUID instanceId, SkillCheckRequest skillCheckRequest) {
        SkillCheckResult result = narrativeService.performSkillCheck(instanceId, skillCheckRequest);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<QuestInstance> startQuest(StartQuestRequest startQuestRequest) {
        QuestInstance questInstance = narrativeService.startQuest(startQuestRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(questInstance);
    }
}


