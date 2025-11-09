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
 * PartyCreateRequest
 */


public class PartyCreateRequest {

  private String name;

  private String mode;

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

  private VisibilityEnum visibility = VisibilityEnum.PRIVATE;

  private @Nullable Integer maxMembers;

  private @Nullable LootSettings lootSettings;

  private Boolean autoFill = false;

  public PartyCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PartyCreateRequest(String name, String mode) {
    this.name = name;
    this.mode = mode;
  }

  public PartyCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PartyCreateRequest mode(String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public PartyCreateRequest visibility(VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PartyCreateRequest maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * minimum: 2
   * maximum: 12
   * @return maxMembers
   */
  @Min(value = 2) @Max(value = 12) 
  @Schema(name = "maxMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMembers")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public PartyCreateRequest lootSettings(@Nullable LootSettings lootSettings) {
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

  public PartyCreateRequest autoFill(Boolean autoFill) {
    this.autoFill = autoFill;
    return this;
  }

  /**
   * Get autoFill
   * @return autoFill
   */
  
  @Schema(name = "autoFill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoFill")
  public Boolean getAutoFill() {
    return autoFill;
  }

  public void setAutoFill(Boolean autoFill) {
    this.autoFill = autoFill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyCreateRequest partyCreateRequest = (PartyCreateRequest) o;
    return Objects.equals(this.name, partyCreateRequest.name) &&
        Objects.equals(this.mode, partyCreateRequest.mode) &&
        Objects.equals(this.visibility, partyCreateRequest.visibility) &&
        Objects.equals(this.maxMembers, partyCreateRequest.maxMembers) &&
        Objects.equals(this.lootSettings, partyCreateRequest.lootSettings) &&
        Objects.equals(this.autoFill, partyCreateRequest.autoFill);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, mode, visibility, maxMembers, lootSettings, autoFill);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyCreateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    lootSettings: ").append(toIndentedString(lootSettings)).append("\n");
    sb.append("    autoFill: ").append(toIndentedString(autoFill)).append("\n");
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

