package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CyberspaceAvatar
 */


public class CyberspaceAvatar {

  private @Nullable String avatarId;

  private @Nullable String characterId;

  private @Nullable String currentZone;

  private @Nullable String accessLevel;

  private @Nullable Integer cyberdeckRating;

  @Valid
  private List<String> icePrograms = new ArrayList<>();

  private @Nullable Integer reputation;

  public CyberspaceAvatar avatarId(@Nullable String avatarId) {
    this.avatarId = avatarId;
    return this;
  }

  /**
   * Get avatarId
   * @return avatarId
   */
  
  @Schema(name = "avatar_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avatar_id")
  public @Nullable String getAvatarId() {
    return avatarId;
  }

  public void setAvatarId(@Nullable String avatarId) {
    this.avatarId = avatarId;
  }

  public CyberspaceAvatar characterId(@Nullable String characterId) {
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

  public CyberspaceAvatar currentZone(@Nullable String currentZone) {
    this.currentZone = currentZone;
    return this;
  }

  /**
   * Get currentZone
   * @return currentZone
   */
  
  @Schema(name = "current_zone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_zone")
  public @Nullable String getCurrentZone() {
    return currentZone;
  }

  public void setCurrentZone(@Nullable String currentZone) {
    this.currentZone = currentZone;
  }

  public CyberspaceAvatar accessLevel(@Nullable String accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "access_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_level")
  public @Nullable String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(@Nullable String accessLevel) {
    this.accessLevel = accessLevel;
  }

  public CyberspaceAvatar cyberdeckRating(@Nullable Integer cyberdeckRating) {
    this.cyberdeckRating = cyberdeckRating;
    return this;
  }

  /**
   * Get cyberdeckRating
   * @return cyberdeckRating
   */
  
  @Schema(name = "cyberdeck_rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberdeck_rating")
  public @Nullable Integer getCyberdeckRating() {
    return cyberdeckRating;
  }

  public void setCyberdeckRating(@Nullable Integer cyberdeckRating) {
    this.cyberdeckRating = cyberdeckRating;
  }

  public CyberspaceAvatar icePrograms(List<String> icePrograms) {
    this.icePrograms = icePrograms;
    return this;
  }

  public CyberspaceAvatar addIceProgramsItem(String iceProgramsItem) {
    if (this.icePrograms == null) {
      this.icePrograms = new ArrayList<>();
    }
    this.icePrograms.add(iceProgramsItem);
    return this;
  }

  /**
   * Get icePrograms
   * @return icePrograms
   */
  
  @Schema(name = "ice_programs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ice_programs")
  public List<String> getIcePrograms() {
    return icePrograms;
  }

  public void setIcePrograms(List<String> icePrograms) {
    this.icePrograms = icePrograms;
  }

  public CyberspaceAvatar reputation(@Nullable Integer reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Integer getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Integer reputation) {
    this.reputation = reputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberspaceAvatar cyberspaceAvatar = (CyberspaceAvatar) o;
    return Objects.equals(this.avatarId, cyberspaceAvatar.avatarId) &&
        Objects.equals(this.characterId, cyberspaceAvatar.characterId) &&
        Objects.equals(this.currentZone, cyberspaceAvatar.currentZone) &&
        Objects.equals(this.accessLevel, cyberspaceAvatar.accessLevel) &&
        Objects.equals(this.cyberdeckRating, cyberspaceAvatar.cyberdeckRating) &&
        Objects.equals(this.icePrograms, cyberspaceAvatar.icePrograms) &&
        Objects.equals(this.reputation, cyberspaceAvatar.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(avatarId, characterId, currentZone, accessLevel, cyberdeckRating, icePrograms, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberspaceAvatar {\n");
    sb.append("    avatarId: ").append(toIndentedString(avatarId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentZone: ").append(toIndentedString(currentZone)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
    sb.append("    cyberdeckRating: ").append(toIndentedString(cyberdeckRating)).append("\n");
    sb.append("    icePrograms: ").append(toIndentedString(icePrograms)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
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

