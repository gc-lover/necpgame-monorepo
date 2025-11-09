package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChainStage;
import com.necpgame.worldservice.model.ChainStatus;
import com.necpgame.worldservice.model.ChainVisibility;
import com.necpgame.worldservice.model.EntryRequirement;
import com.necpgame.worldservice.model.FactionChainContactNpc;
import com.necpgame.worldservice.model.FactionChainTelemetry;
import com.necpgame.worldservice.model.ReputationChange;
import com.necpgame.worldservice.model.RewardPayload;
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
 * FactionChain
 */


public class FactionChain {

  private UUID chainId;

  private UUID factionId;

  private String name;

  private @Nullable String synopsis;

  private ChainVisibility visibility;

  private ChainStatus status;

  private @Nullable EntryRequirement entryRequirements;

  private @Nullable FactionChainContactNpc contactNpc;

  @Valid
  private List<@Valid ChainStage> stages = new ArrayList<>();

  private @Nullable Integer estimatedDurationMinutes;

  private @Nullable Integer cooldownMinutes;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lockoutUntil;

  @Valid
  private List<@Valid ReputationChange> reputationImpacts = new ArrayList<>();

  private @Nullable RewardPayload rewards;

  private @Nullable FactionChainTelemetry telemetry;

  public FactionChain() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FactionChain(UUID chainId, UUID factionId, String name, ChainVisibility visibility, ChainStatus status) {
    this.chainId = chainId;
    this.factionId = factionId;
    this.name = name;
    this.visibility = visibility;
    this.status = status;
  }

  public FactionChain chainId(UUID chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  @NotNull @Valid 
  @Schema(name = "chainId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("chainId")
  public UUID getChainId() {
    return chainId;
  }

  public void setChainId(UUID chainId) {
    this.chainId = chainId;
  }

  public FactionChain factionId(UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull @Valid 
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factionId")
  public UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(UUID factionId) {
    this.factionId = factionId;
  }

  public FactionChain name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FactionChain synopsis(@Nullable String synopsis) {
    this.synopsis = synopsis;
    return this;
  }

  /**
   * Get synopsis
   * @return synopsis
   */
  
  @Schema(name = "synopsis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synopsis")
  public @Nullable String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(@Nullable String synopsis) {
    this.synopsis = synopsis;
  }

  public FactionChain visibility(ChainVisibility visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  @NotNull @Valid 
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("visibility")
  public ChainVisibility getVisibility() {
    return visibility;
  }

  public void setVisibility(ChainVisibility visibility) {
    this.visibility = visibility;
  }

  public FactionChain status(ChainStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public ChainStatus getStatus() {
    return status;
  }

  public void setStatus(ChainStatus status) {
    this.status = status;
  }

  public FactionChain entryRequirements(@Nullable EntryRequirement entryRequirements) {
    this.entryRequirements = entryRequirements;
    return this;
  }

  /**
   * Get entryRequirements
   * @return entryRequirements
   */
  @Valid 
  @Schema(name = "entryRequirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryRequirements")
  public @Nullable EntryRequirement getEntryRequirements() {
    return entryRequirements;
  }

  public void setEntryRequirements(@Nullable EntryRequirement entryRequirements) {
    this.entryRequirements = entryRequirements;
  }

  public FactionChain contactNpc(@Nullable FactionChainContactNpc contactNpc) {
    this.contactNpc = contactNpc;
    return this;
  }

  /**
   * Get contactNpc
   * @return contactNpc
   */
  @Valid 
  @Schema(name = "contactNpc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contactNpc")
  public @Nullable FactionChainContactNpc getContactNpc() {
    return contactNpc;
  }

  public void setContactNpc(@Nullable FactionChainContactNpc contactNpc) {
    this.contactNpc = contactNpc;
  }

  public FactionChain stages(List<@Valid ChainStage> stages) {
    this.stages = stages;
    return this;
  }

  public FactionChain addStagesItem(ChainStage stagesItem) {
    if (this.stages == null) {
      this.stages = new ArrayList<>();
    }
    this.stages.add(stagesItem);
    return this;
  }

  /**
   * Get stages
   * @return stages
   */
  @Valid 
  @Schema(name = "stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stages")
  public List<@Valid ChainStage> getStages() {
    return stages;
  }

  public void setStages(List<@Valid ChainStage> stages) {
    this.stages = stages;
  }

  public FactionChain estimatedDurationMinutes(@Nullable Integer estimatedDurationMinutes) {
    this.estimatedDurationMinutes = estimatedDurationMinutes;
    return this;
  }

  /**
   * Get estimatedDurationMinutes
   * minimum: 5
   * @return estimatedDurationMinutes
   */
  @Min(value = 5) 
  @Schema(name = "estimatedDurationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedDurationMinutes")
  public @Nullable Integer getEstimatedDurationMinutes() {
    return estimatedDurationMinutes;
  }

  public void setEstimatedDurationMinutes(@Nullable Integer estimatedDurationMinutes) {
    this.estimatedDurationMinutes = estimatedDurationMinutes;
  }

  public FactionChain cooldownMinutes(@Nullable Integer cooldownMinutes) {
    this.cooldownMinutes = cooldownMinutes;
    return this;
  }

  /**
   * Get cooldownMinutes
   * minimum: 0
   * @return cooldownMinutes
   */
  @Min(value = 0) 
  @Schema(name = "cooldownMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownMinutes")
  public @Nullable Integer getCooldownMinutes() {
    return cooldownMinutes;
  }

  public void setCooldownMinutes(@Nullable Integer cooldownMinutes) {
    this.cooldownMinutes = cooldownMinutes;
  }

  public FactionChain lockoutUntil(@Nullable OffsetDateTime lockoutUntil) {
    this.lockoutUntil = lockoutUntil;
    return this;
  }

  /**
   * Get lockoutUntil
   * @return lockoutUntil
   */
  @Valid 
  @Schema(name = "lockoutUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lockoutUntil")
  public @Nullable OffsetDateTime getLockoutUntil() {
    return lockoutUntil;
  }

  public void setLockoutUntil(@Nullable OffsetDateTime lockoutUntil) {
    this.lockoutUntil = lockoutUntil;
  }

  public FactionChain reputationImpacts(List<@Valid ReputationChange> reputationImpacts) {
    this.reputationImpacts = reputationImpacts;
    return this;
  }

  public FactionChain addReputationImpactsItem(ReputationChange reputationImpactsItem) {
    if (this.reputationImpacts == null) {
      this.reputationImpacts = new ArrayList<>();
    }
    this.reputationImpacts.add(reputationImpactsItem);
    return this;
  }

  /**
   * Get reputationImpacts
   * @return reputationImpacts
   */
  @Valid 
  @Schema(name = "reputationImpacts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationImpacts")
  public List<@Valid ReputationChange> getReputationImpacts() {
    return reputationImpacts;
  }

  public void setReputationImpacts(List<@Valid ReputationChange> reputationImpacts) {
    this.reputationImpacts = reputationImpacts;
  }

  public FactionChain rewards(@Nullable RewardPayload rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable RewardPayload getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable RewardPayload rewards) {
    this.rewards = rewards;
  }

  public FactionChain telemetry(@Nullable FactionChainTelemetry telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  @Valid 
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public @Nullable FactionChainTelemetry getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable FactionChainTelemetry telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionChain factionChain = (FactionChain) o;
    return Objects.equals(this.chainId, factionChain.chainId) &&
        Objects.equals(this.factionId, factionChain.factionId) &&
        Objects.equals(this.name, factionChain.name) &&
        Objects.equals(this.synopsis, factionChain.synopsis) &&
        Objects.equals(this.visibility, factionChain.visibility) &&
        Objects.equals(this.status, factionChain.status) &&
        Objects.equals(this.entryRequirements, factionChain.entryRequirements) &&
        Objects.equals(this.contactNpc, factionChain.contactNpc) &&
        Objects.equals(this.stages, factionChain.stages) &&
        Objects.equals(this.estimatedDurationMinutes, factionChain.estimatedDurationMinutes) &&
        Objects.equals(this.cooldownMinutes, factionChain.cooldownMinutes) &&
        Objects.equals(this.lockoutUntil, factionChain.lockoutUntil) &&
        Objects.equals(this.reputationImpacts, factionChain.reputationImpacts) &&
        Objects.equals(this.rewards, factionChain.rewards) &&
        Objects.equals(this.telemetry, factionChain.telemetry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, factionId, name, synopsis, visibility, status, entryRequirements, contactNpc, stages, estimatedDurationMinutes, cooldownMinutes, lockoutUntil, reputationImpacts, rewards, telemetry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionChain {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    synopsis: ").append(toIndentedString(synopsis)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    entryRequirements: ").append(toIndentedString(entryRequirements)).append("\n");
    sb.append("    contactNpc: ").append(toIndentedString(contactNpc)).append("\n");
    sb.append("    stages: ").append(toIndentedString(stages)).append("\n");
    sb.append("    estimatedDurationMinutes: ").append(toIndentedString(estimatedDurationMinutes)).append("\n");
    sb.append("    cooldownMinutes: ").append(toIndentedString(cooldownMinutes)).append("\n");
    sb.append("    lockoutUntil: ").append(toIndentedString(lockoutUntil)).append("\n");
    sb.append("    reputationImpacts: ").append(toIndentedString(reputationImpacts)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
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

