package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.FactionQuestsResponseEndingsInner;
import com.necpgame.narrativeservice.model.FactionQuestsResponseReputationRewardsInner;
import com.necpgame.narrativeservice.model.QuestNode;
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
 * FactionQuestsResponse
 */


public class FactionQuestsResponse {

  private @Nullable String factionId;

  private @Nullable String factionName;

  private @Nullable String factionDescription;

  @Valid
  private List<@Valid QuestNode> questChain = new ArrayList<>();

  @Valid
  private List<@Valid FactionQuestsResponseEndingsInner> endings = new ArrayList<>();

  @Valid
  private List<@Valid FactionQuestsResponseReputationRewardsInner> reputationRewards = new ArrayList<>();

  public FactionQuestsResponse factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public FactionQuestsResponse factionName(@Nullable String factionName) {
    this.factionName = factionName;
    return this;
  }

  /**
   * Get factionName
   * @return factionName
   */
  
  @Schema(name = "faction_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_name")
  public @Nullable String getFactionName() {
    return factionName;
  }

  public void setFactionName(@Nullable String factionName) {
    this.factionName = factionName;
  }

  public FactionQuestsResponse factionDescription(@Nullable String factionDescription) {
    this.factionDescription = factionDescription;
    return this;
  }

  /**
   * Get factionDescription
   * @return factionDescription
   */
  
  @Schema(name = "faction_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_description")
  public @Nullable String getFactionDescription() {
    return factionDescription;
  }

  public void setFactionDescription(@Nullable String factionDescription) {
    this.factionDescription = factionDescription;
  }

  public FactionQuestsResponse questChain(List<@Valid QuestNode> questChain) {
    this.questChain = questChain;
    return this;
  }

  public FactionQuestsResponse addQuestChainItem(QuestNode questChainItem) {
    if (this.questChain == null) {
      this.questChain = new ArrayList<>();
    }
    this.questChain.add(questChainItem);
    return this;
  }

  /**
   * Get questChain
   * @return questChain
   */
  @Valid 
  @Schema(name = "quest_chain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_chain")
  public List<@Valid QuestNode> getQuestChain() {
    return questChain;
  }

  public void setQuestChain(List<@Valid QuestNode> questChain) {
    this.questChain = questChain;
  }

  public FactionQuestsResponse endings(List<@Valid FactionQuestsResponseEndingsInner> endings) {
    this.endings = endings;
    return this;
  }

  public FactionQuestsResponse addEndingsItem(FactionQuestsResponseEndingsInner endingsItem) {
    if (this.endings == null) {
      this.endings = new ArrayList<>();
    }
    this.endings.add(endingsItem);
    return this;
  }

  /**
   * Возможные концовки quest chain
   * @return endings
   */
  @Valid 
  @Schema(name = "endings", description = "Возможные концовки quest chain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endings")
  public List<@Valid FactionQuestsResponseEndingsInner> getEndings() {
    return endings;
  }

  public void setEndings(List<@Valid FactionQuestsResponseEndingsInner> endings) {
    this.endings = endings;
  }

  public FactionQuestsResponse reputationRewards(List<@Valid FactionQuestsResponseReputationRewardsInner> reputationRewards) {
    this.reputationRewards = reputationRewards;
    return this;
  }

  public FactionQuestsResponse addReputationRewardsItem(FactionQuestsResponseReputationRewardsInner reputationRewardsItem) {
    if (this.reputationRewards == null) {
      this.reputationRewards = new ArrayList<>();
    }
    this.reputationRewards.add(reputationRewardsItem);
    return this;
  }

  /**
   * Get reputationRewards
   * @return reputationRewards
   */
  @Valid 
  @Schema(name = "reputation_rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_rewards")
  public List<@Valid FactionQuestsResponseReputationRewardsInner> getReputationRewards() {
    return reputationRewards;
  }

  public void setReputationRewards(List<@Valid FactionQuestsResponseReputationRewardsInner> reputationRewards) {
    this.reputationRewards = reputationRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionQuestsResponse factionQuestsResponse = (FactionQuestsResponse) o;
    return Objects.equals(this.factionId, factionQuestsResponse.factionId) &&
        Objects.equals(this.factionName, factionQuestsResponse.factionName) &&
        Objects.equals(this.factionDescription, factionQuestsResponse.factionDescription) &&
        Objects.equals(this.questChain, factionQuestsResponse.questChain) &&
        Objects.equals(this.endings, factionQuestsResponse.endings) &&
        Objects.equals(this.reputationRewards, factionQuestsResponse.reputationRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, factionName, factionDescription, questChain, endings, reputationRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionQuestsResponse {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    factionName: ").append(toIndentedString(factionName)).append("\n");
    sb.append("    factionDescription: ").append(toIndentedString(factionDescription)).append("\n");
    sb.append("    questChain: ").append(toIndentedString(questChain)).append("\n");
    sb.append("    endings: ").append(toIndentedString(endings)).append("\n");
    sb.append("    reputationRewards: ").append(toIndentedString(reputationRewards)).append("\n");
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

