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
 * RegionMapQuestChainsInner
 */

@JsonTypeName("RegionMap_quest_chains_inner")

public class RegionMapQuestChainsInner {

  private @Nullable String chainId;

  private @Nullable String chainName;

  private @Nullable Integer questsCount;

  public RegionMapQuestChainsInner chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public RegionMapQuestChainsInner chainName(@Nullable String chainName) {
    this.chainName = chainName;
    return this;
  }

  /**
   * Get chainName
   * @return chainName
   */
  
  @Schema(name = "chain_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_name")
  public @Nullable String getChainName() {
    return chainName;
  }

  public void setChainName(@Nullable String chainName) {
    this.chainName = chainName;
  }

  public RegionMapQuestChainsInner questsCount(@Nullable Integer questsCount) {
    this.questsCount = questsCount;
    return this;
  }

  /**
   * Get questsCount
   * @return questsCount
   */
  
  @Schema(name = "quests_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests_count")
  public @Nullable Integer getQuestsCount() {
    return questsCount;
  }

  public void setQuestsCount(@Nullable Integer questsCount) {
    this.questsCount = questsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionMapQuestChainsInner regionMapQuestChainsInner = (RegionMapQuestChainsInner) o;
    return Objects.equals(this.chainId, regionMapQuestChainsInner.chainId) &&
        Objects.equals(this.chainName, regionMapQuestChainsInner.chainName) &&
        Objects.equals(this.questsCount, regionMapQuestChainsInner.questsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chainId, chainName, questsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionMapQuestChainsInner {\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    chainName: ").append(toIndentedString(chainName)).append("\n");
    sb.append("    questsCount: ").append(toIndentedString(questsCount)).append("\n");
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

