package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetCharacterCategories200Response;
import com.necpgame.backjava.model.GetCharacterCodex200Response;
import com.necpgame.backjava.model.GetTimeline200Response;
import com.necpgame.backjava.model.ListFactions200Response;
import com.necpgame.backjava.model.SearchLore200Response;
import com.necpgame.backjava.model.UniverseLore;
import com.necpgame.backjava.model.UnlockCodexEntryRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LoreReferenceServiceIntegrationTest {

    @Autowired
    private UniverseService universeService;

    @Autowired
    private FactionsService factionsService;

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private CharactersService charactersService;

    @Test
    void shouldReturnUniverseLore() {
        UniverseLore lore = universeService.getUniverseLore();
        assertThat(lore.getTitle()).isEqualTo("Cyberpunk Universe Overview");
        assertThat(lore.getMajorFactionsCount()).isEqualTo(84);
    }

    @Test
    void shouldFilterTimelineByEventType() {
        GetTimeline200Response timeline = universeService.getTimeline(null, "WAR");
        assertThat(timeline.getEvents()).hasSize(1);
        assertThat(timeline.getEvents().get(0).getName()).contains("Corporate War");
    }

    @Test
    void shouldSearchLoreAcrossFactions() {
        SearchLore200Response response = universeService.searchLore("Arasaka", "FACTIONS");
        assertThat(response.getResults()).isNotEmpty();
        assertThat(response.getResults().get(0).getResultType().getValue()).isEqualTo("FACTION");
    }

    @Test
    void shouldUnlockCodexEntry() {
        UUID characterId = UUID.randomUUID();
        GetCharacterCodex200Response initial = universeService.getCharacterCodex(characterId);
        BigDecimal initialCompletion = initial.getCompletionPercentage();

        UnlockCodexEntryRequest request = new UnlockCodexEntryRequest()
                .characterId(characterId)
                .entryId("codex-relic-overview");
        universeService.unlockCodexEntry(request);

        GetCharacterCodex200Response updated = universeService.getCharacterCodex(characterId);
        assertThat(updated.getCompletionPercentage()).isGreaterThan(initialCompletion);
        assertThat(updated.getEntries())
                .anyMatch(entry -> entry.getEntryId().equals("codex-relic-overview") && entry.getUnlocked());
    }

    @Test
    void shouldListFactionsByType() {
        ListFactions200Response response = factionsService.listFactions("CORPORATION", null, 1, 10);
        assertThat(response.getData()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(response.getData()).allMatch(faction -> "CORPORATION".equals(faction.getType().getValue()));
    }

    @Test
    void shouldReturnLocationDetails() {
        var location = locationsService.getLocation("location-night-city");
        assertThat(location.getName()).isEqualTo("Night City");
        assertThat(location.getDistricts()).isNotEmpty();
    }

    @Test
    void shouldReturnCharacterCategories() {
        GetCharacterCategories200Response categories = charactersService.getCharacterCategories();
        assertThat(categories.getCategories()).hasSize(3);
    }
}

