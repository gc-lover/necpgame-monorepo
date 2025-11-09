package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RestoreState
 */


public class RestoreState {

  private @Nullable String location;

  private @Nullable String inventoryChecksum;

  private @Nullable String partyId;

  public RestoreState location(@Nullable String location) {
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

  public RestoreState inventoryChecksum(@Nullable String inventoryChecksum) {
    this.inventoryChecksum = inventoryChecksum;
    return this;
  }

  /**
   * Get inventoryChecksum
   * @return inventoryChecksum
   */
  
  @Schema(name = "inventoryChecksum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inventoryChecksum")
  public @Nullable String getInventoryChecksum() {
    return inventoryChecksum;
  }

  public void setInventoryChecksum(@Nullable String inventoryChecksum) {
    this.inventoryChecksum = inventoryChecksum;
  }

  public RestoreState partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestoreState restoreState = (RestoreState) o;
    return Objects.equals(this.location, restoreState.location) &&
        Objects.equals(this.inventoryChecksum, restoreState.inventoryChecksum) &&
        Objects.equals(this.partyId, restoreState.partyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, inventoryChecksum, partyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestoreState {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    inventoryChecksum: ").append(toIndentedString(inventoryChecksum)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
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

