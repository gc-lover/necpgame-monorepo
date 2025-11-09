package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSelectInfo
 */


public class CharacterSelectInfo {

  private @Nullable UUID characterId;

  private @Nullable String name;

  private @Nullable Integer level;

  private @Nullable String propertyClass;

  private @Nullable String location;

  private @Nullable Integer playtimeHours;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastPlayed;

  private @Nullable String thumbnailUrl;

  public CharacterSelectInfo characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterSelectInfo name(@Nullable String name) {
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

  public CharacterSelectInfo level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public CharacterSelectInfo propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public CharacterSelectInfo location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public CharacterSelectInfo playtimeHours(@Nullable Integer playtimeHours) {
    this.playtimeHours = playtimeHours;
    return this;
  }

  /**
   * Get playtimeHours
   * @return playtimeHours
   */
  
  @Schema(name = "playtime_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playtime_hours")
  public @Nullable Integer getPlaytimeHours() {
    return playtimeHours;
  }

  public void setPlaytimeHours(@Nullable Integer playtimeHours) {
    this.playtimeHours = playtimeHours;
  }

  public CharacterSelectInfo lastPlayed(@Nullable OffsetDateTime lastPlayed) {
    this.lastPlayed = lastPlayed;
    return this;
  }

  /**
   * Get lastPlayed
   * @return lastPlayed
   */
  @Valid 
  @Schema(name = "last_played", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_played")
  public @Nullable OffsetDateTime getLastPlayed() {
    return lastPlayed;
  }

  public void setLastPlayed(@Nullable OffsetDateTime lastPlayed) {
    this.lastPlayed = lastPlayed;
  }

  public CharacterSelectInfo thumbnailUrl(@Nullable String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
    return this;
  }

  /**
   * Get thumbnailUrl
   * @return thumbnailUrl
   */
  
  @Schema(name = "thumbnail_url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thumbnail_url")
  public @Nullable String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(@Nullable String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSelectInfo characterSelectInfo = (CharacterSelectInfo) o;
    return Objects.equals(this.characterId, characterSelectInfo.characterId) &&
        Objects.equals(this.name, characterSelectInfo.name) &&
        Objects.equals(this.level, characterSelectInfo.level) &&
        Objects.equals(this.propertyClass, characterSelectInfo.propertyClass) &&
        Objects.equals(this.location, characterSelectInfo.location) &&
        Objects.equals(this.playtimeHours, characterSelectInfo.playtimeHours) &&
        Objects.equals(this.lastPlayed, characterSelectInfo.lastPlayed) &&
        Objects.equals(this.thumbnailUrl, characterSelectInfo.thumbnailUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, name, level, propertyClass, location, playtimeHours, lastPlayed, thumbnailUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSelectInfo {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    playtimeHours: ").append(toIndentedString(playtimeHours)).append("\n");
    sb.append("    lastPlayed: ").append(toIndentedString(lastPlayed)).append("\n");
    sb.append("    thumbnailUrl: ").append(toIndentedString(thumbnailUrl)).append("\n");
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

