package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * GenerateNPC200Response
 */

@JsonTypeName("generateNPC_200_response")

public class GenerateNPC200Response {

  private @Nullable String npcId;

  private @Nullable String name;

  private @Nullable Object personality;

  private @Nullable String backstory;

  public GenerateNPC200Response npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public GenerateNPC200Response name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public GenerateNPC200Response personality(@Nullable Object personality) {
    this.personality = personality;
    return this;
  }

  /**
   * Get personality
   * @return personality
   */
  
  @Schema(name = "personality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personality")
  public @Nullable Object getPersonality() {
    return personality;
  }

  public void setPersonality(@Nullable Object personality) {
    this.personality = personality;
  }

  public GenerateNPC200Response backstory(@Nullable String backstory) {
    this.backstory = backstory;
    return this;
  }

  /**
   * Get backstory
   * @return backstory
   */
  
  @Schema(name = "backstory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backstory")
  public @Nullable String getBackstory() {
    return backstory;
  }

  public void setBackstory(@Nullable String backstory) {
    this.backstory = backstory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateNPC200Response generateNPC200Response = (GenerateNPC200Response) o;
    return Objects.equals(this.npcId, generateNPC200Response.npcId) &&
        Objects.equals(this.name, generateNPC200Response.name) &&
        Objects.equals(this.personality, generateNPC200Response.personality) &&
        Objects.equals(this.backstory, generateNPC200Response.backstory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, personality, backstory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateNPC200Response {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    personality: ").append(toIndentedString(personality)).append("\n");
    sb.append("    backstory: ").append(toIndentedString(backstory)).append("\n");
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

