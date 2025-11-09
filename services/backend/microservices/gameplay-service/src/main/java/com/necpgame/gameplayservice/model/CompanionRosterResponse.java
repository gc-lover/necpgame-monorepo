package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CompanionDetail;
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
 * CompanionRosterResponse
 */


public class CompanionRosterResponse {

  private @Nullable String playerId;

  private @Nullable Integer limitActive;

  @Valid
  private List<@Valid CompanionDetail> companions = new ArrayList<>();

  public CompanionRosterResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public CompanionRosterResponse limitActive(@Nullable Integer limitActive) {
    this.limitActive = limitActive;
    return this;
  }

  /**
   * Get limitActive
   * @return limitActive
   */
  
  @Schema(name = "limitActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limitActive")
  public @Nullable Integer getLimitActive() {
    return limitActive;
  }

  public void setLimitActive(@Nullable Integer limitActive) {
    this.limitActive = limitActive;
  }

  public CompanionRosterResponse companions(List<@Valid CompanionDetail> companions) {
    this.companions = companions;
    return this;
  }

  public CompanionRosterResponse addCompanionsItem(CompanionDetail companionsItem) {
    if (this.companions == null) {
      this.companions = new ArrayList<>();
    }
    this.companions.add(companionsItem);
    return this;
  }

  /**
   * Get companions
   * @return companions
   */
  @Valid 
  @Schema(name = "companions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("companions")
  public List<@Valid CompanionDetail> getCompanions() {
    return companions;
  }

  public void setCompanions(List<@Valid CompanionDetail> companions) {
    this.companions = companions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionRosterResponse companionRosterResponse = (CompanionRosterResponse) o;
    return Objects.equals(this.playerId, companionRosterResponse.playerId) &&
        Objects.equals(this.limitActive, companionRosterResponse.limitActive) &&
        Objects.equals(this.companions, companionRosterResponse.companions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, limitActive, companions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionRosterResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    limitActive: ").append(toIndentedString(limitActive)).append("\n");
    sb.append("    companions: ").append(toIndentedString(companions)).append("\n");
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

