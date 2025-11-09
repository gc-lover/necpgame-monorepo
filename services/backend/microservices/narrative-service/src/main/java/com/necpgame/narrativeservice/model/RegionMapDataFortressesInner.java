package com.necpgame.narrativeservice.model;

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
 * RegionMapDataFortressesInner
 */

@JsonTypeName("RegionMap_data_fortresses_inner")

public class RegionMapDataFortressesInner {

  private @Nullable String fortressId;

  private @Nullable String fortressName;

  private @Nullable Integer netrunnerQuests;

  public RegionMapDataFortressesInner fortressId(@Nullable String fortressId) {
    this.fortressId = fortressId;
    return this;
  }

  /**
   * Get fortressId
   * @return fortressId
   */
  
  @Schema(name = "fortress_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fortress_id")
  public @Nullable String getFortressId() {
    return fortressId;
  }

  public void setFortressId(@Nullable String fortressId) {
    this.fortressId = fortressId;
  }

  public RegionMapDataFortressesInner fortressName(@Nullable String fortressName) {
    this.fortressName = fortressName;
    return this;
  }

  /**
   * Get fortressName
   * @return fortressName
   */
  
  @Schema(name = "fortress_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fortress_name")
  public @Nullable String getFortressName() {
    return fortressName;
  }

  public void setFortressName(@Nullable String fortressName) {
    this.fortressName = fortressName;
  }

  public RegionMapDataFortressesInner netrunnerQuests(@Nullable Integer netrunnerQuests) {
    this.netrunnerQuests = netrunnerQuests;
    return this;
  }

  /**
   * Get netrunnerQuests
   * @return netrunnerQuests
   */
  
  @Schema(name = "netrunner_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netrunner_quests")
  public @Nullable Integer getNetrunnerQuests() {
    return netrunnerQuests;
  }

  public void setNetrunnerQuests(@Nullable Integer netrunnerQuests) {
    this.netrunnerQuests = netrunnerQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionMapDataFortressesInner regionMapDataFortressesInner = (RegionMapDataFortressesInner) o;
    return Objects.equals(this.fortressId, regionMapDataFortressesInner.fortressId) &&
        Objects.equals(this.fortressName, regionMapDataFortressesInner.fortressName) &&
        Objects.equals(this.netrunnerQuests, regionMapDataFortressesInner.netrunnerQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fortressId, fortressName, netrunnerQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionMapDataFortressesInner {\n");
    sb.append("    fortressId: ").append(toIndentedString(fortressId)).append("\n");
    sb.append("    fortressName: ").append(toIndentedString(fortressName)).append("\n");
    sb.append("    netrunnerQuests: ").append(toIndentedString(netrunnerQuests)).append("\n");
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

