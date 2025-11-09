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
 * RedeemRequest
 */


public class RedeemRequest {

  private String playerId;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    ACHIEVEMENT("achievement"),
    
    EVENT("event"),
    
    BATTLE_PASS("battle_pass"),
    
    PROMO_CODE("promo_code");

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

  private @Nullable String referenceId;

  private @Nullable String promoCode;

  public RedeemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RedeemRequest(String playerId, SourceEnum source) {
    this.playerId = playerId;
    this.source = source;
  }

  public RedeemRequest playerId(String playerId) {
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

  public RedeemRequest source(SourceEnum source) {
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

  public RedeemRequest referenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  /**
   * Get referenceId
   * @return referenceId
   */
  
  @Schema(name = "referenceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceId")
  public @Nullable String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
  }

  public RedeemRequest promoCode(@Nullable String promoCode) {
    this.promoCode = promoCode;
    return this;
  }

  /**
   * Get promoCode
   * @return promoCode
   */
  
  @Schema(name = "promoCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoCode")
  public @Nullable String getPromoCode() {
    return promoCode;
  }

  public void setPromoCode(@Nullable String promoCode) {
    this.promoCode = promoCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RedeemRequest redeemRequest = (RedeemRequest) o;
    return Objects.equals(this.playerId, redeemRequest.playerId) &&
        Objects.equals(this.source, redeemRequest.source) &&
        Objects.equals(this.referenceId, redeemRequest.referenceId) &&
        Objects.equals(this.promoCode, redeemRequest.promoCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, source, referenceId, promoCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RedeemRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    referenceId: ").append(toIndentedString(referenceId)).append("\n");
    sb.append("    promoCode: ").append(toIndentedString(promoCode)).append("\n");
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

