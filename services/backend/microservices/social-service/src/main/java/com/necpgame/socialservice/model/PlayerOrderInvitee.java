package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * PlayerOrderInvitee
 */


public class PlayerOrderInvitee {

  private UUID id;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PLAYER("player"),
    
    NPC("npc");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable String displayName;

  private @Nullable BigDecimal relationshipScore;

  public PlayerOrderInvitee() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderInvitee(UUID id, TypeEnum type) {
    this.id = id;
    this.type = type;
  }

  public PlayerOrderInvitee id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public PlayerOrderInvitee type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public PlayerOrderInvitee displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public PlayerOrderInvitee relationshipScore(@Nullable BigDecimal relationshipScore) {
    this.relationshipScore = relationshipScore;
    return this;
  }

  /**
   * Текущий кредит доверия (для players/NPC).
   * @return relationshipScore
   */
  @Valid 
  @Schema(name = "relationshipScore", description = "Текущий кредит доверия (для players/NPC).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationshipScore")
  public @Nullable BigDecimal getRelationshipScore() {
    return relationshipScore;
  }

  public void setRelationshipScore(@Nullable BigDecimal relationshipScore) {
    this.relationshipScore = relationshipScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderInvitee playerOrderInvitee = (PlayerOrderInvitee) o;
    return Objects.equals(this.id, playerOrderInvitee.id) &&
        Objects.equals(this.type, playerOrderInvitee.type) &&
        Objects.equals(this.displayName, playerOrderInvitee.displayName) &&
        Objects.equals(this.relationshipScore, playerOrderInvitee.relationshipScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, displayName, relationshipScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderInvitee {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    relationshipScore: ").append(toIndentedString(relationshipScore)).append("\n");
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

