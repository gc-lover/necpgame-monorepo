package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.TextVersionStateAvailableActionsInner;
import com.necpgame.backjava.model.TextVersionStateCharacter;
import com.necpgame.backjava.model.TextVersionStateCurrentQuest;
import com.necpgame.backjava.model.TextVersionStateInventorySummary;
import com.necpgame.backjava.model.TextVersionStateNearbyNpcsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Упрощенное состояние для текстовой версии игры
 */

@Schema(name = "TextVersionState", description = "Упрощенное состояние для текстовой версии игры")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TextVersionState {

  private @Nullable TextVersionStateCharacter character;

  @Valid
  private List<@Valid TextVersionStateAvailableActionsInner> availableActions = new ArrayList<>();

  private JsonNullable<TextVersionStateCurrentQuest> currentQuest = JsonNullable.<TextVersionStateCurrentQuest>undefined();

  private @Nullable TextVersionStateInventorySummary inventorySummary;

  @Valid
  private List<@Valid TextVersionStateNearbyNpcsInner> nearbyNpcs = new ArrayList<>();

  public TextVersionState character(@Nullable TextVersionStateCharacter character) {
    this.character = character;
    return this;
  }

  /**
   * Get character
   * @return character
   */
  @Valid 
  @Schema(name = "character", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character")
  public @Nullable TextVersionStateCharacter getCharacter() {
    return character;
  }

  public void setCharacter(@Nullable TextVersionStateCharacter character) {
    this.character = character;
  }

  public TextVersionState availableActions(List<@Valid TextVersionStateAvailableActionsInner> availableActions) {
    this.availableActions = availableActions;
    return this;
  }

  public TextVersionState addAvailableActionsItem(TextVersionStateAvailableActionsInner availableActionsItem) {
    if (this.availableActions == null) {
      this.availableActions = new ArrayList<>();
    }
    this.availableActions.add(availableActionsItem);
    return this;
  }

  /**
   * Get availableActions
   * @return availableActions
   */
  @Valid 
  @Schema(name = "available_actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_actions")
  public List<@Valid TextVersionStateAvailableActionsInner> getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(List<@Valid TextVersionStateAvailableActionsInner> availableActions) {
    this.availableActions = availableActions;
  }

  public TextVersionState currentQuest(TextVersionStateCurrentQuest currentQuest) {
    this.currentQuest = JsonNullable.of(currentQuest);
    return this;
  }

  /**
   * Get currentQuest
   * @return currentQuest
   */
  @Valid 
  @Schema(name = "current_quest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_quest")
  public JsonNullable<TextVersionStateCurrentQuest> getCurrentQuest() {
    return currentQuest;
  }

  public void setCurrentQuest(JsonNullable<TextVersionStateCurrentQuest> currentQuest) {
    this.currentQuest = currentQuest;
  }

  public TextVersionState inventorySummary(@Nullable TextVersionStateInventorySummary inventorySummary) {
    this.inventorySummary = inventorySummary;
    return this;
  }

  /**
   * Get inventorySummary
   * @return inventorySummary
   */
  @Valid 
  @Schema(name = "inventory_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inventory_summary")
  public @Nullable TextVersionStateInventorySummary getInventorySummary() {
    return inventorySummary;
  }

  public void setInventorySummary(@Nullable TextVersionStateInventorySummary inventorySummary) {
    this.inventorySummary = inventorySummary;
  }

  public TextVersionState nearbyNpcs(List<@Valid TextVersionStateNearbyNpcsInner> nearbyNpcs) {
    this.nearbyNpcs = nearbyNpcs;
    return this;
  }

  public TextVersionState addNearbyNpcsItem(TextVersionStateNearbyNpcsInner nearbyNpcsItem) {
    if (this.nearbyNpcs == null) {
      this.nearbyNpcs = new ArrayList<>();
    }
    this.nearbyNpcs.add(nearbyNpcsItem);
    return this;
  }

  /**
   * Get nearbyNpcs
   * @return nearbyNpcs
   */
  @Valid 
  @Schema(name = "nearby_npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nearby_npcs")
  public List<@Valid TextVersionStateNearbyNpcsInner> getNearbyNpcs() {
    return nearbyNpcs;
  }

  public void setNearbyNpcs(List<@Valid TextVersionStateNearbyNpcsInner> nearbyNpcs) {
    this.nearbyNpcs = nearbyNpcs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextVersionState textVersionState = (TextVersionState) o;
    return Objects.equals(this.character, textVersionState.character) &&
        Objects.equals(this.availableActions, textVersionState.availableActions) &&
        equalsNullable(this.currentQuest, textVersionState.currentQuest) &&
        Objects.equals(this.inventorySummary, textVersionState.inventorySummary) &&
        Objects.equals(this.nearbyNpcs, textVersionState.nearbyNpcs);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(character, availableActions, hashCodeNullable(currentQuest), inventorySummary, nearbyNpcs);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextVersionState {\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
    sb.append("    availableActions: ").append(toIndentedString(availableActions)).append("\n");
    sb.append("    currentQuest: ").append(toIndentedString(currentQuest)).append("\n");
    sb.append("    inventorySummary: ").append(toIndentedString(inventorySummary)).append("\n");
    sb.append("    nearbyNpcs: ").append(toIndentedString(nearbyNpcs)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

