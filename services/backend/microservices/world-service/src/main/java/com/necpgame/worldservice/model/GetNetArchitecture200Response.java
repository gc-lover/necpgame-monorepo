package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.BlackwallInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetNetArchitecture200Response
 */

@JsonTypeName("getNetArchitecture_200_response")

public class GetNetArchitecture200Response {

  @Valid
  private List<String> architectureLayers = new ArrayList<>();

  @Valid
  private List<String> protocols = new ArrayList<>();

  @Valid
  private List<String> securityLevels = new ArrayList<>();

  private @Nullable BlackwallInfo blackwall;

  public GetNetArchitecture200Response architectureLayers(List<String> architectureLayers) {
    this.architectureLayers = architectureLayers;
    return this;
  }

  public GetNetArchitecture200Response addArchitectureLayersItem(String architectureLayersItem) {
    if (this.architectureLayers == null) {
      this.architectureLayers = new ArrayList<>();
    }
    this.architectureLayers.add(architectureLayersItem);
    return this;
  }

  /**
   * Get architectureLayers
   * @return architectureLayers
   */
  
  @Schema(name = "architecture_layers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("architecture_layers")
  public List<String> getArchitectureLayers() {
    return architectureLayers;
  }

  public void setArchitectureLayers(List<String> architectureLayers) {
    this.architectureLayers = architectureLayers;
  }

  public GetNetArchitecture200Response protocols(List<String> protocols) {
    this.protocols = protocols;
    return this;
  }

  public GetNetArchitecture200Response addProtocolsItem(String protocolsItem) {
    if (this.protocols == null) {
      this.protocols = new ArrayList<>();
    }
    this.protocols.add(protocolsItem);
    return this;
  }

  /**
   * Get protocols
   * @return protocols
   */
  
  @Schema(name = "protocols", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("protocols")
  public List<String> getProtocols() {
    return protocols;
  }

  public void setProtocols(List<String> protocols) {
    this.protocols = protocols;
  }

  public GetNetArchitecture200Response securityLevels(List<String> securityLevels) {
    this.securityLevels = securityLevels;
    return this;
  }

  public GetNetArchitecture200Response addSecurityLevelsItem(String securityLevelsItem) {
    if (this.securityLevels == null) {
      this.securityLevels = new ArrayList<>();
    }
    this.securityLevels.add(securityLevelsItem);
    return this;
  }

  /**
   * Get securityLevels
   * @return securityLevels
   */
  
  @Schema(name = "security_levels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_levels")
  public List<String> getSecurityLevels() {
    return securityLevels;
  }

  public void setSecurityLevels(List<String> securityLevels) {
    this.securityLevels = securityLevels;
  }

  public GetNetArchitecture200Response blackwall(@Nullable BlackwallInfo blackwall) {
    this.blackwall = blackwall;
    return this;
  }

  /**
   * Get blackwall
   * @return blackwall
   */
  @Valid 
  @Schema(name = "blackwall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blackwall")
  public @Nullable BlackwallInfo getBlackwall() {
    return blackwall;
  }

  public void setBlackwall(@Nullable BlackwallInfo blackwall) {
    this.blackwall = blackwall;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNetArchitecture200Response getNetArchitecture200Response = (GetNetArchitecture200Response) o;
    return Objects.equals(this.architectureLayers, getNetArchitecture200Response.architectureLayers) &&
        Objects.equals(this.protocols, getNetArchitecture200Response.protocols) &&
        Objects.equals(this.securityLevels, getNetArchitecture200Response.securityLevels) &&
        Objects.equals(this.blackwall, getNetArchitecture200Response.blackwall);
  }

  @Override
  public int hashCode() {
    return Objects.hash(architectureLayers, protocols, securityLevels, blackwall);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNetArchitecture200Response {\n");
    sb.append("    architectureLayers: ").append(toIndentedString(architectureLayers)).append("\n");
    sb.append("    protocols: ").append(toIndentedString(protocols)).append("\n");
    sb.append("    securityLevels: ").append(toIndentedString(securityLevels)).append("\n");
    sb.append("    blackwall: ").append(toIndentedString(blackwall)).append("\n");
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

