package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * FactionChainContactNpc
 */

@JsonTypeName("FactionChain_contactNpc")

public class FactionChainContactNpc {

  private @Nullable UUID npcId;

  private @Nullable String name;

  private @Nullable String location;

  public FactionChainContactNpc npcId(@Nullable UUID npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @Valid 
  @Schema(name = "npcId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcId")
  public @Nullable UUID getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable UUID npcId) {
    this.npcId = npcId;
  }

  public FactionChainContactNpc name(@Nullable String name) {
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

  public FactionChainContactNpc location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionChainContactNpc factionChainContactNpc = (FactionChainContactNpc) o;
    return Objects.equals(this.npcId, factionChainContactNpc.npcId) &&
        Objects.equals(this.name, factionChainContactNpc.name) &&
        Objects.equals(this.location, factionChainContactNpc.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionChainContactNpc {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

