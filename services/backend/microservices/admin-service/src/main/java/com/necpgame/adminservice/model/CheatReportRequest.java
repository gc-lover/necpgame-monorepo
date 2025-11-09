package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.CheatReportRequestEvidence;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CheatReportRequest
 */


public class CheatReportRequest {

  private UUID reportedPlayerId;

  private JsonNullable<UUID> reporterId = JsonNullable.<UUID>undefined();

  /**
   * Gets or Sets cheatType
   */
  public enum CheatTypeEnum {
    SPEED_HACK("SPEED_HACK"),
    
    TELEPORT("TELEPORT"),
    
    DUPLICATION("DUPLICATION"),
    
    AIMBOT("AIMBOT"),
    
    ESP("ESP"),
    
    ECONOMY_EXPLOIT("ECONOMY_EXPLOIT"),
    
    BOTTING("BOTTING");

    private final String value;

    CheatTypeEnum(String value) {
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
    public static CheatTypeEnum fromValue(String value) {
      for (CheatTypeEnum b : CheatTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CheatTypeEnum cheatType;

  private @Nullable CheatReportRequestEvidence evidence;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    CRITICAL("CRITICAL");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  private @Nullable Boolean autoDetected;

  public CheatReportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CheatReportRequest(UUID reportedPlayerId, CheatTypeEnum cheatType) {
    this.reportedPlayerId = reportedPlayerId;
    this.cheatType = cheatType;
  }

  public CheatReportRequest reportedPlayerId(UUID reportedPlayerId) {
    this.reportedPlayerId = reportedPlayerId;
    return this;
  }

  /**
   * Get reportedPlayerId
   * @return reportedPlayerId
   */
  @NotNull @Valid 
  @Schema(name = "reported_player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reported_player_id")
  public UUID getReportedPlayerId() {
    return reportedPlayerId;
  }

  public void setReportedPlayerId(UUID reportedPlayerId) {
    this.reportedPlayerId = reportedPlayerId;
  }

  public CheatReportRequest reporterId(UUID reporterId) {
    this.reporterId = JsonNullable.of(reporterId);
    return this;
  }

  /**
   * Null если автоматический репорт
   * @return reporterId
   */
  @Valid 
  @Schema(name = "reporter_id", description = "Null если автоматический репорт", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reporter_id")
  public JsonNullable<UUID> getReporterId() {
    return reporterId;
  }

  public void setReporterId(JsonNullable<UUID> reporterId) {
    this.reporterId = reporterId;
  }

  public CheatReportRequest cheatType(CheatTypeEnum cheatType) {
    this.cheatType = cheatType;
    return this;
  }

  /**
   * Get cheatType
   * @return cheatType
   */
  @NotNull 
  @Schema(name = "cheat_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cheat_type")
  public CheatTypeEnum getCheatType() {
    return cheatType;
  }

  public void setCheatType(CheatTypeEnum cheatType) {
    this.cheatType = cheatType;
  }

  public CheatReportRequest evidence(@Nullable CheatReportRequestEvidence evidence) {
    this.evidence = evidence;
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  @Valid 
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidence")
  public @Nullable CheatReportRequestEvidence getEvidence() {
    return evidence;
  }

  public void setEvidence(@Nullable CheatReportRequestEvidence evidence) {
    this.evidence = evidence;
  }

  public CheatReportRequest severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public CheatReportRequest autoDetected(@Nullable Boolean autoDetected) {
    this.autoDetected = autoDetected;
    return this;
  }

  /**
   * Get autoDetected
   * @return autoDetected
   */
  
  @Schema(name = "auto_detected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auto_detected")
  public @Nullable Boolean getAutoDetected() {
    return autoDetected;
  }

  public void setAutoDetected(@Nullable Boolean autoDetected) {
    this.autoDetected = autoDetected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheatReportRequest cheatReportRequest = (CheatReportRequest) o;
    return Objects.equals(this.reportedPlayerId, cheatReportRequest.reportedPlayerId) &&
        equalsNullable(this.reporterId, cheatReportRequest.reporterId) &&
        Objects.equals(this.cheatType, cheatReportRequest.cheatType) &&
        Objects.equals(this.evidence, cheatReportRequest.evidence) &&
        Objects.equals(this.severity, cheatReportRequest.severity) &&
        Objects.equals(this.autoDetected, cheatReportRequest.autoDetected);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportedPlayerId, hashCodeNullable(reporterId), cheatType, evidence, severity, autoDetected);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheatReportRequest {\n");
    sb.append("    reportedPlayerId: ").append(toIndentedString(reportedPlayerId)).append("\n");
    sb.append("    reporterId: ").append(toIndentedString(reporterId)).append("\n");
    sb.append("    cheatType: ").append(toIndentedString(cheatType)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    autoDetected: ").append(toIndentedString(autoDetected)).append("\n");
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

