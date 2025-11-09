package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GameAction;
import com.necpgame.gameplayservice.model.GameLocation;
import com.necpgame.gameplayservice.model.GameNPC;
import com.necpgame.gameplayservice.model.GameQuest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InitialStateResponse
 */


public class InitialStateResponse {

  private GameLocation location;

  @Valid
  private List<@Valid GameNPC> availableNPCs = new ArrayList<>();

  private GameQuest firstQuest;

  @Valid
  private List<@Valid GameAction> availableActions = new ArrayList<>();

  public InitialStateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InitialStateResponse(GameLocation location, List<@Valid GameNPC> availableNPCs, GameQuest firstQuest, List<@Valid GameAction> availableActions) {
    this.location = location;
    this.availableNPCs = availableNPCs;
    this.firstQuest = firstQuest;
    this.availableActions = availableActions;
  }

  public InitialStateResponse location(GameLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @NotNull @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location")
  public GameLocation getLocation() {
    return location;
  }

  public void setLocation(GameLocation location) {
    this.location = location;
  }

  public InitialStateResponse availableNPCs(List<@Valid GameNPC> availableNPCs) {
    this.availableNPCs = availableNPCs;
    return this;
  }

  public InitialStateResponse addAvailableNPCsItem(GameNPC availableNPCsItem) {
    if (this.availableNPCs == null) {
      this.availableNPCs = new ArrayList<>();
    }
    this.availableNPCs.add(availableNPCsItem);
    return this;
  }

  /**
   * Список доступных NPC в текущей локации
   * @return availableNPCs
   */
  @NotNull @Valid 
  @Schema(name = "availableNPCs", description = "Список доступных NPC в текущей локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availableNPCs")
  public List<@Valid GameNPC> getAvailableNPCs() {
    return availableNPCs;
  }

  public void setAvailableNPCs(List<@Valid GameNPC> availableNPCs) {
    this.availableNPCs = availableNPCs;
  }

  public InitialStateResponse firstQuest(GameQuest firstQuest) {
    this.firstQuest = firstQuest;
    return this;
  }

  /**
   * Get firstQuest
   * @return firstQuest
   */
  @NotNull @Valid 
  @Schema(name = "firstQuest", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("firstQuest")
  public GameQuest getFirstQuest() {
    return firstQuest;
  }

  public void setFirstQuest(GameQuest firstQuest) {
    this.firstQuest = firstQuest;
  }

  public InitialStateResponse availableActions(List<@Valid GameAction> availableActions) {
    this.availableActions = availableActions;
    return this;
  }

  public InitialStateResponse addAvailableActionsItem(GameAction availableActionsItem) {
    if (this.availableActions == null) {
      this.availableActions = new ArrayList<>();
    }
    this.availableActions.add(availableActionsItem);
    return this;
  }

  /**
   * Список доступных действий в локации
   * @return availableActions
   */
  @NotNull @Valid 
  @Schema(name = "availableActions", description = "Список доступных действий в локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("availableActions")
  public List<@Valid GameAction> getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(List<@Valid GameAction> availableActions) {
    this.availableActions = availableActions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitialStateResponse initialStateResponse = (InitialStateResponse) o;
    return Objects.equals(this.location, initialStateResponse.location) &&
        Objects.equals(this.availableNPCs, initialStateResponse.availableNPCs) &&
        Objects.equals(this.firstQuest, initialStateResponse.firstQuest) &&
        Objects.equals(this.availableActions, initialStateResponse.availableActions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, availableNPCs, firstQuest, availableActions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitialStateResponse {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    availableNPCs: ").append(toIndentedString(availableNPCs)).append("\n");
    sb.append("    firstQuest: ").append(toIndentedString(firstQuest)).append("\n");
    sb.append("    availableActions: ").append(toIndentedString(availableActions)).append("\n");
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

