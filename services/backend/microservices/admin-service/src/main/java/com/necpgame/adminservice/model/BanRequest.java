package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * BanRequest
 */


public class BanRequest {

  private UUID playerId;

  private String reason;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    TEMPORARY("TEMPORARY"),
    
    PERMANENT("PERMANENT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private JsonNullable<Integer> durationDays = JsonNullable.<Integer>undefined();

  private JsonNullable<UUID> relatedReportId = JsonNullable.<UUID>undefined();

  public BanRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BanRequest(UUID playerId, String reason, TypeEnum type) {
    this.playerId = playerId;
    this.reason = reason;
    this.type = type;
  }

  public BanRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public BanRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public BanRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public BanRequest durationDays(Integer durationDays) {
    this.durationDays = JsonNullable.of(durationDays);
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public JsonNullable<Integer> getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(JsonNullable<Integer> durationDays) {
    this.durationDays = durationDays;
  }

  public BanRequest relatedReportId(UUID relatedReportId) {
    this.relatedReportId = JsonNullable.of(relatedReportId);
    return this;
  }

  /**
   * Get relatedReportId
   * @return relatedReportId
   */
  @Valid 
  @Schema(name = "related_report_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_report_id")
  public JsonNullable<UUID> getRelatedReportId() {
    return relatedReportId;
  }

  public void setRelatedReportId(JsonNullable<UUID> relatedReportId) {
    this.relatedReportId = relatedReportId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BanRequest banRequest = (BanRequest) o;
    return Objects.equals(this.playerId, banRequest.playerId) &&
        Objects.equals(this.reason, banRequest.reason) &&
        Objects.equals(this.type, banRequest.type) &&
        equalsNullable(this.durationDays, banRequest.durationDays) &&
        equalsNullable(this.relatedReportId, banRequest.relatedReportId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, reason, type, hashCodeNullable(durationDays), hashCodeNullable(relatedReportId));
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
    sb.append("class BanRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    relatedReportId: ").append(toIndentedString(relatedReportId)).append("\n");
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

