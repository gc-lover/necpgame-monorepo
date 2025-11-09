package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.QuestContext;
import com.necpgame.narrativeservice.model.ResolveOptionRequestClient;
import com.necpgame.narrativeservice.model.SkillRoll;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * ResolveOptionRequest
 */


public class ResolveOptionRequest {

  private UUID characterId;

  private String nodeId;

  private String optionId;

  private @Nullable QuestContext questContext;

  @Valid
  private List<@Valid SkillRoll> skillRolls = new ArrayList<>();

  private @Nullable ResolveOptionRequestClient client;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public ResolveOptionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResolveOptionRequest(UUID characterId, String nodeId, String optionId, List<@Valid SkillRoll> skillRolls) {
    this.characterId = characterId;
    this.nodeId = nodeId;
    this.optionId = optionId;
    this.skillRolls = skillRolls;
  }

  public ResolveOptionRequest characterId(UUID characterId) {
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

  public ResolveOptionRequest nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  @NotNull 
  @Schema(name = "nodeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nodeId")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public ResolveOptionRequest optionId(String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  @NotNull 
  @Schema(name = "optionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("optionId")
  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  public ResolveOptionRequest questContext(@Nullable QuestContext questContext) {
    this.questContext = questContext;
    return this;
  }

  /**
   * Get questContext
   * @return questContext
   */
  @Valid 
  @Schema(name = "questContext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questContext")
  public @Nullable QuestContext getQuestContext() {
    return questContext;
  }

  public void setQuestContext(@Nullable QuestContext questContext) {
    this.questContext = questContext;
  }

  public ResolveOptionRequest skillRolls(List<@Valid SkillRoll> skillRolls) {
    this.skillRolls = skillRolls;
    return this;
  }

  public ResolveOptionRequest addSkillRollsItem(SkillRoll skillRollsItem) {
    if (this.skillRolls == null) {
      this.skillRolls = new ArrayList<>();
    }
    this.skillRolls.add(skillRollsItem);
    return this;
  }

  /**
   * Get skillRolls
   * @return skillRolls
   */
  @NotNull @Valid 
  @Schema(name = "skillRolls", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skillRolls")
  public List<@Valid SkillRoll> getSkillRolls() {
    return skillRolls;
  }

  public void setSkillRolls(List<@Valid SkillRoll> skillRolls) {
    this.skillRolls = skillRolls;
  }

  public ResolveOptionRequest client(@Nullable ResolveOptionRequestClient client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  @Valid 
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable ResolveOptionRequestClient getClient() {
    return client;
  }

  public void setClient(@Nullable ResolveOptionRequestClient client) {
    this.client = client;
  }

  public ResolveOptionRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public ResolveOptionRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolveOptionRequest resolveOptionRequest = (ResolveOptionRequest) o;
    return Objects.equals(this.characterId, resolveOptionRequest.characterId) &&
        Objects.equals(this.nodeId, resolveOptionRequest.nodeId) &&
        Objects.equals(this.optionId, resolveOptionRequest.optionId) &&
        Objects.equals(this.questContext, resolveOptionRequest.questContext) &&
        Objects.equals(this.skillRolls, resolveOptionRequest.skillRolls) &&
        Objects.equals(this.client, resolveOptionRequest.client) &&
        Objects.equals(this.metadata, resolveOptionRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, nodeId, optionId, questContext, skillRolls, client, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolveOptionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
    sb.append("    questContext: ").append(toIndentedString(questContext)).append("\n");
    sb.append("    skillRolls: ").append(toIndentedString(skillRolls)).append("\n");
    sb.append("    client: ").append(toIndentedString(client)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

