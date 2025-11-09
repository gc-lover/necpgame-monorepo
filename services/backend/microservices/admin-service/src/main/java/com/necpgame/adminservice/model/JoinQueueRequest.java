package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * JoinQueueRequest
 */

@JsonTypeName("joinQueue_request")

public class JoinQueueRequest {

  private String characterId;

  /**
   * Gets or Sets activityType
   */
  public enum ActivityTypeEnum {
    PVP_ARENA_3V3("pvp_arena_3v3"),
    
    PVP_ARENA_5V5("pvp_arena_5v5"),
    
    RAID_10("raid_10"),
    
    RAID_25("raid_25"),
    
    DUNGEON_5("dungeon_5"),
    
    EXTRACTION_ZONE("extraction_zone");

    private final String value;

    ActivityTypeEnum(String value) {
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
    public static ActivityTypeEnum fromValue(String value) {
      for (ActivityTypeEnum b : ActivityTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActivityTypeEnum activityType;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    TANK("tank"),
    
    DPS("dps"),
    
    HEALER("healer"),
    
    SUPPORT("support");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  private @Nullable String partyId;

  public JoinQueueRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinQueueRequest(String characterId, ActivityTypeEnum activityType, RoleEnum role) {
    this.characterId = characterId;
    this.activityType = activityType;
    this.role = role;
  }

  public JoinQueueRequest characterId(String characterId) {
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

  public JoinQueueRequest activityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @NotNull 
  @Schema(name = "activity_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activity_type")
  public ActivityTypeEnum getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
  }

  public JoinQueueRequest role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public JoinQueueRequest partyId(@Nullable String partyId) {
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
    JoinQueueRequest joinQueueRequest = (JoinQueueRequest) o;
    return Objects.equals(this.characterId, joinQueueRequest.characterId) &&
        Objects.equals(this.activityType, joinQueueRequest.activityType) &&
        Objects.equals(this.role, joinQueueRequest.role) &&
        Objects.equals(this.partyId, joinQueueRequest.partyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, activityType, role, partyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinQueueRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

