package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.Appeal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * BanDetailed
 */


public class BanDetailed {

  private @Nullable UUID banId;

  private @Nullable UUID playerId;

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

  private @Nullable TypeEnum type;

  private @Nullable String reason;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    EXPIRED("EXPIRED"),
    
    APPEALED("APPEALED"),
    
    LIFTED("LIFTED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime issuedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> expiresAt = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable UUID issuedBy;

  @Valid
  private List<UUID> relatedReports = new ArrayList<>();

  private @Nullable Appeal appeal;

  public BanDetailed banId(@Nullable UUID banId) {
    this.banId = banId;
    return this;
  }

  /**
   * Get banId
   * @return banId
   */
  @Valid 
  @Schema(name = "ban_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ban_id")
  public @Nullable UUID getBanId() {
    return banId;
  }

  public void setBanId(@Nullable UUID banId) {
    this.banId = banId;
  }

  public BanDetailed playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public BanDetailed type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public BanDetailed reason(@Nullable String reason) {
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

  public BanDetailed status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public BanDetailed issuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
    return this;
  }

  /**
   * Get issuedAt
   * @return issuedAt
   */
  @Valid 
  @Schema(name = "issued_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issued_at")
  public @Nullable OffsetDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public BanDetailed expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = JsonNullable.of(expiresAt);
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public JsonNullable<OffsetDateTime> getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(JsonNullable<OffsetDateTime> expiresAt) {
    this.expiresAt = expiresAt;
  }

  public BanDetailed issuedBy(@Nullable UUID issuedBy) {
    this.issuedBy = issuedBy;
    return this;
  }

  /**
   * Get issuedBy
   * @return issuedBy
   */
  @Valid 
  @Schema(name = "issued_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issued_by")
  public @Nullable UUID getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(@Nullable UUID issuedBy) {
    this.issuedBy = issuedBy;
  }

  public BanDetailed relatedReports(List<UUID> relatedReports) {
    this.relatedReports = relatedReports;
    return this;
  }

  public BanDetailed addRelatedReportsItem(UUID relatedReportsItem) {
    if (this.relatedReports == null) {
      this.relatedReports = new ArrayList<>();
    }
    this.relatedReports.add(relatedReportsItem);
    return this;
  }

  /**
   * Get relatedReports
   * @return relatedReports
   */
  @Valid 
  @Schema(name = "related_reports", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_reports")
  public List<UUID> getRelatedReports() {
    return relatedReports;
  }

  public void setRelatedReports(List<UUID> relatedReports) {
    this.relatedReports = relatedReports;
  }

  public BanDetailed appeal(@Nullable Appeal appeal) {
    this.appeal = appeal;
    return this;
  }

  /**
   * Get appeal
   * @return appeal
   */
  @Valid 
  @Schema(name = "appeal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appeal")
  public @Nullable Appeal getAppeal() {
    return appeal;
  }

  public void setAppeal(@Nullable Appeal appeal) {
    this.appeal = appeal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BanDetailed banDetailed = (BanDetailed) o;
    return Objects.equals(this.banId, banDetailed.banId) &&
        Objects.equals(this.playerId, banDetailed.playerId) &&
        Objects.equals(this.type, banDetailed.type) &&
        Objects.equals(this.reason, banDetailed.reason) &&
        Objects.equals(this.status, banDetailed.status) &&
        Objects.equals(this.issuedAt, banDetailed.issuedAt) &&
        equalsNullable(this.expiresAt, banDetailed.expiresAt) &&
        Objects.equals(this.issuedBy, banDetailed.issuedBy) &&
        Objects.equals(this.relatedReports, banDetailed.relatedReports) &&
        Objects.equals(this.appeal, banDetailed.appeal);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(banId, playerId, type, reason, status, issuedAt, hashCodeNullable(expiresAt), issuedBy, relatedReports, appeal);
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
    sb.append("class BanDetailed {\n");
    sb.append("    banId: ").append(toIndentedString(banId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    issuedAt: ").append(toIndentedString(issuedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    issuedBy: ").append(toIndentedString(issuedBy)).append("\n");
    sb.append("    relatedReports: ").append(toIndentedString(relatedReports)).append("\n");
    sb.append("    appeal: ").append(toIndentedString(appeal)).append("\n");
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

