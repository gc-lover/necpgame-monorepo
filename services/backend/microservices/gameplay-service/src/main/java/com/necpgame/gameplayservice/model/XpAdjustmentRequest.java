package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * XpAdjustmentRequest
 */


public class XpAdjustmentRequest {

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    QUEST_REWARD("quest_reward"),
    
    GM_COMMAND("gm_command"),
    
    COMPENSATION("compensation"),
    
    SANCTION("sanction");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceEnum source;

  private Integer delta;

  private @Nullable String reason;

  public XpAdjustmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public XpAdjustmentRequest(SourceEnum source, Integer delta) {
    this.source = source;
    this.delta = delta;
  }

  public XpAdjustmentRequest source(SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public XpAdjustmentRequest delta(Integer delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Integer getDelta() {
    return delta;
  }

  public void setDelta(Integer delta) {
    this.delta = delta;
  }

  public XpAdjustmentRequest reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpAdjustmentRequest xpAdjustmentRequest = (XpAdjustmentRequest) o;
    return Objects.equals(this.source, xpAdjustmentRequest.source) &&
        Objects.equals(this.delta, xpAdjustmentRequest.delta) &&
        Objects.equals(this.reason, xpAdjustmentRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, delta, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpAdjustmentRequest {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

