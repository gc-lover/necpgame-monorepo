package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * TriggerSecurityScanRequest
 */

@JsonTypeName("triggerSecurityScan_request")

public class TriggerSecurityScanRequest {

  /**
   * Gets or Sets scanType
   */
  public enum ScanTypeEnum {
    QUICK("quick"),
    
    FULL("full"),
    
    TARGETED("targeted");

    private final String value;

    ScanTypeEnum(String value) {
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
    public static ScanTypeEnum fromValue(String value) {
      for (ScanTypeEnum b : ScanTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ScanTypeEnum scanType = ScanTypeEnum.QUICK;

  @Valid
  private List<String> targets = new ArrayList<>();

  public TriggerSecurityScanRequest scanType(ScanTypeEnum scanType) {
    this.scanType = scanType;
    return this;
  }

  /**
   * Get scanType
   * @return scanType
   */
  
  @Schema(name = "scan_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scan_type")
  public ScanTypeEnum getScanType() {
    return scanType;
  }

  public void setScanType(ScanTypeEnum scanType) {
    this.scanType = scanType;
  }

  public TriggerSecurityScanRequest targets(List<String> targets) {
    this.targets = targets;
    return this;
  }

  public TriggerSecurityScanRequest addTargetsItem(String targetsItem) {
    if (this.targets == null) {
      this.targets = new ArrayList<>();
    }
    this.targets.add(targetsItem);
    return this;
  }

  /**
   * Get targets
   * @return targets
   */
  
  @Schema(name = "targets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targets")
  public List<String> getTargets() {
    return targets;
  }

  public void setTargets(List<String> targets) {
    this.targets = targets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerSecurityScanRequest triggerSecurityScanRequest = (TriggerSecurityScanRequest) o;
    return Objects.equals(this.scanType, triggerSecurityScanRequest.scanType) &&
        Objects.equals(this.targets, triggerSecurityScanRequest.targets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scanType, targets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerSecurityScanRequest {\n");
    sb.append("    scanType: ").append(toIndentedString(scanType)).append("\n");
    sb.append("    targets: ").append(toIndentedString(targets)).append("\n");
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

