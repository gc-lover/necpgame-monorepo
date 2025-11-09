package com.necpgame.backjava.service;

<<<<<<< HEAD
import com.necpgame.backjava.model.CreateCharacter201Response;
import com.necpgame.backjava.model.CreateCharacterRequest;
import com.necpgame.backjava.model.DeleteCharacter200Response;
import com.necpgame.backjava.model.GetCharacterCategories200Response;
=======
import com.necpgame.backjava.model.CharacterActivityListResponse;
import com.necpgame.backjava.model.CharacterAppearancePatch;
import com.necpgame.backjava.model.CharacterAppearanceResponse;
import com.necpgame.backjava.model.CharacterCreateRequest;
import com.necpgame.backjava.model.CharacterCreateResponse;
import com.necpgame.backjava.model.CharacterDeleteResponse;
import com.necpgame.backjava.model.CharacterListResponse;
import com.necpgame.backjava.model.CharacterRecalculateResponse;
import com.necpgame.backjava.model.CharacterRestoreExpiredResponse;
import com.necpgame.backjava.model.CharacterRestoreRequest;
import com.necpgame.backjava.model.CharacterRestoreResponse;
import com.necpgame.backjava.model.CharacterSlotPaymentRequiredResponse;
import com.necpgame.backjava.model.CharacterSlotPurchaseRequest;
import com.necpgame.backjava.model.CharacterSlotPurchaseResponse;
import com.necpgame.backjava.model.CharacterSlotStateResponse;
import com.necpgame.backjava.model.CharacterStatsRecalculateRequest;
import com.necpgame.backjava.model.CharacterSwitchLockedResponse;
import com.necpgame.backjava.model.CharacterSwitchRequest;
import com.necpgame.backjava.model.CharacterSwitchResponse;
import org.springframework.format.annotation.DateTimeFormat;
>>>>>>> a51ee69 (feat: implement player character lifecycle backend)
import com.necpgame.backjava.model.Error;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CharactersService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CharactersService {

    GetCharacterCategories200Response getCharacterCategories();

    /**
     * GET /characters/players/accounts/{accountId}/activity : List character activity
     * Returns paginated audit trail of character lifecycle operations including moderator interventions.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param activityType Filter activity log by type (optional)
     * @param dateFrom Activity start timestamp filter (UTC ISO-8601) (optional)
     * @param dateTo Activity end timestamp filter (UTC ISO-8601) (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return CharacterActivityListResponse
     */
    CharacterActivityListResponse charactersPlayersAccountsAccountIdActivityGet(UUID accountId, String activityType, OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer page, Integer pageSize);

    /**
     * PATCH /characters/players/accounts/{accountId}/characters/{characterId}/appearance : Patch character appearance
     * Applies partial updates to character appearance with validation against cosmetic catalogs and progression unlocks.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterId Character identifier from character-service (required)
     * @param characterAppearancePatch  (required)
     * @return CharacterAppearanceResponse
     */
    CharacterAppearanceResponse charactersPlayersAccountsAccountIdCharactersCharacterIdAppearancePatch(UUID accountId, UUID characterId, CharacterAppearancePatch characterAppearancePatch);

    /**
     * DELETE /characters/players/accounts/{accountId}/characters/{characterId} : Soft delete character
     * Marks character as deleted, releases slot and enqueues restore record while preserving inventory references.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterId Character identifier from character-service (required)
     * @return CharacterDeleteResponse
     */
    CharacterDeleteResponse charactersPlayersAccountsAccountIdCharactersCharacterIdDelete(UUID accountId, UUID characterId);

    /**
     * POST /characters/players/accounts/{accountId}/characters/{characterId}/recalculate : Recalculate derived stats
     * Forces recalculation of derived stats after equipment or skill changes and publishes stats update events.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterId Character identifier from character-service (required)
     * @param characterStatsRecalculateRequest  (required)
     * @return CharacterRecalculateResponse
     */
    CharacterRecalculateResponse charactersPlayersAccountsAccountIdCharactersCharacterIdRecalculatePost(UUID accountId, UUID characterId, CharacterStatsRecalculateRequest characterStatsRecalculateRequest);

    /**
     * POST /characters/players/accounts/{accountId}/characters/{characterId}/restore : Restore character
     * Restores a previously soft-deleted character within the grace period including inventory linkage revalidation.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterId Character identifier from character-service (required)
     * @param characterRestoreRequest  (optional)
     * @return CharacterRestoreResponse
     */
    CharacterRestoreResponse charactersPlayersAccountsAccountIdCharactersCharacterIdRestorePost(UUID accountId, UUID characterId, CharacterRestoreRequest characterRestoreRequest);

    /**
     * GET /characters/players/accounts/{accountId}/characters : List characters for account
     * Returns all characters for a player account including soft-deleted entries with restoration metadata.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param includeDeleted Whether to include soft-deleted characters (optional, default to false)
     * @param includeSnapshots Include latest snapshot references for each character (optional, default to false)
     * @return CharacterListResponse
     */
    CharacterListResponse charactersPlayersAccountsAccountIdCharactersGet(UUID accountId, Boolean includeDeleted, Boolean includeSnapshots);

    /**
     * POST /characters/players/accounts/{accountId}/characters : Create character
     * Creates a new character within available slots, provisioning starter inventory, quests and publishing lifecycle events.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterCreateRequest  (required)
     * @return CharacterCreateResponse
     */
    CharacterCreateResponse charactersPlayersAccountsAccountIdCharactersPost(UUID accountId, CharacterCreateRequest characterCreateRequest);

    /**
     * GET /characters/players/accounts/{accountId}/slots : Get slot state
     * Provides current slot allocation, premium purchases and requirements for next expansion tier.
     *
     * @param accountId Account identifier owning the characters (required)
     * @return CharacterSlotStateResponse
     */
    CharacterSlotStateResponse charactersPlayersAccountsAccountIdSlotsGet(UUID accountId);

    /**
     * POST /characters/players/accounts/{accountId}/slots/purchase : Purchase slot expansion
     * Initiates premium slot purchase through economy-service and updates slot limits upon transaction confirmation.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterSlotPurchaseRequest  (required)
     * @return CharacterSlotPurchaseResponse
     */
    CharacterSlotPurchaseResponse charactersPlayersAccountsAccountIdSlotsPurchasePost(UUID accountId, CharacterSlotPurchaseRequest characterSlotPurchaseRequest);

    /**
     * POST /characters/players/accounts/{accountId}/switch : Switch active character
     * Switches the active character tied to the session, persisting snapshots and invalidating combat sessions if required.
     *
     * @param accountId Account identifier owning the characters (required)
     * @param characterSwitchRequest  (required)
     * @return CharacterSwitchResponse
     */
    CharacterSwitchResponse charactersPlayersAccountsAccountIdSwitchPost(UUID accountId, CharacterSwitchRequest characterSwitchRequest);
}

