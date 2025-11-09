package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreatePriceAlertRequest
 */

@JsonTypeName("createPriceAlert_request")

public class CreatePriceAlertRequest {

  private String characterId;

  private String itemId;

  /**
   * Gets or Sets alertType
   */
  public enum AlertTypeEnum {
    PRICE_ABOVE("price_above"),
    
    PRICE_BELOW("price_below"),
    
    VOLUME_SPIKE("volume_spike");

    private final String value;

    AlertTypeEnum(String value) {
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
    public static AlertTypeEnum fromValue(String value) {
      for (AlertTypeEnum b : AlertTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AlertTypeEnum alertType;

  private BigDecimal targetPrice;

  /**
   * Gets or Sets notificationMethod
   */
  public enum NotificationMethodEnum {
    IN_GAME("in_game"),
    
    EMAIL("email"),
    
    BOTH("both");

    private final String value;

    NotificationMethodEnum(String value) {
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
    public static NotificationMethodEnum fromValue(String value) {
      for (NotificationMethodEnum b : NotificationMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable NotificationMethodEnum notificationMethod;

  public CreatePriceAlertRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreatePriceAlertRequest(String characterId, String itemId, AlertTypeEnum alertType, BigDecimal targetPrice) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.alertType = alertType;
    this.targetPrice = targetPrice;
  }

  public CreatePriceAlertRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CreatePriceAlertRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public CreatePriceAlertRequest alertType(AlertTypeEnum alertType) {
    this.alertType = alertType;
    return this;
  }

  /**
   * Get alertType
   * @return alertType
   */
  @NotNull 
  @Schema(name = "alert_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alert_type")
  public AlertTypeEnum getAlertType() {
    return alertType;
  }

  public void setAlertType(AlertTypeEnum alertType) {
    this.alertType = alertType;
  }

  public CreatePriceAlertRequest targetPrice(BigDecimal targetPrice) {
    this.targetPrice = targetPrice;
    return this;
  }

  /**
   * Get targetPrice
   * @return targetPrice
   */
  @NotNull @Valid 
  @Schema(name = "target_price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_price")
  public BigDecimal getTargetPrice() {
    return targetPrice;
  }

  public void setTargetPrice(BigDecimal targetPrice) {
    this.targetPrice = targetPrice;
  }

  public CreatePriceAlertRequest notificationMethod(@Nullable NotificationMethodEnum notificationMethod) {
    this.notificationMethod = notificationMethod;
    return this;
  }

  /**
   * Get notificationMethod
   * @return notificationMethod
   */
  
  @Schema(name = "notification_method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notification_method")
  public @Nullable NotificationMethodEnum getNotificationMethod() {
    return notificationMethod;
  }

  public void setNotificationMethod(@Nullable NotificationMethodEnum notificationMethod) {
    this.notificationMethod = notificationMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePriceAlertRequest createPriceAlertRequest = (CreatePriceAlertRequest) o;
    return Objects.equals(this.characterId, createPriceAlertRequest.characterId) &&
        Objects.equals(this.itemId, createPriceAlertRequest.itemId) &&
        Objects.equals(this.alertType, createPriceAlertRequest.alertType) &&
        Objects.equals(this.targetPrice, createPriceAlertRequest.targetPrice) &&
        Objects.equals(this.notificationMethod, createPriceAlertRequest.notificationMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, alertType, targetPrice, notificationMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePriceAlertRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    alertType: ").append(toIndentedString(alertType)).append("\n");
    sb.append("    targetPrice: ").append(toIndentedString(targetPrice)).append("\n");
    sb.append("    notificationMethod: ").append(toIndentedString(notificationMethod)).append("\n");
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

