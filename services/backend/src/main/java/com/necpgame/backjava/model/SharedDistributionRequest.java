package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.RollConfig;
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
 * SharedDistributionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SharedDistributionRequest {

  private UUID resultId;

  private UUID partyId;

  /**
   * Gets or Sets threshold
   */
  public enum ThresholdEnum {
    FREE_FOR_ALL("FREE_FOR_ALL"),
    
    ROUND_ROBIN("ROUND_ROBIN"),
    
    MASTER_LOOTER("MASTER_LOOTER");

    private final String value;

    ThresholdEnum(String value) {
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
    public static ThresholdEnum fromValue(String value) {
      for (ThresholdEnum b : ThresholdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ThresholdEnum threshold;

  private @Nullable Boolean autoAssignCommons;

  private @Nullable RollConfig rollConfig;

  public SharedDistributionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SharedDistributionRequest(UUID resultId, UUID partyId) {
    this.resultId = resultId;
    this.partyId = partyId;
  }

  public SharedDistributionRequest resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public SharedDistributionRequest partyId(UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @NotNull @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("partyId")
  public UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  public SharedDistributionRequest threshold(@Nullable ThresholdEnum threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable ThresholdEnum getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable ThresholdEnum threshold) {
    this.threshold = threshold;
  }

  public SharedDistributionRequest autoAssignCommons(@Nullable Boolean autoAssignCommons) {
    this.autoAssignCommons = autoAssignCommons;
    return this;
  }

  /**
   * Get autoAssignCommons
   * @return autoAssignCommons
   */
  
  @Schema(name = "autoAssignCommons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAssignCommons")
  public @Nullable Boolean getAutoAssignCommons() {
    return autoAssignCommons;
  }

  public void setAutoAssignCommons(@Nullable Boolean autoAssignCommons) {
    this.autoAssignCommons = autoAssignCommons;
  }

  public SharedDistributionRequest rollConfig(@Nullable RollConfig rollConfig) {
    this.rollConfig = rollConfig;
    return this;
  }

  /**
   * Get rollConfig
   * @return rollConfig
   */
  @Valid 
  @Schema(name = "rollConfig", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollConfig")
  public @Nullable RollConfig getRollConfig() {
    return rollConfig;
  }

  public void setRollConfig(@Nullable RollConfig rollConfig) {
    this.rollConfig = rollConfig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SharedDistributionRequest sharedDistributionRequest = (SharedDistributionRequest) o;
    return Objects.equals(this.resultId, sharedDistributionRequest.resultId) &&
        Objects.equals(this.partyId, sharedDistributionRequest.partyId) &&
        Objects.equals(this.threshold, sharedDistributionRequest.threshold) &&
        Objects.equals(this.autoAssignCommons, sharedDistributionRequest.autoAssignCommons) &&
        Objects.equals(this.rollConfig, sharedDistributionRequest.rollConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, partyId, threshold, autoAssignCommons, rollConfig);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SharedDistributionRequest {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    autoAssignCommons: ").append(toIndentedString(autoAssignCommons)).append("\n");
    sb.append("    rollConfig: ").append(toIndentedString(rollConfig)).append("\n");
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

