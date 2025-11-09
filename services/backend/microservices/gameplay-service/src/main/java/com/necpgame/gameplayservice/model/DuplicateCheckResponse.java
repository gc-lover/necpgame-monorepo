package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * DuplicateCheckResponse
 */


public class DuplicateCheckResponse {

  private @Nullable Boolean hasDuplicate;

  @Valid
  private List<String> locations = new ArrayList<>();

  /**
   * Gets or Sets suggestedAction
   */
  public enum SuggestedActionEnum {
    ALTERNATIVE_REWARD("ALTERNATIVE_REWARD"),
    
    ALLOW("ALLOW");

    private final String value;

    SuggestedActionEnum(String value) {
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
    public static SuggestedActionEnum fromValue(String value) {
      for (SuggestedActionEnum b : SuggestedActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SuggestedActionEnum suggestedAction;

  public DuplicateCheckResponse hasDuplicate(@Nullable Boolean hasDuplicate) {
    this.hasDuplicate = hasDuplicate;
    return this;
  }

  /**
   * Get hasDuplicate
   * @return hasDuplicate
   */
  
  @Schema(name = "hasDuplicate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasDuplicate")
  public @Nullable Boolean getHasDuplicate() {
    return hasDuplicate;
  }

  public void setHasDuplicate(@Nullable Boolean hasDuplicate) {
    this.hasDuplicate = hasDuplicate;
  }

  public DuplicateCheckResponse locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  public DuplicateCheckResponse addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Get locations
   * @return locations
   */
  
  @Schema(name = "locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations")
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public DuplicateCheckResponse suggestedAction(@Nullable SuggestedActionEnum suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  /**
   * Get suggestedAction
   * @return suggestedAction
   */
  
  @Schema(name = "suggestedAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedAction")
  public @Nullable SuggestedActionEnum getSuggestedAction() {
    return suggestedAction;
  }

  public void setSuggestedAction(@Nullable SuggestedActionEnum suggestedAction) {
    this.suggestedAction = suggestedAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DuplicateCheckResponse duplicateCheckResponse = (DuplicateCheckResponse) o;
    return Objects.equals(this.hasDuplicate, duplicateCheckResponse.hasDuplicate) &&
        Objects.equals(this.locations, duplicateCheckResponse.locations) &&
        Objects.equals(this.suggestedAction, duplicateCheckResponse.suggestedAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hasDuplicate, locations, suggestedAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DuplicateCheckResponse {\n");
    sb.append("    hasDuplicate: ").append(toIndentedString(hasDuplicate)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    suggestedAction: ").append(toIndentedString(suggestedAction)).append("\n");
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

