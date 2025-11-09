package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.model.CharacterSummaryCurrentLocation;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * CharacterSummary
 */


public class CharacterSummary {

  private UUID characterId;

  private String name;

  /**
   * Gets or Sets origin
   */
  public enum OriginEnum {
    CORPO("CORPO"),
    
    STREETKID("STREETKID"),
    
    NOMAD("NOMAD"),
    
    CUSTOM("CUSTOM");

    private final String value;

    OriginEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OriginEnum fromValue(String value) {
      for (OriginEnum b : OriginEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OriginEnum origin;

  /**
   * Gets or Sets characterClass
   */
  public enum CharacterClassEnum {
    SOLO("SOLO"),
    
    NETRUNNER("NETRUNNER"),
    
    TECHIE("TECHIE"),
    
    FIXER("FIXER"),
    
    NOMAD("NOMAD");

    private final String value;

    CharacterClassEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CharacterClassEnum fromValue(String value) {
      for (CharacterClassEnum b : CharacterClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CharacterClassEnum characterClass;

  private Integer level;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    IN_COMBAT("IN_COMBAT"),
    
    AFK("AFK"),
    
    DEAD("DEAD"),
    
    DELETED("DELETED");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable CharacterSummaryCurrentLocation currentLocation;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastActiveAt;

  private Boolean deleted;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> deletedAt = JsonNullable.<OffsetDateTime>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> canRestoreUntil = JsonNullable.<OffsetDateTime>undefined();

  private CharacterSlotState slotState;

  @Valid
  private List<String> tags = new ArrayList<>();

  public CharacterSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSummary(UUID characterId, String name, OriginEnum origin, CharacterClassEnum characterClass, Integer level, StatusEnum status, OffsetDateTime lastActiveAt, Boolean deleted, OffsetDateTime canRestoreUntil, CharacterSlotState slotState) {
    this.characterId = characterId;
    this.name = name;
    this.origin = origin;
    this.characterClass = characterClass;
    this.level = level;
    this.status = status;
    this.lastActiveAt = lastActiveAt;
    this.deleted = deleted;
    this.canRestoreUntil = JsonNullable.of(canRestoreUntil);
    this.slotState = slotState;
  }

  public CharacterSummary characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterSummary name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(max = 50) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CharacterSummary origin(OriginEnum origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public OriginEnum getOrigin() {
    return origin;
  }

  public void setOrigin(OriginEnum origin) {
    this.origin = origin;
  }

  public CharacterSummary characterClass(CharacterClassEnum characterClass) {
    this.characterClass = characterClass;
    return this;
  }

  /**
   * Get characterClass
   * @return characterClass
   */
  @NotNull 
  @Schema(name = "characterClass", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterClass")
  public CharacterClassEnum getCharacterClass() {
    return characterClass;
  }

  public void setCharacterClass(CharacterClassEnum characterClass) {
    this.characterClass = characterClass;
  }

  public CharacterSummary level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public CharacterSummary status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public CharacterSummary currentLocation(@Nullable CharacterSummaryCurrentLocation currentLocation) {
    this.currentLocation = currentLocation;
    return this;
  }

  /**
   * Get currentLocation
   * @return currentLocation
   */
  @Valid 
  @Schema(name = "currentLocation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentLocation")
  public @Nullable CharacterSummaryCurrentLocation getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(@Nullable CharacterSummaryCurrentLocation currentLocation) {
    this.currentLocation = currentLocation;
  }

  public CharacterSummary lastActiveAt(OffsetDateTime lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
    return this;
  }

  /**
   * Get lastActiveAt
   * @return lastActiveAt
   */
  @NotNull @Valid 
  @Schema(name = "lastActiveAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastActiveAt")
  public OffsetDateTime getLastActiveAt() {
    return lastActiveAt;
  }

  public void setLastActiveAt(OffsetDateTime lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

  public CharacterSummary deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

  /**
   * Get deleted
   * @return deleted
   */
  @NotNull 
  @Schema(name = "deleted", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deleted")
  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public CharacterSummary deletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = JsonNullable.of(deletedAt);
    return this;
  }

  /**
   * Get deletedAt
   * @return deletedAt
   */
  @Valid 
  @Schema(name = "deletedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deletedAt")
  public JsonNullable<OffsetDateTime> getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(JsonNullable<OffsetDateTime> deletedAt) {
    this.deletedAt = deletedAt;
  }

  public CharacterSummary canRestoreUntil(OffsetDateTime canRestoreUntil) {
    this.canRestoreUntil = JsonNullable.of(canRestoreUntil);
    return this;
  }

  /**
   * Get canRestoreUntil
   * @return canRestoreUntil
   */
  @NotNull @Valid 
  @Schema(name = "canRestoreUntil", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("canRestoreUntil")
  public JsonNullable<OffsetDateTime> getCanRestoreUntil() {
    return canRestoreUntil;
  }

  public void setCanRestoreUntil(JsonNullable<OffsetDateTime> canRestoreUntil) {
    this.canRestoreUntil = canRestoreUntil;
  }

  public CharacterSummary slotState(CharacterSlotState slotState) {
    this.slotState = slotState;
    return this;
  }

  /**
   * Get slotState
   * @return slotState
   */
  @NotNull @Valid 
  @Schema(name = "slotState", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slotState")
  public CharacterSlotState getSlotState() {
    return slotState;
  }

  public void setSlotState(CharacterSlotState slotState) {
    this.slotState = slotState;
  }

  public CharacterSummary tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public CharacterSummary addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Flags for UI (starter, hardcore, permadeath)
   * @return tags
   */
  
  @Schema(name = "tags", description = "Flags for UI (starter, hardcore, permadeath)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSummary characterSummary = (CharacterSummary) o;
    return Objects.equals(this.characterId, characterSummary.characterId) &&
        Objects.equals(this.name, characterSummary.name) &&
        Objects.equals(this.origin, characterSummary.origin) &&
        Objects.equals(this.characterClass, characterSummary.characterClass) &&
        Objects.equals(this.level, characterSummary.level) &&
        Objects.equals(this.status, characterSummary.status) &&
        Objects.equals(this.currentLocation, characterSummary.currentLocation) &&
        Objects.equals(this.lastActiveAt, characterSummary.lastActiveAt) &&
        Objects.equals(this.deleted, characterSummary.deleted) &&
        equalsNullable(this.deletedAt, characterSummary.deletedAt) &&
        Objects.equals(this.canRestoreUntil, characterSummary.canRestoreUntil) &&
        Objects.equals(this.slotState, characterSummary.slotState) &&
        Objects.equals(this.tags, characterSummary.tags);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, name, origin, characterClass, level, status, currentLocation, lastActiveAt, deleted, hashCodeNullable(deletedAt), canRestoreUntil, slotState, tags);
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
    sb.append("class CharacterSummary {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    characterClass: ").append(toIndentedString(characterClass)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    lastActiveAt: ").append(toIndentedString(lastActiveAt)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    deletedAt: ").append(toIndentedString(deletedAt)).append("\n");
    sb.append("    canRestoreUntil: ").append(toIndentedString(canRestoreUntil)).append("\n");
    sb.append("    slotState: ").append(toIndentedString(slotState)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

