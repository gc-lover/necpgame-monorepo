package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CharacterActivityEntryActor
 */

@JsonTypeName("CharacterActivityEntry_actor")

public class CharacterActivityEntryActor {

  /**
   * Gets or Sets actorType
   */
  public enum ActorTypeEnum {
    PLAYER("player"),
    
    MODERATOR("moderator"),
    
    SYSTEM("system");

    private final String value;

    ActorTypeEnum(String value) {
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
    public static ActorTypeEnum fromValue(String value) {
      for (ActorTypeEnum b : ActorTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActorTypeEnum actorType;

  private @Nullable UUID actorId;

  public CharacterActivityEntryActor actorType(@Nullable ActorTypeEnum actorType) {
    this.actorType = actorType;
    return this;
  }

  /**
   * Get actorType
   * @return actorType
   */
  
  @Schema(name = "actorType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorType")
  public @Nullable ActorTypeEnum getActorType() {
    return actorType;
  }

  public void setActorType(@Nullable ActorTypeEnum actorType) {
    this.actorType = actorType;
  }

  public CharacterActivityEntryActor actorId(@Nullable UUID actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  @Valid 
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable UUID getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable UUID actorId) {
    this.actorId = actorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterActivityEntryActor characterActivityEntryActor = (CharacterActivityEntryActor) o;
    return Objects.equals(this.actorType, characterActivityEntryActor.actorType) &&
        Objects.equals(this.actorId, characterActivityEntryActor.actorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actorType, actorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterActivityEntryActor {\n");
    sb.append("    actorType: ").append(toIndentedString(actorType)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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

