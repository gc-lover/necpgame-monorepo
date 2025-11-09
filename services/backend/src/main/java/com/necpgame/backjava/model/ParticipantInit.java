package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ParticipantInitInitialPosition;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ParticipantInit
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ParticipantInit {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PLAYER("PLAYER"),
    
    NPC("NPC"),
    
    AI_ENEMY("AI_ENEMY");

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

  private String id;

  private String team;

  private @Nullable ParticipantInitInitialPosition initialPosition;

  public ParticipantInit() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ParticipantInit(TypeEnum type, String id, String team) {
    this.type = type;
    this.id = id;
    this.team = team;
  }

  public ParticipantInit type(TypeEnum type) {
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

  public ParticipantInit id(String id) {
    this.id = id;
    return this;
  }

  /**
   * character_id для игроков, npc_id для NPC
   * @return id
   */
  @NotNull 
  @Schema(name = "id", description = "character_id для игроков, npc_id для NPC", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ParticipantInit team(String team) {
    this.team = team;
    return this;
  }

  /**
   * Get team
   * @return team
   */
  @NotNull 
  @Schema(name = "team", example = "A", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("team")
  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public ParticipantInit initialPosition(@Nullable ParticipantInitInitialPosition initialPosition) {
    this.initialPosition = initialPosition;
    return this;
  }

  /**
   * Get initialPosition
   * @return initialPosition
   */
  @Valid 
  @Schema(name = "initial_position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initial_position")
  public @Nullable ParticipantInitInitialPosition getInitialPosition() {
    return initialPosition;
  }

  public void setInitialPosition(@Nullable ParticipantInitInitialPosition initialPosition) {
    this.initialPosition = initialPosition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantInit participantInit = (ParticipantInit) o;
    return Objects.equals(this.type, participantInit.type) &&
        Objects.equals(this.id, participantInit.id) &&
        Objects.equals(this.team, participantInit.team) &&
        Objects.equals(this.initialPosition, participantInit.initialPosition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, team, initialPosition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantInit {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    team: ").append(toIndentedString(team)).append("\n");
    sb.append("    initialPosition: ").append(toIndentedString(initialPosition)).append("\n");
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

