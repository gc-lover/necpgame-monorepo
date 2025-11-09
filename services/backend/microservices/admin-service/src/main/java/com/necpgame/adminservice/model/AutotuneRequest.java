package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AdjustmentAction;
import com.necpgame.adminservice.model.AutotuneRequestInitiator;
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
 * AutotuneRequest
 */


public class AutotuneRequest {

  private AutotuneRequestInitiator initiator;

  @Valid
  private List<@Valid AdjustmentAction> adjustments = new ArrayList<>();

  private Boolean sandbox = false;

  private Integer version;

  private @Nullable String notes;

  public AutotuneRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AutotuneRequest(AutotuneRequestInitiator initiator, List<@Valid AdjustmentAction> adjustments, Integer version) {
    this.initiator = initiator;
    this.adjustments = adjustments;
    this.version = version;
  }

  public AutotuneRequest initiator(AutotuneRequestInitiator initiator) {
    this.initiator = initiator;
    return this;
  }

  /**
   * Get initiator
   * @return initiator
   */
  @NotNull @Valid 
  @Schema(name = "initiator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("initiator")
  public AutotuneRequestInitiator getInitiator() {
    return initiator;
  }

  public void setInitiator(AutotuneRequestInitiator initiator) {
    this.initiator = initiator;
  }

  public AutotuneRequest adjustments(List<@Valid AdjustmentAction> adjustments) {
    this.adjustments = adjustments;
    return this;
  }

  public AutotuneRequest addAdjustmentsItem(AdjustmentAction adjustmentsItem) {
    if (this.adjustments == null) {
      this.adjustments = new ArrayList<>();
    }
    this.adjustments.add(adjustmentsItem);
    return this;
  }

  /**
   * Get adjustments
   * @return adjustments
   */
  @NotNull @Valid @Size(min = 1, max = 25) 
  @Schema(name = "adjustments", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adjustments")
  public List<@Valid AdjustmentAction> getAdjustments() {
    return adjustments;
  }

  public void setAdjustments(List<@Valid AdjustmentAction> adjustments) {
    this.adjustments = adjustments;
  }

  public AutotuneRequest sandbox(Boolean sandbox) {
    this.sandbox = sandbox;
    return this;
  }

  /**
   * Get sandbox
   * @return sandbox
   */
  
  @Schema(name = "sandbox", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sandbox")
  public Boolean getSandbox() {
    return sandbox;
  }

  public void setSandbox(Boolean sandbox) {
    this.sandbox = sandbox;
  }

  public AutotuneRequest version(Integer version) {
    this.version = version;
    return this;
  }

  /**
   * Optimistic locking version
   * minimum: 1
   * @return version
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "version", description = "Optimistic locking version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public AutotuneRequest notes(@Nullable String notes) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutotuneRequest autotuneRequest = (AutotuneRequest) o;
    return Objects.equals(this.initiator, autotuneRequest.initiator) &&
        Objects.equals(this.adjustments, autotuneRequest.adjustments) &&
        Objects.equals(this.sandbox, autotuneRequest.sandbox) &&
        Objects.equals(this.version, autotuneRequest.version) &&
        Objects.equals(this.notes, autotuneRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(initiator, adjustments, sandbox, version, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutotuneRequest {\n");
    sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
    sb.append("    adjustments: ").append(toIndentedString(adjustments)).append("\n");
    sb.append("    sandbox: ").append(toIndentedString(sandbox)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

