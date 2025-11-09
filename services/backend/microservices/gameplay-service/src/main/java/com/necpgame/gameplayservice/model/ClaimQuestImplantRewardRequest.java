package com.necpgame.gameplayservice.model;

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
 * ClaimQuestImplantRewardRequest
 */

@JsonTypeName("claimQuestImplantReward_request")

public class ClaimQuestImplantRewardRequest {

  private String characterId;

  private String questId;

  private String rewardId;

  public ClaimQuestImplantRewardRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ClaimQuestImplantRewardRequest(String characterId, String questId, String rewardId) {
    this.characterId = characterId;
    this.questId = questId;
    this.rewardId = rewardId;
  }

  public ClaimQuestImplantRewardRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ClaimQuestImplantRewardRequest questId(String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  @NotNull 
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quest_id")
  public String getQuestId() {
    return questId;
  }

  public void setQuestId(String questId) {
    this.questId = questId;
  }

  public ClaimQuestImplantRewardRequest rewardId(String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  @NotNull 
  @Schema(name = "reward_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reward_id")
  public String getRewardId() {
    return rewardId;
  }

  public void setRewardId(String rewardId) {
    this.rewardId = rewardId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClaimQuestImplantRewardRequest claimQuestImplantRewardRequest = (ClaimQuestImplantRewardRequest) o;
    return Objects.equals(this.characterId, claimQuestImplantRewardRequest.characterId) &&
        Objects.equals(this.questId, claimQuestImplantRewardRequest.questId) &&
        Objects.equals(this.rewardId, claimQuestImplantRewardRequest.rewardId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, questId, rewardId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClaimQuestImplantRewardRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
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

