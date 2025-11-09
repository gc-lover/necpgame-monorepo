package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ReportResolutionRequest
 */


public class ReportResolutionRequest {

  /**
   * Gets or Sets resolution
   */
  public enum ResolutionEnum {
    WARN("WARN"),
    
    BAN("BAN"),
    
    NO_ACTION("NO_ACTION");

    private final String value;

    ResolutionEnum(String value) {
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
    public static ResolutionEnum fromValue(String value) {
      for (ResolutionEnum b : ResolutionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResolutionEnum resolution;

  private @Nullable String notes;

  private @Nullable UUID appliedBanId;

  public ReportResolutionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReportResolutionRequest(ResolutionEnum resolution) {
    this.resolution = resolution;
  }

  public ReportResolutionRequest resolution(ResolutionEnum resolution) {
    this.resolution = resolution;
    return this;
  }

  /**
   * Get resolution
   * @return resolution
   */
  @NotNull 
  @Schema(name = "resolution", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resolution")
  public ResolutionEnum getResolution() {
    return resolution;
  }

  public void setResolution(ResolutionEnum resolution) {
    this.resolution = resolution;
  }

  public ReportResolutionRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  @Size(max = 500) 
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  public ReportResolutionRequest appliedBanId(@Nullable UUID appliedBanId) {
    this.appliedBanId = appliedBanId;
    return this;
  }

  /**
   * Get appliedBanId
   * @return appliedBanId
   */
  @Valid 
  @Schema(name = "appliedBanId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedBanId")
  public @Nullable UUID getAppliedBanId() {
    return appliedBanId;
  }

  public void setAppliedBanId(@Nullable UUID appliedBanId) {
    this.appliedBanId = appliedBanId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReportResolutionRequest reportResolutionRequest = (ReportResolutionRequest) o;
    return Objects.equals(this.resolution, reportResolutionRequest.resolution) &&
        Objects.equals(this.notes, reportResolutionRequest.notes) &&
        Objects.equals(this.appliedBanId, reportResolutionRequest.appliedBanId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resolution, notes, appliedBanId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportResolutionRequest {\n");
    sb.append("    resolution: ").append(toIndentedString(resolution)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    appliedBanId: ").append(toIndentedString(appliedBanId)).append("\n");
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

