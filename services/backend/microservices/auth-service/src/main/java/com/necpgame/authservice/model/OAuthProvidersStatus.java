package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.authservice.model.LinkedProvider;
import com.necpgame.authservice.model.OAuthProvider;
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
 * OAuthProvidersStatus
 */


public class OAuthProvidersStatus {

  @Valid
  private List<@Valid LinkedProvider> linked = new ArrayList<>();

  @Valid
  private List<OAuthProvider> available = new ArrayList<>();

  public OAuthProvidersStatus linked(List<@Valid LinkedProvider> linked) {
    this.linked = linked;
    return this;
  }

  public OAuthProvidersStatus addLinkedItem(LinkedProvider linkedItem) {
    if (this.linked == null) {
      this.linked = new ArrayList<>();
    }
    this.linked.add(linkedItem);
    return this;
  }

  /**
   * Get linked
   * @return linked
   */
  @Valid 
  @Schema(name = "linked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linked")
  public List<@Valid LinkedProvider> getLinked() {
    return linked;
  }

  public void setLinked(List<@Valid LinkedProvider> linked) {
    this.linked = linked;
  }

  public OAuthProvidersStatus available(List<OAuthProvider> available) {
    this.available = available;
    return this;
  }

  public OAuthProvidersStatus addAvailableItem(OAuthProvider availableItem) {
    if (this.available == null) {
      this.available = new ArrayList<>();
    }
    this.available.add(availableItem);
    return this;
  }

  /**
   * Get available
   * @return available
   */
  @Valid 
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public List<OAuthProvider> getAvailable() {
    return available;
  }

  public void setAvailable(List<OAuthProvider> available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthProvidersStatus oauthProvidersStatus = (OAuthProvidersStatus) o;
    return Objects.equals(this.linked, oauthProvidersStatus.linked) &&
        Objects.equals(this.available, oauthProvidersStatus.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(linked, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthProvidersStatus {\n");
    sb.append("    linked: ").append(toIndentedString(linked)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

