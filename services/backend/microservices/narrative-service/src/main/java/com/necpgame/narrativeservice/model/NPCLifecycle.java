package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.NPCLifecycleLifecycleStagesInner;
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
 * NPCLifecycle
 */


public class NPCLifecycle {

  private @Nullable String npcId;

  private @Nullable String name;

  @Valid
  private List<@Valid NPCLifecycleLifecycleStagesInner> lifecycleStages = new ArrayList<>();

  private @Nullable String currentStatus;

  private @Nullable Integer playerInteractionsCount;

  public NPCLifecycle npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public NPCLifecycle name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public NPCLifecycle lifecycleStages(List<@Valid NPCLifecycleLifecycleStagesInner> lifecycleStages) {
    this.lifecycleStages = lifecycleStages;
    return this;
  }

  public NPCLifecycle addLifecycleStagesItem(NPCLifecycleLifecycleStagesInner lifecycleStagesItem) {
    if (this.lifecycleStages == null) {
      this.lifecycleStages = new ArrayList<>();
    }
    this.lifecycleStages.add(lifecycleStagesItem);
    return this;
  }

  /**
   * Get lifecycleStages
   * @return lifecycleStages
   */
  @Valid 
  @Schema(name = "lifecycle_stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lifecycle_stages")
  public List<@Valid NPCLifecycleLifecycleStagesInner> getLifecycleStages() {
    return lifecycleStages;
  }

  public void setLifecycleStages(List<@Valid NPCLifecycleLifecycleStagesInner> lifecycleStages) {
    this.lifecycleStages = lifecycleStages;
  }

  public NPCLifecycle currentStatus(@Nullable String currentStatus) {
    this.currentStatus = currentStatus;
    return this;
  }

  /**
   * Get currentStatus
   * @return currentStatus
   */
  
  @Schema(name = "current_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_status")
  public @Nullable String getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(@Nullable String currentStatus) {
    this.currentStatus = currentStatus;
  }

  public NPCLifecycle playerInteractionsCount(@Nullable Integer playerInteractionsCount) {
    this.playerInteractionsCount = playerInteractionsCount;
    return this;
  }

  /**
   * Get playerInteractionsCount
   * @return playerInteractionsCount
   */
  
  @Schema(name = "player_interactions_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_interactions_count")
  public @Nullable Integer getPlayerInteractionsCount() {
    return playerInteractionsCount;
  }

  public void setPlayerInteractionsCount(@Nullable Integer playerInteractionsCount) {
    this.playerInteractionsCount = playerInteractionsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCLifecycle npCLifecycle = (NPCLifecycle) o;
    return Objects.equals(this.npcId, npCLifecycle.npcId) &&
        Objects.equals(this.name, npCLifecycle.name) &&
        Objects.equals(this.lifecycleStages, npCLifecycle.lifecycleStages) &&
        Objects.equals(this.currentStatus, npCLifecycle.currentStatus) &&
        Objects.equals(this.playerInteractionsCount, npCLifecycle.playerInteractionsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, lifecycleStages, currentStatus, playerInteractionsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NPCLifecycle {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lifecycleStages: ").append(toIndentedString(lifecycleStages)).append("\n");
    sb.append("    currentStatus: ").append(toIndentedString(currentStatus)).append("\n");
    sb.append("    playerInteractionsCount: ").append(toIndentedString(playerInteractionsCount)).append("\n");
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

