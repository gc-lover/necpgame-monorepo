package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootGenerationContextLuckModifiers;
import com.necpgame.lootservice.model.LootParticipant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LootGenerationContext
 */


public class LootGenerationContext {

  /**
   * Gets or Sets sourceType
   */
  public enum SourceTypeEnum {
    NPC("NPC"),
    
    CONTAINER("CONTAINER"),
    
    EVENT("EVENT"),
    
    QUEST("QUEST"),
    
    RAID("RAID"),
    
    WORLD_BOSS("WORLD_BOSS");

    private final String value;

    SourceTypeEnum(String value) {
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
    public static SourceTypeEnum fromValue(String value) {
      for (SourceTypeEnum b : SourceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceTypeEnum sourceType;

  private @Nullable String zoneId;

  /**
   * Gets or Sets difficultyTier
   */
  public enum DifficultyTierEnum {
    STORY("STORY"),
    
    NORMAL("NORMAL"),
    
    HARD("HARD"),
    
    NIGHTMARE("NIGHTMARE"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    DifficultyTierEnum(String value) {
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
    public static DifficultyTierEnum fromValue(String value) {
      for (DifficultyTierEnum b : DifficultyTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyTierEnum difficultyTier;

  @Valid
  private List<@Valid LootParticipant> participants = new ArrayList<>();

  private @Nullable LootGenerationContextLuckModifiers luckModifiers;

  @Valid
  private List<String> eventFlags = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime killTimestamp;

  private @Nullable String seed;

  public LootGenerationContext() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootGenerationContext(SourceTypeEnum sourceType, List<@Valid LootParticipant> participants) {
    this.sourceType = sourceType;
    this.participants = participants;
  }

  public LootGenerationContext sourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get sourceType
   * @return sourceType
   */
  @NotNull 
  @Schema(name = "sourceType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sourceType")
  public SourceTypeEnum getSourceType() {
    return sourceType;
  }

  public void setSourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
  }

  public LootGenerationContext zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneId")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public LootGenerationContext difficultyTier(@Nullable DifficultyTierEnum difficultyTier) {
    this.difficultyTier = difficultyTier;
    return this;
  }

  /**
   * Get difficultyTier
   * @return difficultyTier
   */
  
  @Schema(name = "difficultyTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficultyTier")
  public @Nullable DifficultyTierEnum getDifficultyTier() {
    return difficultyTier;
  }

  public void setDifficultyTier(@Nullable DifficultyTierEnum difficultyTier) {
    this.difficultyTier = difficultyTier;
  }

  public LootGenerationContext participants(List<@Valid LootParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public LootGenerationContext addParticipantsItem(LootParticipant participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @NotNull @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participants")
  public List<@Valid LootParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid LootParticipant> participants) {
    this.participants = participants;
  }

  public LootGenerationContext luckModifiers(@Nullable LootGenerationContextLuckModifiers luckModifiers) {
    this.luckModifiers = luckModifiers;
    return this;
  }

  /**
   * Get luckModifiers
   * @return luckModifiers
   */
  @Valid 
  @Schema(name = "luckModifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("luckModifiers")
  public @Nullable LootGenerationContextLuckModifiers getLuckModifiers() {
    return luckModifiers;
  }

  public void setLuckModifiers(@Nullable LootGenerationContextLuckModifiers luckModifiers) {
    this.luckModifiers = luckModifiers;
  }

  public LootGenerationContext eventFlags(List<String> eventFlags) {
    this.eventFlags = eventFlags;
    return this;
  }

  public LootGenerationContext addEventFlagsItem(String eventFlagsItem) {
    if (this.eventFlags == null) {
      this.eventFlags = new ArrayList<>();
    }
    this.eventFlags.add(eventFlagsItem);
    return this;
  }

  /**
   * Get eventFlags
   * @return eventFlags
   */
  
  @Schema(name = "eventFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventFlags")
  public List<String> getEventFlags() {
    return eventFlags;
  }

  public void setEventFlags(List<String> eventFlags) {
    this.eventFlags = eventFlags;
  }

  public LootGenerationContext killTimestamp(@Nullable OffsetDateTime killTimestamp) {
    this.killTimestamp = killTimestamp;
    return this;
  }

  /**
   * Get killTimestamp
   * @return killTimestamp
   */
  @Valid 
  @Schema(name = "killTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("killTimestamp")
  public @Nullable OffsetDateTime getKillTimestamp() {
    return killTimestamp;
  }

  public void setKillTimestamp(@Nullable OffsetDateTime killTimestamp) {
    this.killTimestamp = killTimestamp;
  }

  public LootGenerationContext seed(@Nullable String seed) {
    this.seed = seed;
    return this;
  }

  /**
   * Get seed
   * @return seed
   */
  
  @Schema(name = "seed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seed")
  public @Nullable String getSeed() {
    return seed;
  }

  public void setSeed(@Nullable String seed) {
    this.seed = seed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootGenerationContext lootGenerationContext = (LootGenerationContext) o;
    return Objects.equals(this.sourceType, lootGenerationContext.sourceType) &&
        Objects.equals(this.zoneId, lootGenerationContext.zoneId) &&
        Objects.equals(this.difficultyTier, lootGenerationContext.difficultyTier) &&
        Objects.equals(this.participants, lootGenerationContext.participants) &&
        Objects.equals(this.luckModifiers, lootGenerationContext.luckModifiers) &&
        Objects.equals(this.eventFlags, lootGenerationContext.eventFlags) &&
        Objects.equals(this.killTimestamp, lootGenerationContext.killTimestamp) &&
        Objects.equals(this.seed, lootGenerationContext.seed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, zoneId, difficultyTier, participants, luckModifiers, eventFlags, killTimestamp, seed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootGenerationContext {\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    difficultyTier: ").append(toIndentedString(difficultyTier)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    luckModifiers: ").append(toIndentedString(luckModifiers)).append("\n");
    sb.append("    eventFlags: ").append(toIndentedString(eventFlags)).append("\n");
    sb.append("    killTimestamp: ").append(toIndentedString(killTimestamp)).append("\n");
    sb.append("    seed: ").append(toIndentedString(seed)).append("\n");
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

