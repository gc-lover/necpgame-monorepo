package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.partyservice.model.LootSettings;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PartyUpdateRequest
 */


public class PartyUpdateRequest {

  private @Nullable String name;

  private @Nullable String mode;

  /**
   * Gets or Sets visibility
   */
  public enum VisibilityEnum {
    PRIVATE("PRIVATE"),
    
    FRIENDS("FRIENDS"),
    
    PUBLIC("PUBLIC");

    private final String value;

    VisibilityEnum(String value) {
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
    public static VisibilityEnum fromValue(String value) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VisibilityEnum visibility;

  private @Nullable Integer maxMembers;

  private @Nullable LootSettings lootSettings;

  public PartyUpdateRequest name(@Nullable String name) {
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

  public PartyUpdateRequest mode(@Nullable String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable String getMode() {
    return mode;
  }

  public void setMode(@Nullable String mode) {
    this.mode = mode;
  }

  public PartyUpdateRequest visibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PartyUpdateRequest maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * @return maxMembers
   */
  
  @Schema(name = "maxMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMembers")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public PartyUpdateRequest lootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
    return this;
  }

  /**
   * Get lootSettings
   * @return lootSettings
   */
  @Valid 
  @Schema(name = "lootSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootSettings")
  public @Nullable LootSettings getLootSettings() {
    return lootSettings;
  }

  public void setLootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyUpdateRequest partyUpdateRequest = (PartyUpdateRequest) o;
    return Objects.equals(this.name, partyUpdateRequest.name) &&
        Objects.equals(this.mode, partyUpdateRequest.mode) &&
        Objects.equals(this.visibility, partyUpdateRequest.visibility) &&
        Objects.equals(this.maxMembers, partyUpdateRequest.maxMembers) &&
        Objects.equals(this.lootSettings, partyUpdateRequest.lootSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, mode, visibility, maxMembers, lootSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyUpdateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    lootSettings: ").append(toIndentedString(lootSettings)).append("\n");
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

