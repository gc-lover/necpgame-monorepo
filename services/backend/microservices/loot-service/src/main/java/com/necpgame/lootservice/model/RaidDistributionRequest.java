package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.GuaranteedReward;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RaidDistributionRequest
 */


public class RaidDistributionRequest {

  private UUID resultId;

  private UUID raidId;

  @Valid
  private List<@Valid GuaranteedReward> guaranteedTokens = new ArrayList<>();

  @Valid
  private List<UUID> lootCouncil = new ArrayList<>();

  public RaidDistributionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RaidDistributionRequest(UUID resultId, UUID raidId) {
    this.resultId = resultId;
    this.raidId = raidId;
  }

  public RaidDistributionRequest resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public RaidDistributionRequest raidId(UUID raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  @NotNull @Valid 
  @Schema(name = "raidId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("raidId")
  public UUID getRaidId() {
    return raidId;
  }

  public void setRaidId(UUID raidId) {
    this.raidId = raidId;
  }

  public RaidDistributionRequest guaranteedTokens(List<@Valid GuaranteedReward> guaranteedTokens) {
    this.guaranteedTokens = guaranteedTokens;
    return this;
  }

  public RaidDistributionRequest addGuaranteedTokensItem(GuaranteedReward guaranteedTokensItem) {
    if (this.guaranteedTokens == null) {
      this.guaranteedTokens = new ArrayList<>();
    }
    this.guaranteedTokens.add(guaranteedTokensItem);
    return this;
  }

  /**
   * Get guaranteedTokens
   * @return guaranteedTokens
   */
  @Valid 
  @Schema(name = "guaranteedTokens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteedTokens")
  public List<@Valid GuaranteedReward> getGuaranteedTokens() {
    return guaranteedTokens;
  }

  public void setGuaranteedTokens(List<@Valid GuaranteedReward> guaranteedTokens) {
    this.guaranteedTokens = guaranteedTokens;
  }

  public RaidDistributionRequest lootCouncil(List<UUID> lootCouncil) {
    this.lootCouncil = lootCouncil;
    return this;
  }

  public RaidDistributionRequest addLootCouncilItem(UUID lootCouncilItem) {
    if (this.lootCouncil == null) {
      this.lootCouncil = new ArrayList<>();
    }
    this.lootCouncil.add(lootCouncilItem);
    return this;
  }

  /**
   * Get lootCouncil
   * @return lootCouncil
   */
  @Valid 
  @Schema(name = "lootCouncil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootCouncil")
  public List<UUID> getLootCouncil() {
    return lootCouncil;
  }

  public void setLootCouncil(List<UUID> lootCouncil) {
    this.lootCouncil = lootCouncil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RaidDistributionRequest raidDistributionRequest = (RaidDistributionRequest) o;
    return Objects.equals(this.resultId, raidDistributionRequest.resultId) &&
        Objects.equals(this.raidId, raidDistributionRequest.raidId) &&
        Objects.equals(this.guaranteedTokens, raidDistributionRequest.guaranteedTokens) &&
        Objects.equals(this.lootCouncil, raidDistributionRequest.lootCouncil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, raidId, guaranteedTokens, lootCouncil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidDistributionRequest {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    guaranteedTokens: ").append(toIndentedString(guaranteedTokens)).append("\n");
    sb.append("    lootCouncil: ").append(toIndentedString(lootCouncil)).append("\n");
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

