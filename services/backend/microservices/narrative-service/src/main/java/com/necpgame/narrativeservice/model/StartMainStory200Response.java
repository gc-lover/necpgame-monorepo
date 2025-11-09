package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StartMainStory200Response
 */

@JsonTypeName("startMainStory_200_response")

public class StartMainStory200Response {

  private @Nullable Boolean success;

  private @Nullable String characterId;

  private @Nullable String lifePath;

  private @Nullable String prologueQuestId;

  private @Nullable String startingLocation;

  public StartMainStory200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public StartMainStory200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public StartMainStory200Response lifePath(@Nullable String lifePath) {
    this.lifePath = lifePath;
    return this;
  }

  /**
   * Get lifePath
   * @return lifePath
   */
  
  @Schema(name = "life_path", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("life_path")
  public @Nullable String getLifePath() {
    return lifePath;
  }

  public void setLifePath(@Nullable String lifePath) {
    this.lifePath = lifePath;
  }

  public StartMainStory200Response prologueQuestId(@Nullable String prologueQuestId) {
    this.prologueQuestId = prologueQuestId;
    return this;
  }

  /**
   * Get prologueQuestId
   * @return prologueQuestId
   */
  
  @Schema(name = "prologue_quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prologue_quest_id")
  public @Nullable String getPrologueQuestId() {
    return prologueQuestId;
  }

  public void setPrologueQuestId(@Nullable String prologueQuestId) {
    this.prologueQuestId = prologueQuestId;
  }

  public StartMainStory200Response startingLocation(@Nullable String startingLocation) {
    this.startingLocation = startingLocation;
    return this;
  }

  /**
   * Get startingLocation
   * @return startingLocation
   */
  
  @Schema(name = "starting_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_location")
  public @Nullable String getStartingLocation() {
    return startingLocation;
  }

  public void setStartingLocation(@Nullable String startingLocation) {
    this.startingLocation = startingLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartMainStory200Response startMainStory200Response = (StartMainStory200Response) o;
    return Objects.equals(this.success, startMainStory200Response.success) &&
        Objects.equals(this.characterId, startMainStory200Response.characterId) &&
        Objects.equals(this.lifePath, startMainStory200Response.lifePath) &&
        Objects.equals(this.prologueQuestId, startMainStory200Response.prologueQuestId) &&
        Objects.equals(this.startingLocation, startMainStory200Response.startingLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, characterId, lifePath, prologueQuestId, startingLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartMainStory200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lifePath: ").append(toIndentedString(lifePath)).append("\n");
    sb.append("    prologueQuestId: ").append(toIndentedString(prologueQuestId)).append("\n");
    sb.append("    startingLocation: ").append(toIndentedString(startingLocation)).append("\n");
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

