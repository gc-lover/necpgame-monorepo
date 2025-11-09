package com.necpgame.adminservice.model;

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
 * GenerateNPCPersonalityRequest
 */

@JsonTypeName("generateNPCPersonality_request")

public class GenerateNPCPersonalityRequest {

  private @Nullable String npcTemplate;

  private @Nullable String faction;

  private @Nullable String region;

  private @Nullable String role;

  public GenerateNPCPersonalityRequest npcTemplate(@Nullable String npcTemplate) {
    this.npcTemplate = npcTemplate;
    return this;
  }

  /**
   * Get npcTemplate
   * @return npcTemplate
   */
  
  @Schema(name = "npc_template", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_template")
  public @Nullable String getNpcTemplate() {
    return npcTemplate;
  }

  public void setNpcTemplate(@Nullable String npcTemplate) {
    this.npcTemplate = npcTemplate;
  }

  public GenerateNPCPersonalityRequest faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public GenerateNPCPersonalityRequest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public GenerateNPCPersonalityRequest role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateNPCPersonalityRequest generateNPCPersonalityRequest = (GenerateNPCPersonalityRequest) o;
    return Objects.equals(this.npcTemplate, generateNPCPersonalityRequest.npcTemplate) &&
        Objects.equals(this.faction, generateNPCPersonalityRequest.faction) &&
        Objects.equals(this.region, generateNPCPersonalityRequest.region) &&
        Objects.equals(this.role, generateNPCPersonalityRequest.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcTemplate, faction, region, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateNPCPersonalityRequest {\n");
    sb.append("    npcTemplate: ").append(toIndentedString(npcTemplate)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

