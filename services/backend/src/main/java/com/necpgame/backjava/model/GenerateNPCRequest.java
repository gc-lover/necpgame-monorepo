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
 * GenerateNPCRequest
 */

@JsonTypeName("generateNPC_request")

public class GenerateNPCRequest {

  private @Nullable String template;

  private @Nullable String faction;

  private @Nullable String region;

  private @Nullable String role;

  public GenerateNPCRequest template(@Nullable String template) {
    this.template = template;
    return this;
  }

  /**
   * Get template
   * @return template
   */
  
  @Schema(name = "template", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("template")
  public @Nullable String getTemplate() {
    return template;
  }

  public void setTemplate(@Nullable String template) {
    this.template = template;
  }

  public GenerateNPCRequest faction(@Nullable String faction) {
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

  public GenerateNPCRequest region(@Nullable String region) {
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

  public GenerateNPCRequest role(@Nullable String role) {
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
    GenerateNPCRequest generateNPCRequest = (GenerateNPCRequest) o;
    return Objects.equals(this.template, generateNPCRequest.template) &&
        Objects.equals(this.faction, generateNPCRequest.faction) &&
        Objects.equals(this.region, generateNPCRequest.region) &&
        Objects.equals(this.role, generateNPCRequest.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(template, faction, region, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateNPCRequest {\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
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

