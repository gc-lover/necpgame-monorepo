package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * EntryRequirement
 */


public class EntryRequirement {

  private @Nullable Integer minimumLevel;

  private @Nullable Integer reputationThreshold;

  @Valid
  private List<UUID> prerequisiteChains = new ArrayList<>();

  @Valid
  private List<String> worldFlagsRequired = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownExpiresAt;

  private @Nullable Integer guildStandingRequired;

  public EntryRequirement minimumLevel(@Nullable Integer minimumLevel) {
    this.minimumLevel = minimumLevel;
    return this;
  }

  /**
   * Get minimumLevel
   * minimum: 1
   * maximum: 100
   * @return minimumLevel
   */
  @Min(value = 1) @Max(value = 100) 
  @Schema(name = "minimumLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minimumLevel")
  public @Nullable Integer getMinimumLevel() {
    return minimumLevel;
  }

  public void setMinimumLevel(@Nullable Integer minimumLevel) {
    this.minimumLevel = minimumLevel;
  }

  public EntryRequirement reputationThreshold(@Nullable Integer reputationThreshold) {
    this.reputationThreshold = reputationThreshold;
    return this;
  }

  /**
   * Get reputationThreshold
   * minimum: -1000
   * maximum: 1000
   * @return reputationThreshold
   */
  @Min(value = -1000) @Max(value = 1000) 
  @Schema(name = "reputationThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationThreshold")
  public @Nullable Integer getReputationThreshold() {
    return reputationThreshold;
  }

  public void setReputationThreshold(@Nullable Integer reputationThreshold) {
    this.reputationThreshold = reputationThreshold;
  }

  public EntryRequirement prerequisiteChains(List<UUID> prerequisiteChains) {
    this.prerequisiteChains = prerequisiteChains;
    return this;
  }

  public EntryRequirement addPrerequisiteChainsItem(UUID prerequisiteChainsItem) {
    if (this.prerequisiteChains == null) {
      this.prerequisiteChains = new ArrayList<>();
    }
    this.prerequisiteChains.add(prerequisiteChainsItem);
    return this;
  }

  /**
   * Get prerequisiteChains
   * @return prerequisiteChains
   */
  @Valid 
  @Schema(name = "prerequisiteChains", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prerequisiteChains")
  public List<UUID> getPrerequisiteChains() {
    return prerequisiteChains;
  }

  public void setPrerequisiteChains(List<UUID> prerequisiteChains) {
    this.prerequisiteChains = prerequisiteChains;
  }

  public EntryRequirement worldFlagsRequired(List<String> worldFlagsRequired) {
    this.worldFlagsRequired = worldFlagsRequired;
    return this;
  }

  public EntryRequirement addWorldFlagsRequiredItem(String worldFlagsRequiredItem) {
    if (this.worldFlagsRequired == null) {
      this.worldFlagsRequired = new ArrayList<>();
    }
    this.worldFlagsRequired.add(worldFlagsRequiredItem);
    return this;
  }

  /**
   * Get worldFlagsRequired
   * @return worldFlagsRequired
   */
  
  @Schema(name = "worldFlagsRequired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldFlagsRequired")
  public List<String> getWorldFlagsRequired() {
    return worldFlagsRequired;
  }

  public void setWorldFlagsRequired(List<String> worldFlagsRequired) {
    this.worldFlagsRequired = worldFlagsRequired;
  }

  public EntryRequirement cooldownExpiresAt(@Nullable OffsetDateTime cooldownExpiresAt) {
    this.cooldownExpiresAt = cooldownExpiresAt;
    return this;
  }

  /**
   * Get cooldownExpiresAt
   * @return cooldownExpiresAt
   */
  @Valid 
  @Schema(name = "cooldownExpiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownExpiresAt")
  public @Nullable OffsetDateTime getCooldownExpiresAt() {
    return cooldownExpiresAt;
  }

  public void setCooldownExpiresAt(@Nullable OffsetDateTime cooldownExpiresAt) {
    this.cooldownExpiresAt = cooldownExpiresAt;
  }

  public EntryRequirement guildStandingRequired(@Nullable Integer guildStandingRequired) {
    this.guildStandingRequired = guildStandingRequired;
    return this;
  }

  /**
   * Get guildStandingRequired
   * minimum: 0
   * maximum: 100
   * @return guildStandingRequired
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "guildStandingRequired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildStandingRequired")
  public @Nullable Integer getGuildStandingRequired() {
    return guildStandingRequired;
  }

  public void setGuildStandingRequired(@Nullable Integer guildStandingRequired) {
    this.guildStandingRequired = guildStandingRequired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntryRequirement entryRequirement = (EntryRequirement) o;
    return Objects.equals(this.minimumLevel, entryRequirement.minimumLevel) &&
        Objects.equals(this.reputationThreshold, entryRequirement.reputationThreshold) &&
        Objects.equals(this.prerequisiteChains, entryRequirement.prerequisiteChains) &&
        Objects.equals(this.worldFlagsRequired, entryRequirement.worldFlagsRequired) &&
        Objects.equals(this.cooldownExpiresAt, entryRequirement.cooldownExpiresAt) &&
        Objects.equals(this.guildStandingRequired, entryRequirement.guildStandingRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minimumLevel, reputationThreshold, prerequisiteChains, worldFlagsRequired, cooldownExpiresAt, guildStandingRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntryRequirement {\n");
    sb.append("    minimumLevel: ").append(toIndentedString(minimumLevel)).append("\n");
    sb.append("    reputationThreshold: ").append(toIndentedString(reputationThreshold)).append("\n");
    sb.append("    prerequisiteChains: ").append(toIndentedString(prerequisiteChains)).append("\n");
    sb.append("    worldFlagsRequired: ").append(toIndentedString(worldFlagsRequired)).append("\n");
    sb.append("    cooldownExpiresAt: ").append(toIndentedString(cooldownExpiresAt)).append("\n");
    sb.append("    guildStandingRequired: ").append(toIndentedString(guildStandingRequired)).append("\n");
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

