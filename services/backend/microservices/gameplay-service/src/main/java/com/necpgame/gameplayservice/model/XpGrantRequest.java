package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * XpGrantRequest
 */


public class XpGrantRequest {

  private String playerId;

  private String seasonId;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    DAILY_QUEST("DAILY_QUEST"),
    
    MATCH_WIN("MATCH_WIN"),
    
    ACHIEVEMENT("ACHIEVEMENT"),
    
    PLAYTIME("PLAYTIME"),
    
    EVENT("EVENT");

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

  private Integer amount;

  private @Nullable BigDecimal multiplier;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public XpGrantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public XpGrantRequest(String playerId, String seasonId, SourceEnum source, Integer amount) {
    this.playerId = playerId;
    this.seasonId = seasonId;
    this.source = source;
    this.amount = amount;
  }

  public XpGrantRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public XpGrantRequest seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  @NotNull 
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonId")
  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public XpGrantRequest source(SourceEnum source) {
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

  public XpGrantRequest amount(Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 1
   * @return amount
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public XpGrantRequest multiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * minimum: 1
   * @return multiplier
   */
  @Valid @DecimalMin(value = "1") 
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  public XpGrantRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public XpGrantRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpGrantRequest xpGrantRequest = (XpGrantRequest) o;
    return Objects.equals(this.playerId, xpGrantRequest.playerId) &&
        Objects.equals(this.seasonId, xpGrantRequest.seasonId) &&
        Objects.equals(this.source, xpGrantRequest.source) &&
        Objects.equals(this.amount, xpGrantRequest.amount) &&
        Objects.equals(this.multiplier, xpGrantRequest.multiplier) &&
        Objects.equals(this.metadata, xpGrantRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, seasonId, source, amount, multiplier, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpGrantRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

