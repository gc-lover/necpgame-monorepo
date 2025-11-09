package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LockedOption
 */


public class LockedOption {

  private @Nullable String optionId;

  private @Nullable String reason;

  @Valid
  private List<String> missingFlags = new ArrayList<>();

  @Valid
  private List<String> missingItems = new ArrayList<>();

  public LockedOption optionId(@Nullable String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  
  @Schema(name = "optionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optionId")
  public @Nullable String getOptionId() {
    return optionId;
  }

  public void setOptionId(@Nullable String optionId) {
    this.optionId = optionId;
  }

  public LockedOption reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public LockedOption missingFlags(List<String> missingFlags) {
    this.missingFlags = missingFlags;
    return this;
  }

  public LockedOption addMissingFlagsItem(String missingFlagsItem) {
    if (this.missingFlags == null) {
      this.missingFlags = new ArrayList<>();
    }
    this.missingFlags.add(missingFlagsItem);
    return this;
  }

  /**
   * Get missingFlags
   * @return missingFlags
   */
  
  @Schema(name = "missingFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missingFlags")
  public List<String> getMissingFlags() {
    return missingFlags;
  }

  public void setMissingFlags(List<String> missingFlags) {
    this.missingFlags = missingFlags;
  }

  public LockedOption missingItems(List<String> missingItems) {
    this.missingItems = missingItems;
    return this;
  }

  public LockedOption addMissingItemsItem(String missingItemsItem) {
    if (this.missingItems == null) {
      this.missingItems = new ArrayList<>();
    }
    this.missingItems.add(missingItemsItem);
    return this;
  }

  /**
   * Get missingItems
   * @return missingItems
   */
  
  @Schema(name = "missingItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missingItems")
  public List<String> getMissingItems() {
    return missingItems;
  }

  public void setMissingItems(List<String> missingItems) {
    this.missingItems = missingItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LockedOption lockedOption = (LockedOption) o;
    return Objects.equals(this.optionId, lockedOption.optionId) &&
        Objects.equals(this.reason, lockedOption.reason) &&
        Objects.equals(this.missingFlags, lockedOption.missingFlags) &&
        Objects.equals(this.missingItems, lockedOption.missingItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionId, reason, missingFlags, missingItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LockedOption {\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    missingFlags: ").append(toIndentedString(missingFlags)).append("\n");
    sb.append("    missingItems: ").append(toIndentedString(missingItems)).append("\n");
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

