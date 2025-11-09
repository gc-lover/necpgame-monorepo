package com.necpgame.backjava.model;

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
 * CreatePartyRequest
 */

@JsonTypeName("createParty_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreatePartyRequest {

  private String leaderCharacterId;

  private Integer maxMembers = 5;

  /**
   * Gets or Sets lootMode
   */
  public enum LootModeEnum {
    PERSONAL("personal"),
    
    NEED_GREED("need_greed"),
    
    MASTER_LOOTER("master_looter");

    private final String value;

    LootModeEnum(String value) {
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
    public static LootModeEnum fromValue(String value) {
      for (LootModeEnum b : LootModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LootModeEnum lootMode = LootModeEnum.NEED_GREED;

  public CreatePartyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreatePartyRequest(String leaderCharacterId) {
    this.leaderCharacterId = leaderCharacterId;
  }

  public CreatePartyRequest leaderCharacterId(String leaderCharacterId) {
    this.leaderCharacterId = leaderCharacterId;
    return this;
  }

  /**
   * Get leaderCharacterId
   * @return leaderCharacterId
   */
  @NotNull 
  @Schema(name = "leader_character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leader_character_id")
  public String getLeaderCharacterId() {
    return leaderCharacterId;
  }

  public void setLeaderCharacterId(String leaderCharacterId) {
    this.leaderCharacterId = leaderCharacterId;
  }

  public CreatePartyRequest maxMembers(Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * minimum: 2
   * maximum: 5
   * @return maxMembers
   */
  @Min(value = 2) @Max(value = 5) 
  @Schema(name = "max_members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_members")
  public Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public CreatePartyRequest lootMode(LootModeEnum lootMode) {
    this.lootMode = lootMode;
    return this;
  }

  /**
   * Get lootMode
   * @return lootMode
   */
  
  @Schema(name = "loot_mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_mode")
  public LootModeEnum getLootMode() {
    return lootMode;
  }

  public void setLootMode(LootModeEnum lootMode) {
    this.lootMode = lootMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePartyRequest createPartyRequest = (CreatePartyRequest) o;
    return Objects.equals(this.leaderCharacterId, createPartyRequest.leaderCharacterId) &&
        Objects.equals(this.maxMembers, createPartyRequest.maxMembers) &&
        Objects.equals(this.lootMode, createPartyRequest.lootMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderCharacterId, maxMembers, lootMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePartyRequest {\n");
    sb.append("    leaderCharacterId: ").append(toIndentedString(leaderCharacterId)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    lootMode: ").append(toIndentedString(lootMode)).append("\n");
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

