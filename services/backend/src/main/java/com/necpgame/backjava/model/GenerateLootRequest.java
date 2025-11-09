package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GenerateLootRequestPosition;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateLootRequest
 */

@JsonTypeName("generateLoot_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GenerateLootRequest {

  /**
   * Gets or Sets sourceType
   */
  public enum SourceTypeEnum {
    NPC_DEATH("npc_death"),
    
    CONTAINER("container"),
    
    BOSS("boss"),
    
    QUEST_REWARD("quest_reward");

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

  private String sourceId;

  private @Nullable GenerateLootRequestPosition position;

  private @Nullable String partyId;

  public GenerateLootRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateLootRequest(SourceTypeEnum sourceType, String sourceId) {
    this.sourceType = sourceType;
    this.sourceId = sourceId;
  }

  public GenerateLootRequest sourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get sourceType
   * @return sourceType
   */
  @NotNull 
  @Schema(name = "source_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_type")
  public SourceTypeEnum getSourceType() {
    return sourceType;
  }

  public void setSourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
  }

  public GenerateLootRequest sourceId(String sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Get sourceId
   * @return sourceId
   */
  @NotNull 
  @Schema(name = "source_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_id")
  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public GenerateLootRequest position(@Nullable GenerateLootRequestPosition position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  @Valid 
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable GenerateLootRequestPosition getPosition() {
    return position;
  }

  public void setPosition(@Nullable GenerateLootRequestPosition position) {
    this.position = position;
  }

  public GenerateLootRequest partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Если в группе
   * @return partyId
   */
  
  @Schema(name = "party_id", description = "Если в группе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party_id")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateLootRequest generateLootRequest = (GenerateLootRequest) o;
    return Objects.equals(this.sourceType, generateLootRequest.sourceType) &&
        Objects.equals(this.sourceId, generateLootRequest.sourceId) &&
        Objects.equals(this.position, generateLootRequest.position) &&
        Objects.equals(this.partyId, generateLootRequest.partyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, sourceId, position, partyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateLootRequest {\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
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

